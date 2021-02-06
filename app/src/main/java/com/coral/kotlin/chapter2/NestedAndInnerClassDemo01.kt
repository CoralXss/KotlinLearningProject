package com.coral.kotlin.chapter2

class NestedAndInnerClassDemo01 {

    // 1. 嵌套类
    class Outer {
        private val bar: Int = 1

        class Nested {
            fun foo() = 2
        }
    }

    fun testNested() {
        val demo = Outer.Nested().foo()  // = 2
        println(demo)  // 2
    }

    // 除了嵌套类，还可以嵌套接口。嵌套类型：接口嵌套类，类嵌套接口，接口嵌套接口。

    interface OuterInterface {
        class InnerClass

        interface InnerInterface
    }

    class OuterClass {
        class InnerClass

        interface InnerInterface
    }

    // 2. 内部类：标记为 inner 的嵌套类 能访问外部类的成员。
    // - 内部类 会带有一个 对外部类对象的引用：this
    class Outer2 {
        private val bar: Int = 1

        inner class Inner {
            fun foo() = bar
        }
    }

    fun testInner() {
        val demo = Outer2().Inner().foo()  // 注意对内部类的调用。是否也会构造外部类？
    }

    /**
     * 3. 匿名内部类：使用『对象表达式』创建 匿名内部类实例。
     *
     */
    interface MyInterface {
        fun learn()
    }

    class MyClass {

        fun setMyInterface(inter: MyInterface) {

        }
    }

    fun testAnonymous() {
        val myClass = MyClass()

        myClass.setMyInterface(object : MyInterface {
            override fun learn() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    // 4. 枚举类：最基本用法是 实现 类型安全的枚举
    enum class Direction {
        // 每个枚举常量，都是一个对象
        NORTH, SOUTH, WEST, EAST
    }

    // 初始化
    enum class Color(val rgb: Int) {
        RED(0xff0000),
        GREEN(0x00ff00),
        BLUE(0x0000ff)
    }

    // 5. 匿名类：枚举常量还可以 声明其带有相应方法 以及 覆盖了基类方法的匿名类。 ???
    enum class ProtocolState {
        WAITING {
            override fun signal() = TAKING
        },

        TAKING {
            override fun signal() = WAITING
        };

        abstract fun signal(): ProtocolState
    }

    /**
     * 6. 在枚举类中实现接口
     * - 一个枚举类 可以 实现接口（但不能从类继承）。
     * 【 比 Java 好，接口也可以实现方法，则 UT 埋点枚举重复代码可以解决。 】
     */
    enum class MyEnum : MyInterface {

        // 枚举默认成员：val name: String   val ordinal: Int

        A {
            override fun learn() {
                println("learn A")
            }
        },

        B {
            override fun learn() {
                println("learn B")
            }
        }
    }

    /**
     * 7. 使用 枚举常量
     * - 列出定义的 枚举常量 的方法
     * - 通过名称获取 枚举常量
     */
    fun testEnumValue() {
        MyEnum.A.learn()

        MyEnum.B.learn()

        MyEnum.valueOf("A").learn()

        MyEnum.values()[0].learn()
    }
}