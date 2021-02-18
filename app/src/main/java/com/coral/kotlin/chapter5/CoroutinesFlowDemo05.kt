package com.coral.kotlin.chapter5

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutinesFlowDemo05 {

    // 案例对比
    suspend fun simple0(): List<Int> {
        delay(1000)
        return listOf(1, 2, 3)
    }
    // 使用 协程 + list 返回多个值【缺点：只能一次返回所有值。】
    fun demo0() = runBlocking {
        simple0().forEach {
            println(it)
        }  // 直接打印 1，2，3
    }

    /**
     * 1. 异步流
     * - 挂起函数 可以 异步的返回单个值，若要返回多个计算好的值呢？ 使用 Kotlin 流（Flow）。
     *
     * 同步计算值，使用 Sequence；异步计算值，使用 Flow 。
     *
     * Flow 与之前代码对比：
     * - 名为 flow 的 Flow 类型构建器函数
     * - flow {} 构建块中的代码可以挂起
     * - 函数 simple1 不再有 suspend 修饰符
     * - 流 使用 emit    函数 发射 值
     * - 流 使用 collect 函数 收集 值
     */
    fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)  // 发送下一个值
        }
    }

    fun demo1() = runBlocking {
        // 启动并发的协程，验证主线程并未被阻塞
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
        }

        // 收集这个流
        simple().collect { value ->
            println("flow: $value")
        }

        /**
         * 打印结果：
        I'm not blocked 1
        flow: 1
        I'm not blocked 2
        flow: 2
        I'm not blocked 3
        flow: 3
         */
    }

    fun demo2() {

    }

    fun demo3() {

    }

    fun demo4() {

    }

    fun demo5() {

    }

    fun demo6() {

    }

    fun demo7() {

    }

    fun demo8() {

    }

    fun demo9() {

    }

    fun demo10() {

    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val d = CoroutinesFlowDemo05()

            d.demo0()

            println("1.----------")
            d.demo1()
//            println("2.----------")
//            d.demo2()
//            println("3.----------")
//            d.demo3()
//            println("4.----------")
//            d.demo4()
//
//            println("5.----------")
//            d.demo5()
//            println("6.----------")
//            d.demo6()
//            println("7.----------")
//            d.demo7()
//
//            println("8.----------")
//            d.demo8()
//            println("9.----------")
//            d.demo9()
//
//            println("10.----------")
//            d.demo10()
        }
    }
}