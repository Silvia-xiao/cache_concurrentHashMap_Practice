package com.richDataCo.cache.service;

import java.util.concurrent.TimeUnit;


public class DeleteOutdatedData implements Runnable{
    @Override
    public void run() {
        while (true) {
            long now = System.currentTimeMillis();
            try {
                System.out.println("clean thread run");
                CacheService.expire(now);
                TimeUnit.SECONDS.sleep(CacheService.sleepTime);
            } catch (NullPointerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
