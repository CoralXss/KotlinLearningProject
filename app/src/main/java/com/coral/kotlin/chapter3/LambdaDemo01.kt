package com.coral.kotlin.chapter3

/**
 * 高阶函数与 lambda 表达式
 *
 * - Kotlin 函数都是 「头等的」，可以：
 * 1）存储在变量 与 数据结构中；
 * 2）作为参数传递给其他 高阶函数；
 * 3）从其他高阶函数返回。
 *
 * 也即是，可以像操作 任何其他非函数值 一样操作函数。
 *
 * - Kotlin 静态类型编程语言
 *
 * - 函数类型：表示函数 并且 提供一组特定的语言结构，如 lambda 表达式。
 */
class LambdaDemo01 {

    /**
     * 1. 高阶函数：将函数作为 参数 或 返回值 的函数
     *
     * - 函数类型：(R, T) -> R
     * - 定义：函数名: 函数类型。
     */
    fun listCombine() {
        val items = listOf(1, 2, 3, 4, 5)

        // Lambda 表达式 是花括号括起来的代码块
        items.fold(0, {
                acc: Int, i: Int ->
                    val result = acc + i
                    println("acc = $acc, i = $i, result = $result")
                    result
        })

        // lambda 表达式的参数类型是可选的，如果可以直接推断出来的话：
        val joinedToString = items.fold("Elements: ", { acc, i -> "$acc $i"})

        // 函数引用过 也可以用于 高阶函数调用
        val product = items.fold(1, Int::times)
    }

    /**
     * 2. 函数类型
     *
     * - 函数的声明：类似 (Int) -> String 的一系列 函数类型：val onClick: () -> Unit = ...
     *
     * 参数和返回值：
     * - 所有 函数类型 都有 一个圆括号括起来的 参数类型列表 及 一个返回类型：(A, B) -> C
     * 表示：接受参数类型分别为 A、B，返回类型为 C。
     * Tips：参数类型列表可为空：() -> A。 Unit 返回类型不可省略！！！
     *
     * - 函数类型 可以有一个额外的 接收者类型：类型 A.(B) -> C
     * 表示：在 A 的接收者对象上 以一个 B 类型参数 来调用，返回类型为 C。.
     *
     * - 挂起函数 属于特殊种类的 函数类型，表示法中有一个 suspend 修饰符，
     * 如 suspend () -> Unit 或 suspend A.(B) -> C 。
     *
     * Tips:
     * 1）函数类型，可以选择性保函 函数的参数名：(x: Int, y: Int) -> Point
     * 2）若需将 函数类型 指定为 可空，使用 圆括号：((Int, Int) -> Int)?
     * 3）函数类型接合表示法（从右往左）：(Int) -> ((Int) -> Unit) == (Int) -> (Int) -> Unit
     * 但不等于 ((Int) -> (Int)) -> Unit 。
     * 4）函数类型 别名：typealias clickHandler = (Button, ClickEvent) -> Unit
     *
     * 3. 函数类型实例化
     *
     * 获取 函数类型 实例的方式：
     * 1）使用 函数字面值 的代码块，形式如下：
     * - lambda 表达式：{ a, b -> a + b }
     * - 匿名函数：fun(s: String): Int { return s.toIntOrNull() ?: 0 }
     * 2）使用 已有声明 的 可调用引用：
     * - 顶层、局部、成员、扩展函数： ::isOdd，String::toInt
     * - 顶层、成员、扩展属性：List<Int>::size
     * - 构造函数： ::Regex
     * 3）使用 实现函数类型接口 的 自定义类的实例
     */

    // 1)匿名函数
    fun anonymousFun() {
        fun(s: String): Int = s.toIntOrNull() ?: 0
    }

    // 2) 实现 函数类型接口 的自定义类：函数类型可认为是一个接口，能被实现
    class IntTransformer: (Int) -> Int {
        override fun invoke(p1: Int): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    val intFun: (Int) -> Int = IntTransformer()

    // 函数作为 表达式赋值
    val a = { i: Int -> i + 1 }

    // (A, B) -> C 类型的只 可以传给 或 赋值给 A.(B) -> C，反之亦然：
    fun demo01() {
        val repeatFun: String.(Int) -> String = { times -> this.repeat(times) }
        val twoParams: (String, Int) -> String = repeatFun

        fun runTransformation(f: (String, Int) -> String): String {
            return f("hello", 3)
        }
        val result = runTransformation(repeatFun)
        val result1 = runTransformation(twoParams)
    }

    /**
     * 4. 函数类型 实例 调用
     * - 函数类型 的 值 可通过 其 invoke() 操作符调用：f.invoke(x) 或 f(x)
     */
    fun demo02() {
        val strPlus: (String, String) -> String = String::plus
        val intPlus: Int.(Int) -> Int = Int::plus

        println(strPlus.invoke("<-", "->"))
        println(strPlus("Hello ", "world!"))

        println(intPlus.invoke(1, 1))
        println(intPlus(1, 2))
        println(2.intPlus(3)) // 类扩展调用
    }

