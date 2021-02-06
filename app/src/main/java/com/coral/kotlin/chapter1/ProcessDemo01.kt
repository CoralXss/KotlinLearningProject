package com.coral.kotlin.chapter1

class ProcessDemo01 {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            val a = 1
            val b = 2

            // 1. if...else...
            if (a > b) {

            } else {

            }
            val max = if (a > b) a else b

            // 2. when 取代 switch
            val x = 3
            when (x) {
                1           -> print("x == 1")
                2,3         -> print("x > 1")
                "1".toInt() -> print("it's 1")
                in 1..10    -> print("x is in the range")
                !in 10..20  -> print("x is outside the range")
                is Int      -> print("x is Int type")
//                !is Double  -> print("x is Int type")
                else        -> print("none of above")
            }

            // 3. for 循环
            val intArr: IntArray = intArrayOf(1, 2, 3)
            intArr[0] = 4
            for (item: Int in intArr) {
                println(item)
            }
            // 索引遍历数组
            for (i in intArr.indices) {
                print(intArr[i])
            }
            // 库函数索引
            for ((index, value) in intArr.withIndex()) {
                println("the element at $index is $value")
            }

            // 区间表达式
            for (i in 1..3) {
                println(i)
            }

            for (i in 6 downTo 0 step 2) {
                println(i)
            }

            // 4. Break 与 Continue 标签（标签写法：任意名称@）
            loop@ for (i in 1..5) {
                for (j in 2..6) {
                    if (j == i) {
                        println("break")
                        // 跳到标签指定的循环后面的执行点，i 循环也不再执行
                        break@loop
                    }
                    println("i = $i, j = $j")
                }
            }

            // 5. return 返回到标签
            foo()

        }

        // is 与 !is 检测一个特定类型的值
        fun hasPrefix(x: Any) = when(x) {
            is String -> x.startsWith("prefix")
            else -> false
        }


        fun foo() {
            listOf(1, 2, 3, 4, 5).forEach {
                // 非局部直接返回到 foo() 的调用者，结束整个方法的执行
                // 注：此种非聚币的返回，仅支持传回 内联函数的 lambda 表达式
                if (it == 2) return
                print(it)
            }
            println("This point is unreachable")
        }

        fun foo2() {
            listOf(1, 2, 3, 4, 5).forEach lit@ {
                // 局部返回到 该lambda 表达式 的调用者，即 forEach
                if (it == 2) return@lit
                print(it)
            }
            println("This point is unreachable")
        }

        fun foo3() {
            listOf(1, 2, 3, 4, 5).forEach(fun(value: Int)  {
                // 从匿名函数返回
                if (value == 2) return
                print(value)
            })
            println("This point is unreachable")
        }
    }
}