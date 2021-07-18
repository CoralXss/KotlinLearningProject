package com.coral.kotlin;

public class SynchronizedDemo {

    private int a = 0;

    public void test() {
        System.out.println("test: " + a);
    }

    public void test1() {
        synchronized (this) {  // monitorenter  若是代码片段加 synchoronized 则使用这两个指令包裹临界区代码
            a += 1;
            System.out.println("test1: " + a);
        }                    // monitorexit
    }

    public synchronized void test2() {
        a += 1;
        System.out.println("test2: " + a);
    }

    public static synchronized void test3() {
        System.out.println("Hello");
    }

    public static void main(String[] args) {

    }
}
