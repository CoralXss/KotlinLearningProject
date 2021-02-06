package com.coral.kotlin.chapter5

/**
 * 协程
 *
 * - Kotlin 仅在标准库中提供 最底层 API 以便其他各种库能利用协程。
 * - 同其它语言不同，async 与 await 不是 kotlin 的关键字，也不是标准库的一部分。
 * - Kotlin 的「挂起函数」概念，为异步操作 提供了比 future 与 promise 更安全、更不易出错的抽象。
 *
 * - kotlinx.coroutines 是由 JetBrains 开发的功能丰富的协程库，
 * 包含很多启用高级协程的原语，如 launch、async 等。
 *
 *
 */
class CoroutinesDemo01 {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = CoroutinesDemo01()

        }
    }
}