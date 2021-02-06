package com.coral.kotlin.chapter2

class ClassDemo01 {

    class Invoice { }

    class Empty

    // 1. 主构造函数 定义方式（仅 1 个）
    class Person constructor(firstName: String) { }

    class Person1(firstName: String) {
        val name = "Coral $firstName"
    }

    // 属性初始化 与 初始化块 代码按顺序执行 （为主构造函数的一部分）
    class Person2(firstName: String) {

        val property1 = "First property: $firstName".also(::println)

        init {
            println("First initializer block that prints $firstName")
        }

        val property2 = "Second property: ${firstName.length}".also(::println)

        init {
            println("Second initializer block that prints ${firstName.length}")
        }
    }

    // 主构造属性可以是var 可变的 或 val 只读的
    class Person3(val firstName: String, val secondName: String, val age: Int) {

    }

    // 不可省略 constructor 关键字
    class Person4 public constructor(name: String) {

    }

    // 2. 次构造函数（有 多个）
    class Person5 {
        var children: MutableList<Person5> = mutableListOf()

        // 声明
        constructor(parent: Person5) {
            parent.children.add(this)
        }
    }

    // 主构造函数委托 this
    class Person6(name: String) {
        var children: MutableList<Person6> = mutableListOf()

        constructor(name: String, parent: Person6) : this(name) {
            parent.children.add(this)
        }
    }

    // 查看执行顺序：初始化代码块 | 属性初始化块 > 次构造函数
    class Person7 {
        init {
            println("Init block")
        }

        constructor(age: Int) {
            println("constructor $age")
        }
    }

    // 默认会生成一个 无参 主构造函数
    class Person8(val name: String = "Coral") {

    }

    // 二、创建类的实例
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            // 注：kotlin 没有 new 关键字
            val person = Person("Xiong")


        }
    }
}
