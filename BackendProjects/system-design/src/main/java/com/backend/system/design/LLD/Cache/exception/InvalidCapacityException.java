package com.backend.system.design.LLD.Cache.exception;

/**
 * Exception thrown when an invalid capacity is specified for the cache.
 */
public class InvalidCapacityException extends CacheException {
    
    public InvalidCapacityException(int capacity) {
        super("Invalid cache capacity: " + capacity + ". Capacity must be positive.");
    }
}


