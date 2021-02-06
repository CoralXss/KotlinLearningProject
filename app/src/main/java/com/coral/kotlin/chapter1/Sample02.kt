package com.coral.kotlin.chapter1

// 变量
class Sample02 {

    // 变量定义类型一：定义 只读局部变量 使用关键字 val。只能为其赋值一次 【看着像 final】
    fun useVal() {
        val a: Int = 1  // 立即赋值，a,b,c 均不能重新赋值

        val b = 2       // 自动推断出 'Int' 类型

        val c: Int    // 若没有初始值，则类型不能省略
        c = 3

        println("sum = ${a + b + c}")
    }


    // 变量定义类型二：可重新赋值的变量使用 var
    fun useVar() {
        var x = 5       // 自动推断出 'Int' 类型，x 可重新赋值

        x += 5

        println(x)
    }


    // 顶层变量：成员变量
    val PI = 3.14
    var x = 0

    fun incrementX() {
        x += 1
    }

}