package com.richDataCo.cache.service;

import com.richDataCo.cache.entity.Cache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Service
public class CacheService {
    protected static int sleepTime;
    private static Logger LOGGER;
    private static ConcurrentHashMap<String, Cache> cacheObjectMap;
    private static volatile Boolean cleanThreadRun;
    private static ReadWriteLock readWriteLock;
    private static Lock writeLock;
    private static PriorityQueue<Cache> expireQueue;

    public CacheService() {
        LOGGER = LoggerFactory.getLogger(CacheService.class);
        expireQueue = new PriorityQueue<>();
        readWriteLock = new ReentrantReadWriteLock();
        writeLock = readWriteLock.writeLock();
        sleepTime = 20;
        cacheObjectMap = new ConcurrentHashMap<>();
        cleanThreadRun = false;
        startCleanThread();
    }

    /**
     * Delete the expired data.
     */
    static void expire(long now) {
        while(true){
            Cache cache = expireQueue.peek();
            if (cache == null || cache.getExpireTime() > now ) {
                //cache not expired
                break;
            }
            deleteCache(cache.getKey());
            expireQueue.poll();
            System.out.println(" Delete outdated data： " + cache.getKey());
        }

    }

    /**
     * Delete the cache by key.
     */
    public static String deleteCache(String key) {
        writeLock.lock();
        try {
            if (cacheObjectMap.containsKey(key)) {
                cacheObjectMap.remove(key);
                return "Cache with key = " + key + " Deleted";
            } else
                return null;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Hello world - testing connection
     */
    public String hello() {
        return "Hello World";
    }

    /**
     * Get the stored key-value pair data using key.
     */
    public String getData(String key) {
        if (checkCache(key)) {
            //LOGGER.info("Value: "+ cacheObjectMap.values());
            //LOGGER.info("Key: " +cacheObjectMap.keySet());
            return "Cache key: " + key + ", " + cacheObjectMap.get(key).toString();
        } else {
            return "No Valid Data!";
        }
    }

    /**
     * Check if the cache exists or valid
     */
    private boolean checkCache(String key) {
        //Check if the map is empty.
        if (cacheObjectMap.isEmpty()) return false;
        //Check if the key-value pair is existed.
        Cache cache = cacheObjectMap.get(key);
        if (cache == null) {
            return false;
        }
        if(cache.getExpireTime() < System.currentTimeMillis())
            return false;
        //Check if the data is out of date.
        return true;
    }

    /**
     * Store the input key-value pair data
     */
    public void storeData(String data) {
        JSONObject jsonObject;
        writeLock.lock();
        try {
            //Transform the data type and set the value to a cache object.
            jsonObject = new JSONObject(data);
            Cache cacheNew = new Cache(jsonObject.getString("value"));
            String key = jsonObject.getString("key");
            JSONArray keys = jsonObject.names();
            //If the data input does not have a value of a predefined expire time,
            //it will be assigned a default time of 10 seconds.
            if (keys.length() == 2)
                cacheNew.setExpireTime(System.currentTimeMillis()+10000L);
            else{
                cacheNew.setExpireTime(System.currentTimeMillis()+Long.parseLong(jsonObject.getString("expireTime")) * 1000);
                cacheNew.setTtl(Long.parseLong(jsonObject.getString("expireTime")) * 1000);
            }
            Cache cacheOld  = cacheObjectMap.put(key, cacheNew);
            cacheNew.setKey(key);
            expireQueue.add(cacheNew);
            if (cacheOld != null)
                expireQueue.remove(cacheOld);
        } catch (JSONException err) {
            System.out.println(err.toString());
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Start the delete outdated data thread.
     */
    private void startCleanThread() {
        if (!cleanThreadRun) {
            DeleteOutdatedData deleteOutDatedData = new DeleteOutdatedData();
            Thread thread = new Thread(deleteOutDatedData);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
