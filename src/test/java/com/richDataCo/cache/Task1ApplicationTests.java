package com.richDataCo.cache;

import com.richDataCo.cache.service.CacheService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@SpringBootTest
class CacheApplicationTests {

    private final CacheService cacheService;

    @Autowired
    public CacheApplicationTests(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Test
    void contextLoads() {
        try {
            String key = "1";
            //No predefined expire time
            System.out.println("***********No predefined Expire Time**********");
            String data = "{'key':" + key + ",'value':" + "test" + "}";
            cacheService.storeData(data);
            System.out.println(cacheService.getData(key));
            Thread.sleep(10000);
            System.out.println(cacheService.getData(key));

            //Predefined expire time
            data = "{'key':" + key + ",'value':" + "test" + ",'expireTime':" + "20" + "}";
            System.out.println("\n***********With Predefined expire time**********");
            cacheService.storeData(data);
            System.out.println(cacheService.getData(key));
            Thread.sleep(15000);
            System.out.println(cacheService.getData(key));

            //Concurrent tests
            System.out.println("\n***********Concurrent test************");
            //Create thread pool with 10 threads, and test for 10000000 times store actions.
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            Future[] futures = new Future[10];
            long start = System.currentTimeMillis();
            for (int j = 0; j < 10; j++) {
                futures[j] = executorService.submit(() -> {
                    for (int i = 0; i < 100000; i++) {
                        String str = "{'key':" + Thread.currentThread().getId() + key + i + ",'value':" + i + ",'expireTime':" + 300000 + "}";
                        cacheService.storeData(str);
                    }
                });
            }
            //Print the execution time after all the threads have finished
            for (Future future : futures) {
                future.get();
            }
            System.out.printf("Added time spent：%dms\n", System.currentTimeMillis() - start);

            //Select
            start = System.currentTimeMillis();
            for (int j = 0; j < 10; j++) {
                futures[j] = executorService.submit(() -> {
                    for (int i = 0; i < 100000; i++) {
                        cacheService.getData(Thread.currentThread().getId() + key + i);
                    }
                });
            }
            //Print the execution time after all the threads have finished
            for (Future future : futures) {
                future.get();
            }
            System.out.printf("Select time spent：%dms\n", System.currentTimeMillis() - start);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
