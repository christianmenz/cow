package ch.christianmenz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static List<Integer> list = new CopyOnWriteArrayList<>();


    public static void main(String[] args) throws Exception {

        List<Integer> tmp = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            tmp.add(i);
        }

        list.addAll(tmp);

        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 500; i++) {
            executorService.execute(() -> {
//                synchronized (list) {
                    for (int j : list) {
//                        System.out.println(j);
                    }
//                }
                Thread.yield();
            });
        }

        executorService.execute(() -> {
            for (int i = 0; i < 10; i++) {
//                synchronized (list) {
                    list.add((int) (Math.random() * 1000));
//                }
                Thread.yield();
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);

        System.out.format("duration %d\n", System.currentTimeMillis() - start);
    }
}
