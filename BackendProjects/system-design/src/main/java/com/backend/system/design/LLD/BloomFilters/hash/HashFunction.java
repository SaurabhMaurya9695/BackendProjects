package com.backend.system.design.LLD.BloomFilters.hash;

/**
 * Interface for hash functions used in Bloom Filter
 *
 * @param <T> the type of elements to hash
 */
public interface HashFunction<T> {

    /**
     * Generates a hash value for the given element
     *
     * @param element    The element to hash
     * @param seed       The seed value for generating different hash values
     * @param bitSetSize The size of the bit set (for modulo operation)
     * @return Hash value in the range [0, bitSetSize)
     */
    int hash(T element, int seed, int bitSetSize);
}

