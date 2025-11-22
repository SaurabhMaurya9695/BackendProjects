package com.backend.system.design.LLD.BloomFilters.hash;

/**
 * Simple hash function implementation using Java's built-in hashCode()
 * Combined with polynomial rolling hash for multiple hash functions
 * 
 * @param <T> the type of elements to hash
 */
public class SimpleHashFunction<T> implements HashFunction<T> {
    
    private static final int[] PRIMES = {
        31, 37, 41, 43, 47, 53, 59, 61, 67, 71,
        73, 79, 83, 89, 97, 101, 103, 107, 109, 113
    };
    
    @Override
    public int hash(T element, int seed, int bitSetSize) {
        // Use different prime numbers for different hash functions
        int prime = PRIMES[seed % PRIMES.length];
        
        // Combine element's hashCode with seed
        int hash = element.hashCode();
        hash = hash ^ (hash >>> 16); // Mix bits
        hash = hash * prime + seed;
        hash = hash ^ (hash >>> 16); // Mix again
        
        // Ensure positive value and within range
        return Math.abs(hash) % bitSetSize;
    }
}

