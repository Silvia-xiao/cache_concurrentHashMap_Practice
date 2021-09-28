package com.richDataCo.cache;

import com.richDataCo.cache.service.CacheService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@SpringBootTest
class CacheApplicationTests {

	@Test
	void contextLoads() {
		try {
			String key = "1";
			//No predefined expire time
			System.out.println("***********No predefined Expire Time**********");
			String data = "{'key':"+ key + ",'value':" + "test" + "}";
			CacheService.storeData(data);
			System.out.println("key:" + key + ", value:" + CacheService.getData(key));
			System.out.println("key:" + key + ", value:" + CacheService.deleteCache(key));
			System.out.println("key:" + key + ", value:" + CacheService.getData(key));

			//Predefined expire time
			data = "{'key':"+ key + ",'value':" + "test" + ",'expireTime':" + "20" + "}";
			System.out.println("\n***********With Predefined expire time**********");
			CacheService.storeData(data);
			System.out.println("key:" + key + ", value:" + CacheService.getData(key));
			Thread.sleep(20000);
			System.out.println("key:" + key + ", value:" + CacheService.getData(key));

			//****************并发性能测试************
			System.out.println("\n***********Concurrent test************");
			//Create thread pool with 10 threads, and test for 1000000 times store actions.
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			Future[] futures = new Future[10];
			long start = System.currentTimeMillis();
			for (int j = 0; j < 10; j++) {
				futures[j] = executorService.submit(() -> {
					for (int i = 0; i < 100000; i++) {
						String str = "{'key':"+ Thread.currentThread().getId() + key + i + ",'value':" + i + ",'expireTime':" + 300000 + "}";
						CacheService.storeData(str);
					}
				});
			}
			//等待全部线程执行完成，打印执行时间
			for (Future future : futures) {
				future.get();
			}
			System.out.printf("Added time spent：%dms\n", System.currentTimeMillis() - start);

			//********查询********
			start = System.currentTimeMillis();
			for (int j = 0; j < 10; j++) {
				futures[j] = executorService.submit(() -> {
					for (int i = 0; i < 100000; i++) {
						CacheService.getData(Thread.currentThread().getId() + key + i);
					}
				});
			}
			//等待全部线程执行完成，打印执行时间
			for (Future future : futures) {
				future.get();
			}
			System.out.printf("Select time spent：%dms\n", System.currentTimeMillis() - start);
			//System.out.println("当前缓存容量：" + CacheService.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
