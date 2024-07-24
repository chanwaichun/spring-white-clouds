package com.byx.pub.filter;


import com.byx.pub.exception.RedisLockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * RedisLock aop拦截器
 * 必须引入redisson jar包
 * @author cyj
 **/
@Slf4j
@Aspect
@Configuration
@ConditionalOnClass({Redisson.class})
public class RedisLockAop {

    @Resource
    private RedissonClient redissonClient;

    public RedisLockAop() {
        log.info("redis lock aop 初始化");
    }

    @Pointcut("@annotation(com.byx.pub.filter.RedisLock)")
    public void addAdvice() {}

    @Around("addAdvice()")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {

        Method method = AopUtil.getMethod(pjp);
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        String key = genKey(pjp, redisLock);

        RLock lock = redissonClient.getLock(key);
        // 如果注解设置不等待锁，直接抛异常
        if (!redisLock.isWaitForLock() && lock.isLocked()) {
            String msg = String.format("缓存key:%s 已上锁", key);
            log.error(msg);
            throw new RedisLockException(msg);
        }

        if (redisLock.expire() > 0) {
            lock.lock(redisLock.expire(), TimeUnit.MILLISECONDS);
        } else {
            log.info("缓存key：{} 加锁", key);
            lock.lock();
        }

        try {
            return pjp.proceed();
        } finally {
            log.info("缓存key：{} 释放", key);
            lock.unlock();
        }
    }

    private String genKey(ProceedingJoinPoint pjp, RedisLock redisLock) {
        if (StringUtils.isAllBlank(redisLock.key(), redisLock.constantKey())) {
            throw new RuntimeException("key和constantKey不能同时为空");
        }
        if (StringUtils.isNoneBlank(redisLock.key(), redisLock.constantKey())) {
            throw new RuntimeException("key和constantKey不能同时有值");
        }

        // 获取固定值
        if (StringUtils.isNotBlank(redisLock.constantKey())) {
            return redisLock.constantKey();
        }

        String keyEl = redisLock.key();

        // 解析表达式,生成最终的key
        Object key = AopUtil.springElParser(keyEl, pjp);
        log.info("SpringEL表达式解析后的key:{}", key);

        String redisKey;
        if (StringUtils.isNotBlank(redisLock.prefix())) {
            redisKey = redisLock.prefix() + ":" + key;
        } else {
            redisKey = String.valueOf(key);
        }
        log.info("缓存key:{}", redisKey);
        return redisKey;
    }
}
