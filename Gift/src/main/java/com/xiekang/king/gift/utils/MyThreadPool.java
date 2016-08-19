package com.xiekang.king.gift.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by King on 2016/8/19.
 */
public class MyThreadPool {
    public static ExecutorService executorService = Executors.newFixedThreadPool(3);
}
