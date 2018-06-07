package com.itlgl.whoownfish;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WhoOwnFish {

    public synchronized static void onResult(Person[] answer) {
        long time = System.currentTimeMillis() - timeStart;
        long h = time / 1000 / 3600;
        long m = (time - h * 1000 * 3600) / 1000 / 60;
        long s = (time - h * 1000 * 3600 - m * 1000 * 60) / 1000;
        System.out.println(String.format("耗时:%s小时%s分钟%s秒", h, m, s));
    }

    static long timeStart = System.currentTimeMillis();
    static ExecutorService executorService;
    public static final int RANGE_START = 0;
    public static final int RANGE_END = 120;

    public static void main(String[] args) throws Exception {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("机器最大支持 " + availableProcessors + " 线程并发");
        int frame = (RANGE_END - RANGE_START) / availableProcessors;
        if (frame == 0) {
            frame = 1;
        }
        int index = RANGE_START;
        executorService = Executors.newFixedThreadPool(availableProcessors);

        while (true) {
            int start = index;
            int end = index + frame;
            if (end > RANGE_END) {
                end = RANGE_END;
            }
            if (index >= RANGE_END) {
                break;
            }
            index = end;
            executorService.execute(new CalculationTask(start, end));
        }
    }
}
