package com.richDataCo.cache.service;

import com.richDataCo.cache.entity.Cache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class CacheService {

    private static ConcurrentHashMap<String, Cache> cacheObjectMap;
    private static volatile Boolean cleanThreadRun;
    protected static int sleepTime;
    private int maxCapacity;

    public CacheService() {
        sleepTime = 20;
        if (maxCapacity < 0) {
            throw new IllegalArgumentException("Illegal max capacity: " + maxCapacity);
        }
        this.maxCapacity = maxCapacity;
        cacheObjectMap = new ConcurrentHashMap<>(maxCapacity);
        cleanThreadRun = false;
        startCleanThread();
    }

    public String hello() {
        return "Hello World";
    }

    public static String getData(String key) {
        //System.out.println("Get From Cache！");
//        checkNotNull(key);
//        if (concurrentHashMap.isEmpty()) return null;
//        if (!concurrentHashMap.containsKey(key)) return null;
        if (checkCache(key)) {
            //System.out.println(cacheObjectMap.values());
            //System.out.println(cacheObjectMap.keySet());
            return cacheObjectMap.get(key).toString();
        }
        else{
            System.out.println("No valid data");
            return "No Valid Data!";
        }

    }

    //Test the post url
    public void store(String key) {
        Cache cache = new Cache("qwe");
        cacheObjectMap.put(key, cache);
        System.out.println();
    }

    public static void storeData(String data) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            Cache cache = new Cache(jsonObject.getString ("value"));
            cache.setWriteTime(System.currentTimeMillis());
            JSONArray keys = jsonObject.names ();
            if (keys.length()==2)
                cache.setExpireTime(10000L);
            else
                cache.setExpireTime(Long.parseLong(jsonObject.getString ("expireTime"))*1000);
            cacheObjectMap.put(jsonObject.getString ("key"), cache);
            //System.out.println(cacheObjectMap.keys());
            //System.out.println(cacheObjectMap.values());
        }catch (JSONException err){
            System.out.println(err.toString());
        }
    }

    /**
     * Check if the cache exists or valid
     */
    private static boolean checkCache(String key) {
        Cache cache = cacheObjectMap.get(key);
        if (cache == null) {
            return false;
        }
        return System.currentTimeMillis() - cache.getWriteTime() < cache.getExpireTime();
    }


    /**
     * Start the delete outdated data thread.
     */
    private static void startCleanThread() {
        if (!cleanThreadRun) {
            DeleteOutdatedData deleteOutDatedData = new DeleteOutdatedData();
            Thread thread = new Thread(deleteOutDatedData);
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Delete the expired data
     */
    static void expire() {

        for (Map.Entry<String, Cache> entry : cacheObjectMap.entrySet()) {
            long timoutTime = System.currentTimeMillis() - entry.getValue().getWriteTime();
            if (entry.getValue().getExpireTime() > timoutTime) {
                continue;
            }
            System.out.println(" Delete outdated data： " + entry.getKey());
            deleteCache(entry.getKey());
        }

    }

    /**
     * Delete the cache by key
     */
    public static String deleteCache(String cacheKey) {
        cacheObjectMap.remove(cacheKey);
        return "Cache with key = " + cacheKey + " Deleted";
    }

    /**
     * @param reference
     * @param <T>
     * @return
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
