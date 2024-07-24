package com.byx.pub.filter;

import java.lang.annotation.*;

/**
 * redis 分布式锁注解，支持SpringEL表达式
 * @author K
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisLock {

    /**
     * 设置key，支持SpringEL表达式
     * @return key
     */
    String key() default "";

    /**
     * 是否等待锁，不等待直接抛异常
     * @return true-等待锁释放，false-不等待锁释放，直接抛RedisLockException异常
     */
    boolean isWaitForLock() default true;

    /**
     * 设置key前缀
     * @return key前缀
     */
    String prefix() default "";

    /**
     * key的过期时间, 毫秒级
     * @return 过期时间
     */
    long expire() default 0L;

    /**
     * 不想使用SpringEL表达式获取可以，可以使用key固定值
     * @return key
     */
    String constantKey() default "";
}
