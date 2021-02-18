package com.coral.kotlin.chapter5

import kotlinx.coroutines.*
import java.lang.ArithmeticException
import kotlin.system.measureTimeMillis

/**
 * 三、组合挂起函数
 *
 * 1. 默认顺序调用
 * 2. 使用 async 并发
 * 3. 惰性启动 async
 * 4. async 风格的函数
 * 5. 使用 async 的结构化并发
 */
class CoroutinesSuspendDemo03 {

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L)
        return 13
    }
    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L)
        return 29
    }

    /**
     * 1. 默认顺序调用
     * - 两个 挂起函数，调用 顺序，同常规代码一致，顺序为默认的。
     */
    fun demo1() = runBlocking {
        val time = measureTimeMillis {      // 计算两个挂起函数所需要的总时间
            val one = doSomethingUsefulOne()
            val two = doSomethingUsefulTwo()
            println("The answer is ${one + two}")
        }
        println("Completed in $time ms")

        /**
         * 打印结果：
        The answer is 42
        Completed in 2018 ms
         */
    }

    /**
     * 2. 使用 async 并发
     *
     * - 类似于 launch，启动一个单独的协程，这是一个 轻量级的线程 并与其它所有的协程一起并发的工作。
     * - 不同点：launch 返回一个 Job 且不附带任何结果值；async 返回一个 Deferred —— 一个轻量级的非阻塞 future，这代表一个将会在稍后提供结果的 promise 。
     * - 可以使用 .await() 在一个延期的值上得到最终结果。
     * - Deferred 也是一个 Job，也可以被取消。
     *
     * Tips: 协程 并发执行 总是 显式的。
     */
    fun demo2() = runBlocking {
        // 对比 demo1() 并发执行
        val time = measureTimeMillis {      // 计算两个挂起函数所需要的总时间
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")

        /**
         * 打印结果（）并发执行，耗时减少了 50%：
        The answer is 42
        Completed in 1017 ms
         */
    }

    /**
     * 3. 惰性启动的 async
     * - async 方法可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
     * - 此模式下，只有结果通过 await 获取时，协程才会启动，或在 Job 的 start() 函数调用事。
     */
    fun demo3() = runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
            one.start() // 启动第一个
            two.start() // 启动第二个
            println("The answer is ${one.await() + two.await()}")  // 先执行协程 one，后执行协程 two，然后等待两个协程执行完毕
        }
        println("Completed in $time ms")
        /**
         * 打印结果：
        The answer is 42
        Completed in 1006 ms
         */
    }

    /**
     * 4. async 异步执行的函数
     *
     * - 定义异步风格的函数来 异步 调用。
     * - 使用 async 协程建造器 并 带有一个显式的 GlobalScope 引用。
     * - 函数结果：只做异步计算，并且需要使用 延期的值获取结果。
     *
     * Tips：以下这种编程风格 Kotlin 中强烈不推荐，原因如下：
     * - 程序出现异常 或 中止，被全局捕获后，代码还是会继续执行，如此会得到错误的逻辑。
     */

    // 返回值类型是 Deferred<Int> ; 可以在任何地方调用，并且异步执行
    @Deprecated(message = "强烈不推荐这种编程风格")
    private fun doSomethingUsefulOneAsync() = GlobalScope.async {
        doSomethingUsefulOne()
    }
    private fun doSomethingUsefulTwoAsync() = GlobalScope.async {
        doSomethingUsefulTwo()
    }

    fun demo4() {
        val time = measureTimeMillis {
            val one = doSomethingUsefulOneAsync()
            val two = doSomethingUsefulTwoAsync()

            // 等待结果 必须调用其它的 挂起或阻塞
            // 当等待结果时，使用 runBlocking {} 来阻塞主线程
            runBlocking {
                println("The answer is ${one.await() + two.await()}")
            }
        }
        println("Completed in $time ms")
        /**
         * 打印结果：
        The answer is 42
        Completed in 1031 ms
         */
    }

    /**
     * 5. 使用 async 的结构化并发
     *
     */

    // 若 concurrentSum() 函数内部发生了错误，抛出了一个异常，所有作用域中启动的协程都会被取消
    private suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        one.await() + two.await()
    }

    fun demo5() = runBlocking {
        val time = measureTimeMillis {
            println("The answer is ${concurrentSum()}")
        }
        println("Completed in $time ms")

        // 取消 始终 通过 协程的层次结构来进行传递
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }

        /**
         * 打印结果：
        The answer is 42
        Completed in 1008 ms
        Second child throws an exception
        First child was cancelled
        Computation failed with ArithmeticException
         */
    }

    // 取消 始终 通过 协程的层次结构来进行传递
    private suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE)
                42
            } finally {
                println("First child was cancelled")
            }
        }

        // 若其中一个子协程失败，第一个 async 以及 等待中的父协程都会被取消
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }

        one.await() + two.await()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val d = CoroutinesSuspendDemo03()
            d.demo1()
            println("-----------")
            d.demo2()
            println("-----------")
            d.demo3()
            println("-----------")
            d.demo4()
            println("-----------")
            d.demo5()
        }
    }
}