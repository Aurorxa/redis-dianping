package com.github.redis.utils;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-08 14:47:32
 */
public class BloomFilterHelper<T> {

    private int numHashFunctions;

    private int bitSize;

    private Funnel<T> funnel;

    public BloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
        Preconditions.checkArgument(funnel != null, "funnel不能为空");
        this.funnel = funnel;
        // 计算bit数组长度
        this.bitSize = this.optimalNumOfBits(expectedInsertions, fpp);
        // 计算hash方法执行次数
        this.numHashFunctions = this.optimalNumOfHashFunctions(expectedInsertions, this.bitSize);
    }

    public int[] murmurHashOffset(T value) {
        int[] offset = new int[this.numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, this.funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= this.numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % this.bitSize;
        }

        return offset;
    }

    /**
     * 计算bit数组长度
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            // 设定最小期望长度
            p = Double.MIN_VALUE;
        }
        int sizeOfBitArray = (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
        return sizeOfBitArray;
    }

    /**
     * 计算hash方法执行次数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        int countOfHash = Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
        return countOfHash;
    }
}