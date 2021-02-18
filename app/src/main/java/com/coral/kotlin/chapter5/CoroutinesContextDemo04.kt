package com.coral.kotlin.chapter5

import kotlinx.coroutines.*

/**
 * 四、协程上下文与调度器
 *
 * 1. 调度器与线程
 * 2. 非受限调度器 vs 受限调度器
 * 3. 调试协程与线程(调试)
 * 4. 在不同线程间跳转(调试)
 * 5. 上下文中的作业
 * 6. 子协程
 * 7. 父协程的职责
 * 8. 命名协程以用于调试
 * 9. 组合上下文中的元素
 * 10. 协程作用域
 * 11. 线程局部数据
 *
 */
class CoroutinesContextDemo04 {

    /**
     * 1. 调度器与线程
     *
     * - 协程上下文 包含 一个 协程调度器，它确定了 相关的协程 在哪个线程或哪些线程上执行。
     * - 协程调度器 可以将 协程 限制在一个特定的线程执行，或将其分派到一个线程池，或让它不受限地运行。
     *
     * - 所有 协程构建器，如 launch 和 async 接收一个可选的 CoroutineContext 参数，可显式为一个新协程 或 其它上下文元素 指定一个 调度器。
     *
     * - launch{} 不传参数时，从启动它的 CoroutineScope 承袭上下文（及调度器）。
     * - Dispatchers.Unconfined 调度器，也似乎运行在 main 线程中，但实际上是不同的机制。
     * - Dispatchers.Default 调度器，GlobalScope 就是使用的该默认调度器，使用共享的后台线程池。
     * - newSingleThreadContext 为协程的运行启动了一个线程。不需要事，使用 close 函数 或 存在在一个 顶层变量 中在整个应用程序可被调用。
     */
    fun demo1() = runBlocking {
        launch {                           // 运行在父协程的上下文中
            println("main runBlocking  : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Unconfined) {  // 不受限的，将工作在主线程中
            println("Unconfined        : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Default) {     // 将会获取默认调度器
            println("Default           : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(newSingleThreadContext("MyOwnThread") ) { // 将获得一个新的线程
            println("newSingleThreadCtx: I'm working in thread ${Thread.currentThread().name}")
        }

        /**
         * 打印结果：
        Unconfined        : I'm working in thread main
        Default           : I'm working in thread DefaultDispatcher-worker-1
        newSingleThreadCtx: I'm working in thread MyOwnThread
        main runBlocking  : I'm working in thread main
         */
    }

    /**
     * 2. 非受限调度器 vs 受限调度器
     *
     * 非受限调度器：Dispatchers.Unconfined 协程调度器
     * - 在调用它的线程，启动一个协程，但它仅仅只是运行到第一个挂起点。挂起后，它恢复线程中的协程，而这完全由被调用的挂起函数决定。
     * - 非常适合 执行不消耗 CPU 时间的任务，以及 不更新局限于特定线程的任何共享数据（如 UI）的线程。
     * - 默认继承了 外部的 CoroutineScope .
     *
     * Tips:
     * - 协程可以在一个线程上挂起，并在其他线程上个恢复。
     */
    fun demo2() = runBlocking {
        launch(Dispatchers.Unconfined) {   // 非受限的 将和 主线程 一起工作
            println("Unconfined        : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined        : After delay in thread ${Thread.currentThread().name}")
            // 问题：上述打印为啥会是子线程？该协程的上下文继承自 runBlocking{} 协程，并在 main 线程中运行；当 delay 函数 调用时，非受限的那个协程 在默认的执行者线程中恢复执行。
        }

        launch {     // 父协程的上下文，主 runBlocking 协程
            println("main runBlocking  : I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("main runBlocking  : After delay in thread ${Thread.currentThread().name}")
        }

        /**
         * 打印结果：
        Unconfined        : I'm working in thread main
        main runBlocking  : I'm working in thread main
        Unconfined        : After delay in thread kotlinx.coroutines.DefaultExecutor
        main runBlocking  : After delay in thread main
         */
    }

    /**
     * 3. 调试协程与线程
     * 4. 在不同线程间跳转
     */
    fun demo3() {
       // log() 函数
    }

    /**
     * 5. 上下文中的作业
     *
     * - 协程的 Job 是上下文的一部分，可以使用 表达式获取。
     *
     */
    fun demo5() = runBlocking {
        println("My job is ${coroutineContext[Job]}") // 获取 job
    }

    /**
     * 6. 子协程
     *
     * - 当一个协程 被 其它协程在 CoroutineScope 中启动时，将通过 CoroutineScope.coroutineContext 来承袭上下文，
     * 并且 这个新协程的 Job 将会成为 父协程作业的 子作业。
     * - 当一个父协程 被取消时，所有它的子协程也会被 递归取消。
     * - 当使用 GlobalScope 启动一个协程时，则新协程的 Job 没有 父 Job。
     */
    fun demo6() = runBlocking {
        val request = launch {
            GlobalScope.launch {
                println("job1: I run in GlobalScope and 独立运行！")
                delay(1000)
                println("job1: 我不受 上层 request job 取消状态影响")
            }

            launch {
                delay(100)
                println("job2: 我是 request 协程的 子协程")
                delay(1000)
                println("job2: 如果我的父协程取消了，我将不会执行这行代码")
            }
        }
        delay(500)
        request.cancel()
        delay(1000)
        println("main: Who has survived request cancellation?")

        /**
         * 打印结果：
        job1: I run in GlobalScope and 独立运行！
        job2: 我是 request 协程的 子协程
        job1: 我不受 上层 request job 取消状态影响
        main: Who has survived request cancellation?
         */
    }

    /**
     * 7. 父协程的职责
     *
     * - 一个 父协程 总是等待所有的子协程执行结束。
     * - 父协程不必使用 Job.join 在最后的时候等待它们。
     */
    fun demo7() = runBlocking {
        val request = launch {
            repeat(3) { i ->
                launch {     // 启动少量的子作业
                    delay((i + 1) * 200L)
                    println("Coroutine $i is done")
                }
            }
            println("request: 我执行完了，显式调用 join 等待尚活跃的子线程执行完毕")
        }
        request.join()      // 等待请求完成，包括所有子线程
        println("Now processing of the request is complete")

        /**
         * 运行结果：
        request: 我执行完了，显式调用 join 等待尚活跃的子线程执行完毕
        Coroutine 0 is done
        Coroutine 1 is done
        Coroutine 2 is done
        Now processing of the request is complete
         */
    }

    /**
     * 9. 组合上下文中的元素
     *
     * - 若需要在协程上下文中定义多个元素，则使用 + 操作符实现。
     */
    fun demo9() = runBlocking {
        // 显式指定一个调度器启动协程 并且 指定一个命名
        launch(Dispatchers.Default + CoroutineName("test")) {
            println("I'm working in thread ${Thread.currentThread().name}")
            // 打印结果：I'm working in thread DefaultDispatcher-worker-1
        }


    }

    /**
     * 10. 协程作用域
     * 场景：在 Activity 中这类具有生命周期的对象中，使用协程。如何管理协程的生命周期，使其与 Activity 的生命周期关联？
     */
    fun demo10() {
        val mainScope = MainScope()

        mainScope.launch {  }

        mainScope.cancel()
    }

    /**
     * 11. 线程局部数据
     *
     * - 协程之间，共享 线程局部数据。但是要注意挂起恢复后更新的值会丢失。
     */
    fun demo11() = runBlocking {
        val threadLocal = ThreadLocal<String>()
        threadLocal.set("main")
        println("Pre-main, currentThread: ${Thread.currentThread()}, threadLocalValue: ${threadLocal.get()}")

        val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {  // Dispatchers.Default 新起一个线程，并设置本地局部变量为 launch
            println("Launch start, currentThread: ${Thread.currentThread()}, threadLocalValue: ${threadLocal.get()}")
            yield()
            println("After yield, currentThread: ${Thread.currentThread()}, threadLocalValue: ${threadLocal.get()}")  // 这里，当线程局部变量变化时，新值不会传播给线程调用者。
        }
        job.join()
        println("Post-main, currentThread: ${Thread.currentThread()}, threadLocalValue: ${threadLocal.get()}")

        /**
         * 打印结果：
        Pre-main, currentThread: Thread[main,5,main], threadLocalValue: main
        Launch start, currentThread: Thread[DefaultDispatcher-worker-1,5,main], threadLocalValue: launch
        After yield, currentThread: Thread[DefaultDispatcher-worker-1,5,main], threadLocalValue: launch
        Post-main, currentThread: Thread[main,5,main], threadLocalValue: main
         */
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val d = CoroutinesContextDemo04()

            d.demo1()
            println("----------")
            d.demo2()
            println("----------")
            d.demo3()

            println("5.----------")
            d.demo5()
            println("6.----------")
            d.demo6()
            println("7.----------")
            d.demo7()

            println("9.----------")
            d.demo9()

            println("11.----------")
            d.demo11()
        }
    }
}