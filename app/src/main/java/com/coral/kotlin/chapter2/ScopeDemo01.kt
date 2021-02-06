package com.coral.kotlin.chapter2

fun baz() { }  // 顶层函数声明

class Bar { }  // 顶层类声明

val a: Int = 0 // 顶层属性声明: 随处可见 [不知道怎么访问 ！！！]

internal val b = 0  // 相同模块可见

class ScopeDemo01 {

    open class Outer {
        private val a = 1

        protected open val b = 2

        internal val c = 3

        val d = 4

        protected class Nested {
            public val e: Int = 5
        }
    }

    class SubClass: Outer() {
        // a 不可见

        // b,c,d 可见
        // Nested 和 e 可见

        override val b: Int   // 继承下来的属性没有下那是声明，则也为 protected
            get() = super.b

        fun test() {
            println(Nested::e)
        }
    }

    class Unrelated(o: Outer) {

        fun test() {
            val o = Outer()
            val unrelated: Unrelated = Unrelated(o)
            println("${o.c} and ${o.d}")

            // o.a 和 o.b 不可见
            // o.c 和 o.d 可见
            // Nested 不可见
        }
    }

    // 构造函数：指定构造函数的可见性（需要显式加 constructor 关键字）
    class C private constructor(a: Int) {

    }

    // 局部声明：局部变量、函数 和 类不能有可见性修饰符 ？？？局部函数？局部类？

    /**
     * 模块
     * 1. 定义：一个模块是编译在一起的一套 Kotlin 文件：
     * - 一个 Intellij IDEA 模块；
     * - 一个 Maven 项目；
     * - 一个 Gradle 源集
     * - 一次 <kotlinc> Ant 任何执行所编译的一天啊文件。
     *
     * 2. 可见性修饰符 internal 表示 改成员仅在相同模块内可见。
     * 【Android 多模块，其他模块不能访问。非常好，隐藏模块实现细节，避免外部无用 API 访问。】
     */

}
