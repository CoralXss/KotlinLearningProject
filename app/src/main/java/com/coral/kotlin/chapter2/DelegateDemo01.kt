package com.coral.kotlin.chapter2

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * 一文彻底弄懂 Kotlin 中的委托：https://mp.weixin.qq.com/s/BD1zT80IADDZS4CAxmooPg
 *
 * 注：委托在 Kotlin 的地位举足轻重，特别是「属性委托」，lazy 延迟初始化非常多。
 * 比如使用属性委托封装 SharedPreference 。
 *
 * 优点：
 * - 符合高内聚低耦合设计思想。
 * - 委托能减少很多重复的样板代码。
 *
 *
 * 1. 由委托实现
 *
 * - 「委托模式」是实现继承的一个很好的替代方式，Kotlin 可以「零样板代码」地原生支持它。
 * 如 Derived 类可以通过 委托 将其所有公有成员 委托给指定对象 来实现一个接口 Base 。
 *
 *
 */
class DelegateDemo01 {

    /**
     * 1. 由委托实现
     *
     * 1.1 Derived 类可以通过 委托 将其所有公有成员 委托给指定对象 来实现一个接口 Base.
     *
     * 1.2 覆盖由委托实现的接口成员
     * - 覆盖符合预期：编译器会使用 override 覆盖的实现而不是委托对象中的。
     */
    interface Base {

        val message: String

        fun print()

        fun printMessage()
    }

    class BaseImpl(val x: Int) : Base {

        override val message: String = "BaseImpl: x = $x"

        override fun print() {
            print(message)
        }

        override fun printMessage() {
            println(x)
        }
    }

    // by 子句表示 b 将会在 Derived 中内部存储，并且 「编译器将生成」 转发给 b 的所有的 Base 的方法。
    class Derived(b: Base) : Base by b {

        override val message: String       // 在 b 的 print() 实现中，不会访问到这个属性（委托不是继承）。  Derived(b).print() 打印的是：BaseImpl: x = 10
            get() = "Message of Derived"

        // 自定义 委托对象的覆盖方法
        override fun printMessage() {
            print("abc")
        }
    }

    fun demo1() {
        val b = BaseImpl(10)
        Derived(b).print()  // 注：这里 Derived 是没有 print() 函数的，但是经 Base 委托可以调用其不存在的方法，实际是调用 Base 的 print() 方法。

        Derived(b).printMessage()  // 注：这里调用自定义的方法。但是委托对象的成员只能访问其自身对接口成员实现。「也即是委托不是继承，委托覆盖的成员只能访问自身接口成员实现。」
    }

    /**
     * 2. 委托属性
     *
     * - 延迟属性：值仅在首次访问时计算，其他时候直接返回值；
     * - 可观察属性：当属性变更时，监听器会收到通知；
     * - 把多个属性 存储在一个 map 中，而不是每个单独的字段中（将数据对象转换为 map）。
     *
     * 语法：val/var <属性名>: <类型> by <类型>
     * - by 后面的表达式为 该 委托，属性对应的 get() 和 set() 会被委托给 getValue() 与 setValue() 方法。
     * - 属性的委托 不必实现任何的接口，但是需要提供一个 getValue() 和 setValue() 函数（有 set 则对应 var 属性）。
     */
    class Delegate {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return "$thisRef, thank you for delegating '${property.name}' to me!"
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            println("$value has been assigned to '${property.name}' in $thisRef")
        }
    }

    class Example {
        var p: String by Delegate()
    }

    fun demo2() {
        val exp = Example()
        println(exp.p)   // 打印：com.coral.kotlin.chapter2.DelegateDemo01$Example@41629346, thank you for delegating 'p' to me!

        exp.p = "NEW"    // 调用 setValue()
        println(exp.p)   // 调用 getValue()
        /**
         * 输出：
         * NEW has been assigned to 'p' in com.coral.kotlin.chapter2.DelegateDemo01$Example@41629346
         * com.coral.kotlin.chapter2.DelegateDemo01$Example@41629346, thank you for delegating 'p' to me!
         */
    }

    /**
     * 2.1 延迟属性
     *
     * 属性求值模式：
     * - LazyThreadSafetyMode.SYNCHRONIZED，默认求值是同步锁的。该值仅在一个线程中计算，并且所有线程都会看到 相同的值。
     * - LazyThreadSafetyMode.PUBLICATION，多线程同时执行，非线程安全
     * - LazyThreadSafetyMode.NONE，初始化总是发生在与属性高使用位于相同的线程，此种模式不会有任何线程安全的保证以及相关的开销。
     */
    val lazyValue: String by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        println("Computed!")
        "Hello!"
    }

    fun demo3() {
        println(lazyValue)  // 首次打印：Computed!  Hello!
        println(lazyValue)  // 再次打印：Hello!
    }

    /**
     * 2.2 可观察属性
     *
     * Delegates.observable() 接收两个参数：
     * - 初始值
     * - 修改时处理程序。
     * 每当给属性赋值时，会调用该 处理程序（在赋值后执行）。三个参数：被赋值的属性、旧值与新值。
     */
    class User {
        var name: String by Delegates.observable("<no name>") {
            property, oldValue, newValue ->
            println("$oldValue -> $newValue")
        }
    }

    fun demo4() {
        val user = User()
        user.name = "first"   // <no name> -> first
        user.name = "second"  // first -> second
    }

    /**
     * 2.3 委托给另一个属性
     *
     * - 一个属性可以把它的 getter 和 setter 委托给另一个属性。这种委托对于 顶层和类的属性（成员和扩展）都可用。该委托属性可以为：
     * - 顶层属性
     * - 同一个类的成员 或 扩展属性
     * - 同一个类的成员 或 扩展属性
     *
     * 为将一个属性委托给另一个属性，应在委托名称中使用恰当的 :: 限定符。
     * 应用场景：向后兼容 重命名属性。
     */
//    class MyClass {
//        var newName: Int = 0
//        @Deprecated("Use 'newName' instead", ReplaceWith("newName"))
//        var oldName: Int by this::newName
//    }
//
//    fun demo5() {
//        val myClass = MyClass()
//        myClass.oldName = 42
//        println(myClass.newName)
//    }

    /**
     * 2.4 将属性存储在 映射中
     *
     */
    class Person(val map: Map<String, Any?>) {
        val name: String by map
        val age: Int     by map
    }

    fun demo6() {
        val person = Person(mapOf(
            "name" to "Coral",
            "age"  to "25"
        ))
        println(person.name)
        println(person.age)
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val d = DelegateDemo01()

            d.demo1()
            println("\n2. -----------")
            d.demo2()
            println("3. -----------")
            d.demo3()
            println("4. -----------")
            d.demo4()
        }
    }
}