package com.coral.kotlin.chapter5

import kotlinx.coroutines.*

/**
 * 一、协程基础
 *
 * - Kotlin 仅在标准库中提供 最底层 API 以便其他各种库能利用协程。
 * - 同其它语言不同，async 与 await 不是 kotlin 的关键字，也不是标准库的一部分。
 * - Kotlin 的「挂起函数」概念，为异步操作 提供了比 future 与 promise 更安全、更不易出错的抽象。
 *
 * - kotlinx.coroutines 是由 JetBrains 开发的功能丰富的协程库，
 * 包含很多启用高级协程的原语，如 launch、async 等。
 *
 *
 * Tips:
 * - 在 GlobalScope 中启动的活动协程，并不会使进程保活。它们就像守护线程。
 * - 将协程中运行的代码提取到一个独立的函数中，需要用 `suspend` 修饰新的函数，表示 挂起函数。
 */
class CoroutinesDemo01 {

    /**
     * 1. 第一个协程程序
     *
     * 协程：本质上，是轻量级的线程。
     * - 在某些 CoroutineScope 上下文中与 launch 协程构建一起启动。
     * - GlobalScope 中启动，表示协程的生命周期只受整个应用程序的生命周期限制。
     *
     * suspend fun delay()
     * - 挂起函数，不会造成线程阻塞，但是会挂起协程，并且只能在 协程 中使用。
     *
     */
    fun demo0() {
        GlobalScope.launch {        // 在后台启动一个新的协程并继续
            delay(1000L)  // 非阻塞线程等待一秒
            println("World !")      // 延迟后打印输出
        }
        println("Hello, ")          // 协程在等待时，主线程还在继续
        Thread.sleep(2000L)   // 阻塞主线程 2s 保证 JVM 存活 （关键，不然等协程唤醒后，程序就结束了。）

        /**
         * 打印结果：
         * Hello,
         * World !
         */
    }

    /**
     * 2. 桥接的阻塞与非阻塞的世界
     * demo0() 中使用了非阻塞的 delay() 和 阻塞的 Thread.sleep() ，以下使用 runBlocking 协程构建器来阻塞：
     *
     * - runBlocking 的主线程会一直 *阻塞* 直到其内部的协程执行完毕。
     */
    fun demo1() {
        GlobalScope.launch {        // 在后台启动一个新的协程并继续
            delay(1000L)  // 非阻塞线程等待一秒
            println("World !")      // 延迟后打印输出
        }
        println("Hello, ")          // 协程在等待时，主线程还在继续
//        Thread.sleep(2000L)

        runBlocking {
            delay(2000L)  // 阻塞主线程 2s 保证 JVM 存活 （关键，不然等协程唤醒后，程序就结束了。）
        }

        /**
         * 打印结果：
         * Hello,
         * World !
         */
    }

    // 上述 demo1 改下为：
    fun demo2() = runBlocking<Unit> {   // 启动顶层主线程的适配器
        GlobalScope.launch {
            delay(1000L)
            println("World !")
        }

        println("Hello, ")
        delay(2000L)
    }

    /**
     * 3. 等待一个作业 job
     * - 使用 job 显式等待所启动的后台 job 执行结束后，再执行自己的任务
     */
    fun demo3() = runBlocking  {  // 将 demo3 方法变成一个协程
        val job = GlobalScope.launch {
            delay(1000L)
            println("world !")
        }
        println("3. Hello, ")
        job.join()    // 等到子协程执行结束（注：必须在 suspend 或者 协程中调用 suspend 方法）
    }


    /**
     * 5. 协程作用域
     * - GlobalScope.launch 全局协程，整个生命周期内，必须手动挂起，才能避免内存占用
     * - runBlocking 作用域为所在代码块，启动协程后，无需显式调用 join 。
     * - coroutineScope 构建器声明自己的作用域。等待其 协程体 以及所有子协程结束。
     *
     * runBlocking 与 coroutineScope 的区别：
     * - 前者会 阻塞 当前线程来等待；后者 只是挂起，会释放底层线程用于其他用途；
     * - 前者是 常规函数，后者是 suspend 挂起函数，只能在协程中调用。
     *
     * 打印结果：
     * Task from coroutine scope
     * Task from runBlocking
     * Task from nested launch
     * Coroutine scope is over
     */
    fun demo4() = runBlocking {
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope {               // 等待其 协程体 以及所有子协程结束再执行。
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope")
        }

        println("Coroutine scope is over")
    }

    /**
     * 6. 协程很轻量
     * - 启动 10 万个协程，不会出现内存不足问题；启动线程，则可能会出现。
     */
    fun demo5() = runBlocking {
        repeat(100_000) { // 启动大量的协程
            launch {
                delay(5000L)
                println(".")
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = CoroutinesDemo01()
            d.demo0()
            d.demo1()
            d.demo2()
            d.demo3()
            d.demo4()
            d.demo5()
        }
    }
}