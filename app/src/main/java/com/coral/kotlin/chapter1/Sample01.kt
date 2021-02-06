package com.coral.kotlin.chapter1

import android.util.Log

// 函数
class Sample01 {

    // Unit 返回类型可省略
    fun print() {
        println("Hello world !")
    }

    // 2. 返回 Int
    fun sum(a: Int, b: Int): Int {
        return a + b
    }

    // 3. 表达式作为函数体，返回值类型自动推断
    fun sum2(a: Int, b: Int) = a + b

    // 3. 返回无意义的值 [ Unit 返回类型可省略]
    fun printSum(a: Int, b: Int): Unit {
        println("sum of $a and $b iss ${a + b}")
    }

    private fun equalNum() {
        val a: Int = 100
        val boxedA: Int? = a   // Int? 声明，会对变量进行装箱，所有此处为一个装箱的 Int (java Integer)
        val anotherBoxedA: Int? = a

        val b: Int = 1000
        val boxedB: Int? = b
        val anotherBoxedB: Int? = b

        println(boxedA === anotherBoxedA)  // true
        println((boxedB === anotherBoxedB))  // false，为何 100 可以，但是 1000 不行？
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val demo = Sample01()
            demo.equalNum()
        }
    }
}