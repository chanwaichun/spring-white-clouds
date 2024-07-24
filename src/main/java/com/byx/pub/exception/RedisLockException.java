package com.byx.pub.exception;

/**
 * redis分布式锁异常
 * @author cyj
 */
public class RedisLockException extends RuntimeException{

    public RedisLockException(String message) {
        super(message);
    }
}
