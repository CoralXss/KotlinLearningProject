package com.coral.kotlin.chapter1

class ArrayDemo01 {

    // main 入口方法的写法
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            println("Hello")

            // 1. 使用库方法构建数字
            val asc = arrayListOf<String>()
            asc.add("1")
            asc.add("2")
            // 2. 遍历数组
            for (a in asc) {
                println(a)
            }

            // 使用构造函数构建数字 lambda 表达式
            val arr = Array(5) { i -> (i * i).toString() }
//            arr.forEach { println(it) }

            // 3. 取元素值
            val item1: String = arr[1]
            val item2 = arr.get(0)   // 建议使用 []

            // 4. 原生类型数组(无装箱)
            val intArr: IntArray = intArrayOf(1, 2, 3)
            intArr[0] = intArr[1] + intArr[2]

            // 使用常量初始化数组中的值
            val intArr2 = IntArray(5) { 42 }

            // 使用 lambda 表达式初始化数组中的值
            val intArr3 = IntArray(5) { it * 1 }

            val doubleArr = DoubleArray(3) { it * 0.1 }

            // TODO 以下写法错误，因为 Kotlin 不允许将 Array<String> 赋值给 Array<Any>
//            val errorArrAny: Array<Any> = Array<String>()
            // 解决方式：使用 Array<out Any> 可以赋值，参见 类型投影
            val anyArr: Array<out Any> = Array<String>(2) { "" }
        }
    }

}