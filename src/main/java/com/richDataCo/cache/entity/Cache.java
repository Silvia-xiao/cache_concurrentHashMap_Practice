package com.richDataCo.cache.entity;


import java.util.Objects;

public class Cache {
    //private Map<String, Object> cache = new ConcurrentHashMap<>();
    //private String key;
    private String cacheValue;
    private Long expireTime;
    private Long writeTime;

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(Long writeTime) {
        this.writeTime = writeTime;
    }

    public Cache(String cacheValue) {
        //this.key = key;
        this.cacheValue = cacheValue;
        writeTime = null;
        expireTime = null;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }

    Object getCacheValue() {
        return cacheValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cache cache = (Cache) o;
        return cacheValue.equals(cache.cacheValue) &&
                expireTime.equals(cache.expireTime) &&
                writeTime.equals(cache.writeTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cacheValue, expireTime, writeTime);
    }

    @Override
    public String toString() {
        return "Cache{" +
                "cacheValue='" + cacheValue + '\'' +
                ", expireTime=" + expireTime +
                ", writeTime=" + writeTime +
                '}';
    }
}