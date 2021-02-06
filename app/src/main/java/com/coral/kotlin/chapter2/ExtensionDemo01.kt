package com.coral.kotlin.chapter2

/**
 * 扩展
 *
 * 1. 定义：Kotlin 可 扩展一个类的新功能 而不用继承该类 或 使用像装饰者这样的设计模式。
 * 这可以通过叫做「扩展」的特殊声明完成。
 *
 * 例如：可以为一个不能修改的、来自第三方库中的类编写一个新的函数。【嗯，非常强大！】
 * 这个新增的函数就像原始类中的函数一样，可以直接调用。这种机制称为 「扩展函数」。
 *
 * - 扩展函数
 * - 扩展属性：为已经存在的类添加新的属性。
 */

/**
 * 扩展的作用域：
 * - 大多数是在 顶层定义扩展
 * - 要使用 所在包之外的 一个扩展，需要导入包 import package
 *
 * 扩展声明为成员：
 * - 可以在另一个类 内部声明 本类的扩展，如 ThirdParty.swap 直接在 ExtensionDemo01 中声明的。
 * 但是调用时，不能直接用 ThirdParty 的对象实例进行调用，而需借助 ExtensionDemo01。
 */
fun ExtensionDemo01.ThirdParty.swap(i: Int, j: Int) {
    val tmp = this.arr[i]
    this.arr[i] = this.arr[j]
    this.arr[j] = tmp
}

fun ExtensionDemo01.MyClass.Companion.extendMethod() {
    println("Companion")
}

class ExtensionDemo01 {

    class ThirdParty {

        var arr = arrayListOf<Int>()

        fun init() {
            arr.add(1)
            arr.add(2)
        }

        fun m1() { }
    }

    /**
     * 扩展函数：为 ThirdParty 添加 swap 函数。
     *
     * - 声明扩展函数，需要用 接收者类型 为其前缀，如 ThirdParty.methodName
     * - this 关键字在 扩展函数 内部 对应到 接收者对象。
     *
     * Tips:
     * 1. 子类和父类扩展 同一个函数，在真正运行时，由表达式所在类型决定。
     * 也即是，扩展函数是编译期静态解析的，运行时动态绑定并不能影响最终的结果。
     *
     * 2. 若 扩展函数 和 类成员函数 签名一直，则调用时，总是取 成员函数。
     */
//    fun ThirdParty.swap(i: Int, j: Int) {
//        val tmp = this.arr[i]
//        this.arr[i] = this.arr[j]
//        this.arr[j] = tmp
//    }

    fun callSwap() {
        val tp = ThirdParty()

        tp.init()
        // 调用扩展函数
        tp.swap(0, 1)

        tp.arr.forEach {
            println(it)
        }
    }

    // Tips: 1. 扩展是 静态解析的，不能真正修改他们所扩展的类（也即是不能被覆盖，只归当前类所有）
    open class Shape

    class Rectangle: Shape() {

        var color:String = "#ffffff"

        // Tips：2. 若 扩展函数 和 类成员函数 签名一直，则调用时，总是取 成员函数。
        fun getName(): String {
            return "成员函数：Rectangle.getName()"
        }
    }

    fun Shape.getName(): String {
        return "Shape"
    }

    // 函数的两种写法
    fun Rectangle.getName() = "Rectangle"

    // 调用扩展函数
    fun printName(shape: Shape) {
        println(shape.getName())
    }

    // 可控接收者 定义 扩展：
    fun Any?.toString(): String {
        if (this == null) return "null"
        // 空检测后，this 会自动转换为 非空类型，所以以下 toString() 可以解析为 Any 类的成员函数。
        return toString()
    }


    /**
     * 扩展属性
     *
     * - 因为 扩展 没有实际将 成员 插入到类中，所以对 扩展属性 来说，幕后字段 是无效的。
     * 这也是 扩展属性 不能有初始化器。 只能又显式的 getters/setters 定义。
     */

//    val Rectangle.vertexCount = 1  // 这种写法是错误的：扩展属性不能有 初始化器。


    /**
     * 伴生对象扩展
     *
     * - 若一个类定义 有一个伴生对象，则可以为伴生对象的定义 扩展函数与属性。
     * - 可以只使用 类名 作为限定符 来调用 伴生对象的扩展成员。
     */
    class MyClass {
        companion object { }  // 将被称为 Companion
    }

//    fun MyClass.Companion.extendMethod() {
//        println("Companion")
//    }

    fun companionTest() {
        MyClass.extendMethod()
    }

    companion object {  // 伴生对象 == Java static
        @JvmStatic
        fun main(args: Array<String>) {
            var demo = ExtensionDemo01()
            demo.callSwap()

            // 扩展是 静态解析的
            var rect = Rectangle()
            demo.printName(rect)    // 打印 "Shape"

            println(rect.getName()) // 打印 "成员函数：Rectangle.getName()"

            demo.companionTest()
        }
    }

}