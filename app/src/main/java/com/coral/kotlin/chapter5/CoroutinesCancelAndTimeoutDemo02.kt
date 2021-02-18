package com.coral.kotlin.chapter5

import kotlinx.coroutines.*

/**
 * 二、取消与超时
 *
 * 1. 取消协程的执行
 *
 * 2. 取消是协作的
 *
 * 3. 使计算代码可取消
 *
 * 4. 在 finally 中释放资源
 *
 * 5. 运行不能取消的代码块
 *
 * 6. 超时
 *
 * 7. 异步超时与资源
 */
class CoroutinesCancelAndTimeoutDemo02 {

    /**
     * 1. 取消协程的执行
     * 场景：页面关闭时，其中启动的协程应该也要被取消。
     *
     * - 使用 launch() 返回的 job ，调用 job.cancel() 取消运行中的协程。
     * - 也可以使用 job.cancelAndJoin() 合并对 cancel 与 join 的调用。
     */
    fun demo0() = runBlocking {
        val job = launch {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        }

        delay(1300L)   // 延迟一段时间执行以下代码
        println("main: I'm tried of waiting!")
        job.cancel()  // 取消该作业
        job.join()    // 等待作业执行结束
        println("main: Now I can quit.")

        /**
         * 打印结果：
         * job: I'm sleeping 0 ...
         * job: I'm sleeping 1 ...
         * job: I'm sleeping 2 ...
         * main: I'm tried of waiting!
         * main: Now I can quit.
         */
    }

    /**
     * 2. 取消是协作的
     * - 协程的取消 是 协作 的。一段协程代码必须协作才能取消。
     * - 所有 kotlinx.corountines 中的挂起函数 都是 可被取消的。它们检测协程的取消，并在取消时抛出 CancellationException.
     * 若协程正在执行计算任务，并且没有检查取消的话，则它是不能被取消的。
     *
     * 3. 使计算代码可取消。
     * demo1 中的代码，在取消后，计算代码依旧执行了 2次 sleeping，一共 5 次才真正结束。以下使用两种方式，让计算代码可被取消：
     * 1）定期调用 挂起函数 来检测取消。使用 yield ;  demo3()
     * 2）显式检查取消状态。 demo2()
     */
    fun demo1() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {     // 一个执行计算的循环，只是为了占有 CPU
                if (System.currentTimeMillis() >= nextPrintTime) {  // 每秒打印消息两次
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L)
        println("main: I'm tried of waiting !")
        job.cancelAndJoin()    // 取消一个作业并且等待它结束
        println("main: Now I can quit.")

        /**
         * 打印结果：
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tried of waiting !
        job: I'm sleeping 3 ...
        job: I'm sleeping 4 ...
        main: Now I can quit.
         */
    }

    // 方式一：使计算代码可取消
    fun demo2() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (isActive) {     // 检测取消状态，取消计时循环 [ isActive 是 CoroutineScope 中的扩展属性. ]
                if (System.currentTimeMillis() >= nextPrintTime) {  // 每秒打印消息两次
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L)
        println("main: I'm tried of waiting !")
        job.cancelAndJoin()    // 取消一个作业并且等待它结束
        println("main: Now I can quit.")

        /**
         * 打印结果：
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tried of waiting !
        main: Now I can quit.
         */
    }

    /**
     * 4. 在 finally 中释放资源
     * - 当在被取消时抛出 CancellationException 的可被取消的 挂起函数，可使用 try...finally 终结其动作。
     */
    fun demo3() = runBlocking {
        println("-------------------")
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                println("job: I'm running finally")
            }
        }
        delay(1300L)
        println("main: I'm tried of waiting !")
        job.cancelAndJoin()    // 取消一个作业并且等待它结束
        println("main: Now I can quit.")

        /**
         * 打印结果：
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tried of waiting !
        job: I'm running finally
        main: Now I can quit.
         */
    }

    /**
     * 5. 运行不能取消的代码块
     * 背景：尝试在 finally 块中调用 挂起函数 的行为 都会抛出 CancellationException 。一般情况下，不会出现这种调用。
     * 但当你需要挂起一个被取消的协程，可以将相应的代码 包装在 withContext(NonCancellable) {...} 中。
     */
    fun demo4() = runBlocking {
        println("-------------------")
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    println("job: I'm running finally")
                    delay(1000L)
                    println("job: And I've just delayed for 1s because I'm non-cancelable")
                }
            }
        }
        delay(1300L)
        println("main: I'm tried of waiting !")
        job.cancelAndJoin()    // 取消一个作业并且等待它结束
        println("main: Now I can quit.")

        /**
         * 打印结果：
        job: I'm sleeping 0 ...
        job: I'm sleeping 1 ...
        job: I'm sleeping 2 ...
        main: I'm tried of waiting !
        job: I'm running finally
        job: And I've just delayed for 1s because I'm non-cancelable
        main: Now I can quit.
         */
    }

    /**
     * 6. 超时
     * withTimeOut  /   withTimeoutOrNull
     */
    fun demo5() = runBlocking {
        val result = withTimeoutOrNull(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
            "DONE"
        }
        println("Result is $result")
        /**
         * 打印结果：
        I'm sleeping 0 ...
        I'm sleeping 1 ...
        I'm sleeping 2 ...
        Result is null
         */

        withTimeout(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }

        /**
         * 打印结果：
        I'm sleeping 0 ...
        I'm sleeping 1 ...
        I'm sleeping 2 ...
        Exception in thread "main" kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
         */
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val d = CoroutinesCancelAndTimeoutDemo02()

//            d.demo0()
//            d.demo1()
//            d.demo2()
//            d.demo3()

//            d.demo4()

            d.demo5()
        }
    }

}