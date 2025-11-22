package com.backend.system.design.LLD.BloomFilters.hash;

/**
 * Implementation of MurmurHash-based hash function for Bloom Filter
 * Uses double hashing technique: hash(i) = hash1 + i * hash2
 *
 * @param <T> the type of elements to hash
 */
public class MurmurHashFunction<T> implements HashFunction<T> {

    private static final int SEED_1 = 0x9747b28c;
    private static final int SEED_2 = 0x7fb5d329;

    @Override
    public int hash(T element, int seed, int bitSetSize) {
        String str = element.toString();

        // Generate two different hash values
        int hash1 = murmur3Hash32(str, SEED_1 + seed);
        int hash2 = murmur3Hash32(str, SEED_2 + seed);

        // Combine using double hashing
        int combinedHash = Math.abs(hash1 + seed * hash2);

        return combinedHash % bitSetSize;
    }

    /**
     * MurmurHash3 32-bit implementation
     *
     * @param data The string to hash
     * @param seed The seed value
     * @return 32-bit hash value
     */
    private int murmur3Hash32(String data, int seed) {
        byte[] bytes = data.getBytes();
        return murmur3Hash32(bytes, seed);
    }

    /**
     * MurmurHash3 32-bit implementation for byte array
     *
     * @param data The byte array to hash
     * @param seed The seed value
     * @return 32-bit hash value
     */
    private int murmur3Hash32(byte[] data, int seed) {
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;
        final int r1 = 15;
        final int r2 = 13;
        final int m = 5;
        final int n = 0xe6546b64;

        int hash = seed;
        int length = data.length;
        int roundedEnd = (length & 0xfffffffc); // Round down to 4 byte block

        // Process 4-byte blocks
        for (int i = 0; i < roundedEnd; i += 4) {
            int k = (data[i] & 0xff) | ((data[i + 1] & 0xff) << 8) | ((data[i + 2] & 0xff) << 16) | (data[i + 3] << 24);

            k *= c1;
            k = Integer.rotateLeft(k, r1);
            k *= c2;

            hash ^= k;
            hash = Integer.rotateLeft(hash, r2);
            hash = hash * m + n;
        }

        // Process remaining bytes
        int k1 = 0;
        switch (length & 0x03) {
            case 3:
                k1 ^= (data[roundedEnd + 2] & 0xff) << 16;
            case 2:
                k1 ^= (data[roundedEnd + 1] & 0xff) << 8;
            case 1:
                k1 ^= (data[roundedEnd] & 0xff);
                k1 *= c1;
                k1 = Integer.rotateLeft(k1, r1);
                k1 *= c2;
                hash ^= k1;
        }

        // Finalization
        hash ^= length;
        hash ^= (hash >>> 16);
        hash *= 0x85ebca6b;
        hash ^= (hash >>> 13);
        hash *= 0xc2b2ae35;
        hash ^= (hash >>> 16);

        return hash;
    }
}

