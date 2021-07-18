package com.coral.kotlin.demo

import com.coral.kotlin.SynchronizedDemo
import java.util.*

class JavaVSKotlinDemo {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = SynchronizedDemo()


            val t1 = Thread(Runnable() {
                for (i in 0..999) {
                    d.test1();
                }
            }, "t1");
            t1.run();

//            Thread.sleep(1000)

            Thread(Runnable() {
                for (i in 0..999) {
                    d.test2();
                }
            }, "t2").run();

            d.test();
        }
    }
}
