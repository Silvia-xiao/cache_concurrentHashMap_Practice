package com.richDataCo.cache.entity;


import java.util.Objects;

public class Cache implements Comparable<Cache>{
    private String key;
    private String cacheValue;
    private Long expireTime;
    private Long ttl;

    public Cache(String cacheValue) {
        this.cacheValue = cacheValue;
        ttl = null;
        expireTime = null;
    }

    public Cache(String key, String cacheValue) {
        this.key = key;
        this.cacheValue = cacheValue;
        ttl = null;
        expireTime = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long writeTime) {
        this.ttl = writeTime;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }

    public String getCacheValue() {
        return cacheValue;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cache cache = (Cache) o;
        return cacheValue.equals(cache.cacheValue) &&
                expireTime.equals(cache.expireTime) &&
                ttl.equals(cache.ttl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cacheValue, expireTime, ttl);
    }

    @Override
    public String toString() {
        return "Cache Value: " + cacheValue +
                ", expireTime: " + expireTime;
    }

    @Override
    public int compareTo(Cache cache) {
        long r = this.expireTime - cache.expireTime;
        if (r > 0)  return 1;
        if (r < 0) return -1;
        return 0;
    }
}