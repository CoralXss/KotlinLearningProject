package com.coral.kotlin.chapter2

class InterfaceDemo01 {
    /**
     * Tips：
     * 1. Kotlin 的接口可以包含：抽象方法的声明 + 实现
     * 2. vs 抽象类，接口无法保存状态
     * 3. 可以有属性，但必须声明为 抽象或提供访问器 实现
     *
     * 定义：关键字 interface 定义接口
     *
     *
     */

    // 1. 定义接口
    /**
     * 4. 接口中的属性
     * - 抽象的
     * - 提供访问器实现
     * - 不能是幕后字段(backing field)
     */
    interface MyInterface {
        val prop: Int  // 抽象的

        val propertyWithImplementation: String  // 提供访问器实现
            get() = "foo"

        fun bar()

        fun foo() {
            // 可选的方法体
            println(prop)
        }
    }

    // 2. 实现接口：一个类 或 对象 可以实现 一个或多个接口
    class child : MyInterface {

        // 必须覆盖抽象属性
        override val prop: Int
            get() = 29

        override fun bar() {
            // 方法体
        }
    }

    // 3. 多接口实现，解决覆盖冲突
    interface A {
        fun foo() {
            println("A")
        }

        fun bar()
    }

    interface B {
        fun foo() {
            println("B")
        }

        fun bar() {
            println("B bar")
        }
    }

    class C : A {
        override fun bar() {
            println("C bar")
        }
    }

    class D : A, B {
        override fun foo() {
            super<A>.foo()
            super<B>.foo()
        }

        override fun bar() {
            super<B>.bar()
        }
    }

    /**
     * 5. TODO 函数式 SAM 接口 ???
     * - 只有一个抽象方法的接口 称为 函数式接口 或 SAM(单一抽象方法)接口。
     * - 函数式接口可以有 多个 非抽象成员，但只有 一个 抽象成员。
     *
     * 定义：fun 修饰符声明一个 函数式接口
     */
//    fun interface KRunnable {
//        fun invoke() {
//
//        }
//    }
}
