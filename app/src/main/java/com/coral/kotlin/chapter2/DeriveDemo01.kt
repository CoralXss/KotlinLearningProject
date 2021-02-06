package com.coral.kotlin.chapter2

import android.content.Context
import android.util.AttributeSet
import android.view.View

// 类继承
class DeriveDemo01 {

    // 1. 继承
    /**
     * 1）隐式继承超类 Any (等同 Java Object)
     * 2）类 默认是 final 不可继承，若允许继承，则需要 open 关键字标记
     */
    class Empty

    // open 开放继承
    open class Base(p: Int)

    // Tips: 子类有一个主构造函数时，父类必须要以子类的主构造参数 就地初始化，不然报错
    class Derived(p: Int) : Base(p)

    // Tips：若子类没有主构造函数，则每个次构造函数必须使用 super 初始化其 父类，或委托给其他构造方法
    class MyView : View {
        constructor(ctx: Context) : super(ctx)

        constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
    }

    /** 3. 覆盖方法
     * 1）父类必须加 open 表示方法可覆盖；
     * 2）子类必须加显式修饰符 override 标识为覆盖方法
     * 3）必须 类 open 才允许方法 open
     * 4）标记为 override 的方法可以继续呗覆写，若不允许继续覆写，则使用 final 修饰
     */
    open class Shape {
        // Tips：若没有加 open，则子类不允许存在相同签名的函数
        open fun draw() { }

        open fun fill() { }

        fun paint() {  }

        open val bgColor: String = "#efefef"

        open val vertexCount: Int = 0;

        open var borderWidth: Double = 0.1
    }

    class Circle() : Shape() {
        // Tips：若此处没有 override 修饰符，则编译器会报错
        override fun draw() {
            super.draw()
        }

        // Tips：覆盖方法添加了 final 则不允许其子类覆盖
        final override fun fill() {
            super.fill()
        }

        override val bgColor: String = "#ff0000"

        // Tips：覆写属性，var 可以覆盖 val 属性，反之不行
        override val vertexCount: Int = 4

        // 使用 var 覆盖 val 编译器会报错
//        override val borderWidth: Double = 2
    }

    /**
     * 4. 覆盖属性 【规则 同 覆盖方法】
     * 1）var 可以覆盖 val 属性，反之不行
     * 2) var 覆盖 val 后，可以赋值为任何数
     * 2）使用 var 覆盖 val 编译器会报错
     */

    open class Rectangle(override val vertexCount: Int = 4) : Shape() {
        open fun size() {
            println("A rectangle size")
        }

        val borderColor: String get() = "black"
    }

    class Polygon : Shape() {
        override var vertexCount: Int = 0
    }

    /**
     * 5. 派生类初始化顺序：父类初始化 > 子类初始化
     */
    open class Base1(val name: String) {
        init {
            println("Initializing Base")
        }

        open val size: Int = name.length.also {
            println("Initializing size in Base: $it")
        }
    }

    // Tips：设计父类时，应避免在 构造函数、属性初始化器 或 init 块中使用 open 成员
    class Derived1(name: String,
                   val lastName: String
    ) : Base1(name.capitalize().also {
        println("Argument for Base: $it")
    }) {
        init {
            println("Initializing Derived")
        }

        override val size: Int = (super.size + lastName.length).also {
            println("Initializing size in Derived: $it")
        }
    }

    /**
     * 6. 调用超类实现：
     * 1) 子类中使用 super 关键字调用父类函数与 属性访问器
     * 2) 内部类 中 访问 外部类的超类，可以使用外部类名限定 super 实现：super@outer
     */
    class FilledRectangle : Rectangle() {

        override fun size() {
            super.size()
            println("A FilledRectangle size")
        }

        val fillColor: String get() = super.borderColor

        override fun draw() {
            val filler = Filler()
            filler.drawAndFill()
        }

        inner class Filler {

            private fun fill() {
                println("Filling")
            }

            fun drawAndFill() {
                // 内部类调用超类 Rectangle 的 size() 方法
                super@FilledRectangle.size()

                fill()

                // 使用超类 Rectangle 实现的 borderColor 的 get()
                println("Drawn a filled rectangle with color ${super@FilledRectangle.borderColor}")
            }
        }
    }

    /**
     * 7. 多继承覆盖规则
     * 1）子类从 不同的超类 继承的 相同成员，必须覆盖并且有自己的实现；
     * 2）表示从哪个 超类 继承的实现，可以使用 super<父类限定名> 表示：super<Base>
     */
    open class R1 {
        open fun draw() {  }
    }

    // Tips：接口成员默认是 "open" 的
    interface P1 {
        fun draw() {  }
    }

    // Tips：继承父类，必须要带上父类的构造方法，有参或无参构造
    class S1 : R1(), P1 {
        override fun draw() {
            super<P1>.draw()  // 调用 P1.draw()
            super<R1>.draw()  // 调用 R1.draw()
        }
    }

    /**
     * 8. 抽象类
     * - 类 或 成员 可以声明为 abstract；
     * - 抽象成员 在本类中 可以不用实现；
     * - 不需要用 open 标识一个 抽象类 或 函数；
     * - 可以使用 抽象成员 覆盖一个 非抽象成员。[Java 也支持]
     */
    open class RealA {
        open fun draw() {}
    }

    abstract class AbstB : RealA() {
        abstract override fun draw()
    }

    /**
     * 9. 伴生对象
     * - 无需用类的实例调用，但是需要访问 内部的函数（静态方法），
     *   可以将其写成 类内 对象声明 中的一员。
     * - 在 类内 声明一个 伴生对象，就可以访问其成员，但需要 以类名作为限定符。
     */
    // todo 如何写？

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            // 注：kotlin 没有 new 关键字
            /**
             * 以下语句执行后打印结果如下：
             *  Argument for Base: Coral   // 属性初始化块
                Initializing Base          // 初始化块
                Initializing size in Base: 5
                Initializing Derived
                Initializing size in Derived: 9
             */
            val derived1 = Derived1("Coral", "Shan")
        }
    }
}