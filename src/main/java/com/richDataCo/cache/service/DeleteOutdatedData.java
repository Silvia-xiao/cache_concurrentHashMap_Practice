package com.richDataCo.cache.service;

import java.util.concurrent.TimeUnit;


public class DeleteOutdatedData implements Runnable{
    @Override
    public void run() {
        while (true) {
            System.out.println("clean thread run");
            CacheService.expire();
            try {
                TimeUnit.SECONDS.sleep(CacheService.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
