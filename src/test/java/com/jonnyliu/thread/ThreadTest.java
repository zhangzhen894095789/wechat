package com.jonnyliu.thread;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class ThreadTest {
    @Test
    public  void test() {
        CountDownLatch latch = new CountDownLatch(10000);
        for (int i = 0;i < 10000 ;i++ ){
            MyThread myThread = new MyThread(latch);
            new Thread(myThread).start();
        }
        try {
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    class MyThread implements Runnable{
        CountDownLatch latch;
        public MyThread(CountDownLatch latch){
            this.latch =latch;
        }
        @Override
        public void run() {
            System.out.println("thread name ：" + Thread.currentThread().getName() + "start time" + new Date().toLocaleString());
            latch.countDown();
        }
    }
}
