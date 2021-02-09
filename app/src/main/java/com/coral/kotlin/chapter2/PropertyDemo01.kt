package com.coral.kotlin.chapter2

class PropertyDemo01 {

    // 1. 使用对象属性：直接名称引用
    class Address {
        var name: String = "Coralline"

        var street: String = "QingYu Road"
    }

    fun copyAddress(address: Address): Address {
        val result = Address()

        result.name = address.name
        result.street = address.street

        return result
    }

    // 2. Getters 与 Setters
    val i = 1 // 类型 Int，默认 setter 和 getter

    // Tips：只读属性，不允许 setter
    val isEmpty: Boolean
        get() = this.i > 0

    var name: String
        get() = "Coral"
        set(value) {
            this.name = value
        }

    // 3. 幕后字段 + 幕后属性
    /**
     *
     * 1. 幕后字段（TODO 一开始没明白啥用处 ！！！）
     * Tips:
     * 1) field 标识符只能用在 属性的访问器内
     * 2）当一个属性需要一个 幕后字段 时，kotlin 会自动提供。
     * 3）若 属性 至少有一个访问器 使用默认实现，或 自定义 访问器 通过 field 引用幕后字段，
     * 将会为该属性生成一个幕后字段。
     */
    var counter = 0
        set(value) {  // 注：直接为 幕后字段赋值
            /**
             * filed 可以代表字段本身，但是若在 set() 方法中直接给 counter 赋值，则底层是再次调用 set 方法，
             * 通 Java 中，方法自己调用自己，最后是 栈溢出错误，所以可以认为 field 是一个中间变量，为避免这种错误，这是最主要的作用之一。
             * - 作用之二是，自定义 赋值逻辑，也即是 自定义访问器。
             */
            if (value >= 0) field = value
        }

//    var errorSetter = 0
//        set(value) {
//            this.errorSetter = value  // 栈溢出，这一句会继续调用 set(value)
//        }

    val size = 0
    val isEmpty2: Boolean      // 没有使用幕后字段，这是因为 属性 isEmpty2 的赋值是通过另一个属性进行赋值的
        get() = this.size == 0

    // 幕后属性
    private var _table: Map<String, Int>? = null

    public val table: Map<String, Int>
        get() {
            if (_table == null) {
                _table = HashMap()  // 类型参数已推断出
            }
            return _table ?: throw AssertionError("Set to null by other thres")
        }

    /**
     * 4. 编译期常量：const 修饰符标记，此属性需要满足以下需求：
     * - 位于顶层 或 是object声明 或 companion object 的一个成员
     * - 以 String 或 原生类型值初始化
     * - 没有自定义 getter
     *
     * 5. 延迟初始化属性与变量 ？？？
     *
     * 6. 检测一个 lateinit var 是否已初始化 ？？？
     *
     * 7. 委托属性 ？？？
     */

}