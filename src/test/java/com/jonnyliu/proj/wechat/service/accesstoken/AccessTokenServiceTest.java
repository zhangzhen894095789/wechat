package com.jonnyliu.proj.wechat.service.accesstoken;

import com.jonnyliu.proj.wechat.bean.AccessTokenBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * User: jonnyliu@tcl.com <br/>
 * Date: on 2016-09-01 9:33.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
public class AccessTokenServiceTest {
    private final Logger logger = LoggerFactory.getLogger(AccessTokenService.class);
    @Autowired
    private AccessTokenService accessTokenService;

    @Test
    public void testGetAccessToken() throws InterruptedException {

        while (true) {
            AccessTokenBean accessToken = accessTokenService.getAccessToken();
            System.out.println(accessToken);

            System.out.println("===================================");
            TimeUnit.SECONDS.sleep(10);
        }
    }

    /**
     * 测试多线程环境下access token的获取
     */
    @Test
    public void testGetAccessTokenInMutilThreadEnvironment() throws InterruptedException {
        logger.info("创建countDownLatch并且设置闭锁要等待的线程数量");
        CountDownLatch latch = new CountDownLatch(100);
        logger.info("创建100个线程并行启动并使线程就绪状态");
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new FetchAccessTokenThread("线程" + (i + 1), accessTokenService, latch));
            thread.start();
        }
        logger.info("创建线程完成："+String.valueOf(latch.getCount()));
        logger.info("主线程调用latch对象的await()方法，自己处于该方法上的等待状态，直到其他已经启动的线程执行完成任务（即count=0)" + new Date().toLocaleString());
        latch.await();
        logger.info("所有线程任务完成后主线程继续执行" + new Date().toLocaleString());
        logger.info(String.valueOf(latch.getCount()));
    }


    @Test
    public void testRefreshAccessToken() {
        AccessTokenBean accessToken = accessTokenService.refreshAccessToken();
        System.out.println(accessToken.getAccess_token());
    }

}
