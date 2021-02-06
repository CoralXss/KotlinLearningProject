package com.coral.kotlin.chapter2

class ObjectExpressionDemo01 {

    /**
     * 对象表达式 与 对象声明
     *
     * - 不用为类 显式声明新的子类，则使用 对象表达式 和 对象声明 处理。
     */

    // 1. 对象表达式

    // Demo01：创建一个继承自 某个/某些 类型的匿名类的对象
    abstract class BaseAdapter {
        abstract fun getCount(): Int
    }

    class MyListView {

        // 初始化一个 adapter，对象表达式 object : BaseAdapter
        var adapter: BaseAdapter = object : BaseAdapter() {
            override fun getCount(): Int = 3
        }
    }

    // demo02：若超类有多个格构柱函数，则传递构造参数，多个超类使用 , 分隔
    open class A(x: Int) {
        public open val y: Int = x
    }

    interface B {
        fun b()
    }

    val ab: A = object : A(1), B {
        override fun b() {
            println("y = $y")
        }

        override val y: Int
            get() = 15
    }

    // demo03：直接创建一个匿名对象，省去类定义？
    fun createObject() {
        val adHoc = object {
            var x: Int = 0
            val y: Int = 0
        }
        print(adHoc.x + adHoc.y)
    }

    // demo04：
    class C {

        // 使用 匿名对象 作为函数的返回类型，私有函数返回类型为 匿名对象类型
        private fun foo() = object {
            val x: String = "x"
        }

        // 共用函数，其返回类型为 Any
        fun publicFoo() = object {
            val x: String = "x"
        }

        fun bar() {
            val x1 = foo().x   // 正确，返回值为 匿名对象，可以直接访问器成员属性
//            val x2 = publicFoo().x  // 错误：未能解析的引用 'x'，因为返回值为 Any
        }
    }


    /**
     * 2. 对象声明
     *
     * - 总是在 Object 关键字后跟一个名称。就像声明变量一样，对象声明不是一个表达式，不能再赋值语句右边。
     * - 对象声明 的初始化过程是 线程安全的并且在首次访问时进行。
     * - 引用单例对象，则直接用 名称即可。
     *
     * Tips：对象声明，不能在 局部作用域（即直接嵌套在 函数内部），但是可以嵌套到其他对象声明 或 非内部类中。
     */

    // demo01: 单例模式，在一些场景中很有用，Kotlin 使单例声明变得很容易：

    class DataProvider {

    }

    object DataManager {
        fun register(provider: DataProvider) {
            //
        }

        val providers: Collection<DataManager>
            get() {
                TODO()
            }
    }

    fun testSingleton() {
        DataManager.register(DataProvider())
    }

    /**
     * 3. 伴生对象
     * - 类内部的对象声明 可以用 companion 关键字标记。
     */

    class MyCompanion {
        companion object Factory {
            fun create(): MyCompanion = MyCompanion()
        }
    }

    // Tips：伴生对象的成员 可通过 只是用类名 作为限定符来调用
    val instance = MyCompanion.create()

    // Tips：伴生对象的成员 很像其他语言的 静态成员，但运行时，仍是 真实对象的实例成员 && 可实现接口。
    interface Factory<T> {
        fun create(): T
    }

    class MyClass {
        // 伴生对象，实现接口
        companion object : Factory<MyClass> {
            override fun create(): MyClass {
                return MyClass()
            }
        }
    }

    // public static MyClass f ?
    val f: Factory<MyClass> = MyClass

    // Tips：使用 @JvmStatic 注解，可以将伴生对象的成员 生成为 真正的静态方法和字段。如 main 入口方法

    /**
     * 总结：对象表达式 和 对象声明 直接的预计差别：
     *
     * - 对象表达式 是在使用它们的地方 立即执行（及初始化）的；【单例】
     * - 对象声明 是在第一次被访问时 延迟初始化的；【工程模式】
     * - 伴生对象 的初始化 是在相应的类被加载（解析）时，与 Java 静态初始化器的语义相匹配。
     */
}