    // ==========================================
    /**
     * 5. Lambda 表达式与匿名函数
     *
     * 1）lambda 表达式 + 匿名函数 = "函数字面值"，即 未声明的韩式，但可作为表达式传递。
     *
     * 完整语法形式：val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
     * - 参数声明： 在花括号内，并有可选的类型标准；
     * - 函数体： 在一个 -> 符号之后。
     * - 返回值：若推断出 lambda 返回类型非 Unit，则 lambda 主体中的 最后一个表达式 为 返回值。
     * val sum = { x: Int, y: Int -> x + y }
     *
     * 2）传递末尾的 lambda 表达式
     * - 若函数的最后一个参数是 函数，则传递 lambda 表达式时可以放在圆括号之外：
     * val product = items.fold(1) { acc, e -> acc * e }
     * 上述语法称为「拖尾 lambda 表达式」。
     *
     * - 若该 lambda 表达式是调用时唯一的参数，则圆括号可以完全省略：
     * run { println("...") }
     *
     * 3）it：单个参数的隐式名称
     * - 一个 lambda 只有一个参数是很常见的。
     * 如果编译期可以识别签名，也可以不声明唯一的参数并忽略 -> 。该参数会隐式 声明为 it ：
     * ints.filter { it > 0 }  // 字面值是 "(it: Int) -> Boolean" 类型
     *
     * 4）从 lambda 表达式中返回一个值
     * - 使用 限定的返回 语法从 lambda 显式返回一个值。否则，将隐式返回最后一个表达式的值。
     *
     * 以下两个片段是等价的：
     */
    fun demo03() {
        val ints = intArrayOf(1, 2)
        ints.filter {
            val shouldFilter = it > 0  // 隐式返回最后一个表达式的值
            shouldFilter
        }

        // 等价于
        ints.filter {
            val shouldFilter = it > 0
            return@filter shouldFilter  // 使用限定的返回
        }

        //
        var strings = Array<String>(3) {
            var v = it * 2
            v.toString()
        }
        strings.filter { it.length == 5 }.sortedBy { it }.map { it.toUpperCase() }
    }

    /**
     * 6. 匿名函数
     *
     * 定义：
     * - 省略了函数名称
     * - 参数 和 返回类型 同常规函数一致，能根据上下文推断出的参数类型可省略
     * - 返回类型机制：同正常函数一致。1）对于具有 表达式函数体 的匿名函数将自动过推断返回类型；2）对具有代码块函数体的返回类型 必须显式制定个（或假定为 Unit）。
     *
     * 形式：fun(x: Int, y: Int): Int = x + y
     *
     * Tips：匿名函数参数 总是在括号内传递。将函数 留在圆括号外的简写语法 仅适用于 lambda 表达式。
     *
     * 1）VS lambda 表达式：
     * - lambda 缺少 指定函数的返回类型 的能力。
     * 大多数情况非必要，因为返回类型可自动推断。如果要显式指定，可使用另一种语法：匿名函数。
     *
     * -「非局部返回」的行为。一个不带标签的 return 语句总是用 fun 关键字声明的函数中返回。
     * lambda 表达式中的 return 将从包含它的函数返回；而匿名函数中的 return 将从匿名函数自身返回。
     *
     */

    /**
     * 7. 闭包
     *
     * 定义：在外部作用域中声明的变量。
     * lambda 表达式 和 匿名函数（局部函数、对象表达式），均可访问 闭包。
     *
     * - 在 lambda 表达式中，可以修改 闭包中捕获的变量。
     */
    fun demo04() {
        var sum = 0
        var ints = intArrayOf(1, 2, 3)
        ints.filter { it > 0 }.forEach { sum += it }
        println(sum)
    }

    /**
     * 8. 带有接收者的函数字面值 ？？？ 不理解
     */
    fun List<String>.demo05(words: MutableList<String>, maxLen: Int) {
        this.filter {
            it.length <= maxLen
        }
        val articles = setOf("a", "A", "an", "An", "the", "The")
        words -= articles
    }

    fun callDemo01() {
        val words = "A long time ago in a galaxy far far away".split(" ")
        val shortWords = mutableListOf<String>()
        words.demo05(shortWords, 3)  // 在一个类定义另一个类的方法，并可直接调用
        println(shortWords)
    }

    /**
     * 9. 内联函数
     * 背景：使用高阶函数 会带来一些 运行时的效率损失：每个函数都是一个对象，并且会捕获一个闭包。
     * 即，那些在函数体内 会访问到的变量。内存分配（函数对象和类）和虚拟调用都会引入运行时开销。
     *
     * 优化：通过 内联化 lambda 表达式 可 消除这类的开销。
     *
     * 定义：使用 inline 修饰符标记函数
     * inline fun <T> lock(lock: Lock, body: () -> T): T { ... }
     *
     * 调用：
     * lock(1) { foo() }    ？？？不理解
     */

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            LambdaDemo01().listCombine()
        }
    }
}