package io.rackshift.metal.sdk;

import java.util.Random;
import java.util.concurrent.*;

public class MetalPluginException extends RuntimeException {

    private static final long serialVersionUID = -7430603197031343440L;

    public MetalPluginException() {
        super();
    }

    public MetalPluginException(String message, Throwable cause,
                                boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MetalPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetalPluginException(String message) {
        super(message);
    }

    public MetalPluginException(Throwable cause) {
        super(cause);
    }

    public static void throwException(String message) {
        throw new MetalPluginException(message);
    }

    public static MetalPluginException getException(String message) {
        throw new MetalPluginException(message);
    }

    public static void throwException(Throwable t) {
        throw new MetalPluginException(t);
    }

    public static void main(String[] args) {
//        long s = System.currentTimeMillis();
//        CountDownLatch c = new CountDownLatch(2);
//        MyThread t1 = new MyThread(c);
//        MyThread t2 = new MyThread(c);
//
//        FutureTask<Integer> task1 = new FutureTask(t1);
//        FutureTask<Integer> task2 = new FutureTask(t2);
//
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        executorService.submit(task1);
//        executorService.submit(task2);
//        try {
//            c.await();
//            System.out.println("总共耗时：" + (task1.get() + task2.get()));
//            System.out.println("总共耗时：" + (System.currentTimeMillis() - s) / 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        executorService.shutdown();

        System.out.println("[\n" +
                "    {\n" +
                "        \"label\": \"起始IP\",\n" +
                "        \"inputType\": \"text\",\n" +
                "        \"id\": \"startIp\",\n" +
                "        \"show\": \"true\",\n" +
                "        \"description\": \"带外网段的起始ip\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"label\": \"结束IP\",\n" +
                "        \"inputType\": \"text\",\n" +
                "        \"id\": \"endIp\",\n" +
                "        \"show\": \"true\",\n" +
                "        \"description\": \"带外网段的结束ip\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"label\": \"子网掩码\",\n" +
                "        \"inputType\": \"text\",\n" +
                "        \"id\": \"mask\",\n" +
                "        \"show\": \"true\",\n" +
                "        \"description\": \"带外网段的子网掩码\"\n" +
                "    },\n" +
                "{\n" +
                "        \"label\": \"是否排除IP\",\n" +
                "        \"inputType\": \"boolean\",\n" +
                "        \"show\": \"true\",\n" +
                "        \"id\": \"excludeIpRadio\",\n" +
                "        \"description\": \"多个ip用英文逗号“,”隔开\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"label\": \"排除IP\",\n" +
                "        \"inputType\": \"textarea\",\n" +
                "        \"show\": \"excludeIpRadio\",\n" +
                "        \"id\": \"excludeIps\",\n" +
                "        \"description\": \"多个ip用英文逗号“,”隔开\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"label\": \"带外用户名\",\n" +
                "        \"inputType\": \"text\",\n" +
                "        \"id\": \"userName\",\n" +
                "        \"show\": \"true\",\n" +
                "        \"defaultValue\": \"root\",\n" +
                "        \"description\": \"该带外网段物理机对应的通用用户名\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"label\": \"带外密码\",\n" +
                "        \"inputType\": \"password\",\n" +
                "        \"id\": \"password\",\n" +
                "        \"show\": \"true\",\n" +
                "        \"defaultValue\": \"calvin\",\n" +
                "        \"description\": \"该带外网段物理机对应的通用密码\"\n" +
                "    }\n" +
                "    \n" +
                "]");
    }

    static class MyThread implements Callable<Integer> {
        CountDownLatch latch;

        public MyThread(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public Integer call() {
            long s = new Random().nextInt(10) * 1000;
            try {
                System.out.println(Thread.currentThread().getName() + "执行前:" + latch.getCount());
                Thread.sleep(s);
                System.out.println(Thread.currentThread().getName() + "工作" + s / 1000 + "秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + "执行前后:" + latch.getCount());

            return (int) s / 1000;
        }
    }
}
