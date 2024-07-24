package com.byx.pub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byx.pub.bean.vo.LoginHeadBean;
import com.byx.pub.bean.vo.UserLoginVo;
import com.byx.pub.enums.ClientTypeEnum;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.LoginTokenPlusDao;
import com.byx.pub.plus.entity.LoginToken;
import com.byx.pub.service.RedisService;
import com.byx.pub.util.MD5;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis操作实现类
 * Created by zyjk on 2020/3/3.
 */
public class RedisServiceImpl implements RedisService {

    @Value("${token.effective.admin}")
    private Long adminSecond;
    @Value("${token.effective.user}")
    private Long userSecond;
    @Resource
    LoginTokenPlusDao tokenPlusDao;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void executePipelinedSet(List<String> keys,String value,long expirationTime) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            keys.forEach((key) -> {
                connection.sAdd(Objects.requireNonNull(serializer.serialize(key)), serializer.serialize(value));
                connection.expire(Objects.requireNonNull(serializer.serialize(key)), expirationTime);
            });
            return null;
        });
    }

    @Override
    public void executePipelinedDel(List<String> keys) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            keys.forEach((key) -> connection.del(serializer.serialize(key)));
            return null;
        });
    }

    @Override
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        if(Objects.isNull(obj)){
            return null;
        }
        return String.valueOf(obj);
    }

    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long del(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return expire(key, time);
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Boolean hSetAll(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    @Override
    public void hSetAll(String key, Map<String, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public void hDel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Long hDecr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long sAdd(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Long lPush(String key, Object value, long time) {
        Long index = redisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
        return index;
    }

    @Override
    public Long lPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public Long lPushAll(String key, Long time, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }


    /**
     * 设置登录token = MD5(来源) + MD5(用户id)
     * 给前端的token并不是完整的
     * 设置到redis的key为：来源+token
     * 给前端的token是没有加上来源的
     * 后端校验是要+来源去校验的
     * @param bean
     * @return
     */
    public String setLoginToken(LoginHeadBean bean){
        String beanStr = JSONObject.toJSONString(bean);
        String tokenKey,resToken,loginId;
        //判断来源
        if(bean.getClientType().equals("manage")){
            loginId = bean.getAdminId();
            resToken = MD5.md5(UUID.randomUUID() + loginId);
            tokenKey = MD5.md5(bean.getClientType()) + resToken;
            this.set(tokenKey,beanStr,adminSecond);
        }else if(bean.getClientType().equals("front")){
            loginId = bean.getUserId();
            resToken = MD5.md5(UUID.randomUUID() + loginId);
            tokenKey = MD5.md5(bean.getClientType()) + resToken;
            this.set(tokenKey,beanStr,adminSecond);
        }else{
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"登录类型错误");
        }
        //清理掉已存在token
        List<LoginToken> list = this.tokenPlusDao.lambdaQuery()
                .eq(LoginToken::getClientType,bean.getClientType())
                .eq(LoginToken::getLoginId, loginId).list();
        if(!CollectionUtils.isEmpty(list)){
            for(LoginToken token : list){
                this.del(token.getTokenStr());
                this.tokenPlusDao.removeById(token.getId());
            }
        }
        //储存最新的token
        this.tokenPlusDao.save(new LoginToken()
                .setLoginId(loginId)
                .setTokenStr(tokenKey)
                .setResStr(resToken)
                .setClientType(bean.getClientType())
        );
        return resToken;
    }

    /**
     * 删除token
     * @param id
     */
    public void delToken(String id){
        List<LoginToken> list = this.tokenPlusDao.lambdaQuery().eq(LoginToken::getLoginId, id).list();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        for(LoginToken token : list){
            this.del(token.getTokenStr());
            this.tokenPlusDao.removeById(token.getId());
        }
    }

    /**
     * 重新设置token
     * @param xToken
     * @param userInfo
     */
    public void reSetToken(String xToken ,UserLoginVo userInfo){
        //重新设置token信息
        LoginHeadBean loginHeadBean = new LoginHeadBean();
        loginHeadBean.setUserId(userInfo.getUserId())
                .setUserRole(userInfo.getRoleType().toString())
                .setClientType(ClientTypeEnum.BYX_FRONT.getValue())
                .setIsBusiness(userInfo.getIsBusiness())
                .setBusinessId(userInfo.getBusinessId())
                .setAdminId(userInfo.getAdminId());
        this.set(xToken, JSONObject.toJSONString(loginHeadBean),userSecond);
    }



}
