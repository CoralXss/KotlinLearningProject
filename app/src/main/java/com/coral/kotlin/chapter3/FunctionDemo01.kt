package com.coral.kotlin.chapter3

class FunctionDemo01 {

    // 1. 函数声明：使用 fun 关键字
    fun double(x: Int): Int {
        return 2 * x
    }

    // 2. 函数调用
    val result = double(2)

    // 3. 函数参数：使用 Pascal 表示法定义，即 name:type。每个参数必须有显式类型！！！
    fun powerOf(number: Int, exponent: Int): Int {
        return 0
    }

    // 4. 默认参数：可减少重载数量 【函数调用时，可以省略默认参数，以此实现重载。】
    fun read(b: Array<Byte>, off: Int = 0, len: Int = b.size) {

    }

    // Tips: 覆盖方法总是与 基类型方法 默认参数值相同。当覆盖时，必须省略该默认参数
    open class A {
        open fun foo(i: Int = 10) {

        }
    }

    class B : A() {
        override fun foo(i: Int) {  // 覆盖函数，不能有默认值
            super.foo(i)
        }
    }

    // Tips：若默认参数在一个 无默认参数 之前，则调用时，该默认参数必须传值（具名参数）
    fun foo0(bar: Int = 0, baz: Int) {

    }

    fun testFoo0() {
        foo0(baz = 1)  // 使用默认值 bar = 0
    }

    // Tips：若默认参数最后一个参数是 lambda 表达式，则其既可以作为 具名参数在括号内传入，也可以在 括号外 传入
    fun foo1(
        bar: Int = 0,
        baz: Int = 1,
        qux: () -> Unit
    ) { }

    fun testFoo1() {
        foo1(1) { }         // 使用默认值 baz = 1

        foo1(1, 2) { }

        foo1(qux = { })         // 使用默认值 bar = 0, baz = 1
        // 或
        foo1 {   // lambda 表达式作为参数

        }
    }

    // 5. 具名参数：函数调用时，给参数赋值为 赋值表达式 [ Java 无具名参数语法 ]
    fun reformat(
       str: String,
       lowerCase: Boolean = true,
       upperLetter: Boolean = true,
       wordSeparator: Char = ' '
    ) {}

    fun testReformat() {
        // 调用时，使用具名参数
        reformat("Hello", lowerCase = false, wordSeparator = '_')
    }

    // 6. 返回 Unit 函数：函数不返回任何犹豫的值，则返回类型是 Unit，可以省略。
    fun printHello(name: String?): Unit {
        if (name != null) {
            println("Hello $name")
        } else {
            println("Hi there!")
        }
    }

    // 7. 单表达式函数：当函数返回 单个表达式时，可以省略花括号，直接 = 之后指代代码体
    fun double2(x: Int): Int = x * 2  // 若编译器可推断返回值类型，则 Int 可省

    // 8. 可变数量的参数 （vararg 修饰符，参数数量可变）
    fun <T> asList(vararg ts: T): List<T> {
        val result = ArrayList<T>()
        for (t in ts) {
            result.add(t)
        }
        return result
    }

    fun testVararg() {
        val a = arrayOf(1, 2, 3)

        // 将数组传给可变参数函数，需要在 数组 前面加 * (伸展操作符)
        val  list = asList(1, 2, *a, 4)
    }

    /**
     * 9. 中缀表示法：infix 关键字标识函数
     *
     * 关键字使用满足条件：
     * - 函数必须是 成员函数 或 扩展函数
     * - 必须只有一个参数
     * - 参数不得 接收可变数量的参数 && 不能有默认值
     *
     * 表达式中的优先级：
     * - 低于 算数操作符、类型装换、rangeTo 操作符
     * - 高于 布尔操作符、is / in 检测 及其他操作符。
     */
    infix fun Int.sh1(x: Int): Int = sh1(x)

    fun testInfix() {
        // 中缀表示法 调用 函数 - 省略调用的 点和圆括号
        1 sh1 2
        // 等价于普通调用
        1.sh1(2)
    }

    /**
     * 10. 函数作用域
     *
     * - Kotlin 中函数可以在 文件顶层声明，所以无需 创建一个类保存一个函数
     * - 函数类型：顶层函数、局部函数、成员函数、扩展函数
     *
     * - 局部函数：即一个函数在另一个函数内部
     */

    // demo01：局部函数 - 一个函数在另一个函数内部
    fun dfs(graph: String) {

        fun show(name: String, age: Int) {
            print("$name , $age, $graph")
        }

        // Tips：局部函数可访问外部函数（即闭包）的局部变量
        show("", 20)
    }

    // demo02：成员函数 - 在类或对象内部定义的函数

    // demo03：泛型函数 - 在函数名前使用尖括号指定
//    fun <T> singletonList(item: T): List<T> { }

    // demo04：内联函数
    // demo05：扩展函数
    // demo06：高阶函数和 lambda 表达式

    // demo07：尾递归函数
    /**
     * Kotlin 支持一种称为 尾递归 的函数式编程风格。允许：
     * - 一些常用循环算法改用 递归函数写，而无堆栈溢出的风险。
     * - 用 tailrec 修饰符标记并满足形式事，编译器会优化递归，留下快速而高效的基于循环的版本。
     */

    val eps = 1E-10

//    tailrec fun findFixPoint(x: Double = 1.0): Double {}
//            = if (Math.abs(x - Math.cos(x) < eps) x else findFixPoint(Math.cos(x)))

}