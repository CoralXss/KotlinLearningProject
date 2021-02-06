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
     * Tips:
     * 1) field 标识符只能用在 属性的访问器内
     * 2）TODO 没明白啥用处 ！！！
     */
    var counter = 0
        set(value) {
            if (value >= 0) field = value
        }

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