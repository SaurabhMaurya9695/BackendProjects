package com.backend.system.design.LLD.Cache.exception;

/**
 * Base exception for all cache-related exceptions.
 */
public class CacheException extends RuntimeException {
    
    public CacheException(String message) {
        super(message);
    }
    
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}

