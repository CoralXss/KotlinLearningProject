package com.coral.kotlin.chapter2

class GenericsDemo01 {

    // 1. 泛型
    class Box<T>(t: T) {
        var value = t
    }

    // 泛型无处不在，只不过可以推断类型时，就省略了
    val box: Box<Int> = Box<Int>(1)

    // 等价于 (1 为 Int，所有编译期知晓为 Box<Int>)
    var box1 = Box(1)

    /**
     * Java 通配符类型(最麻烦) vs kotlin 声明处型变 + 类型投影
     * - Java 可以利用 限制 通配符 来提高 API 的灵活性；
     * - Java 中的泛型是不型变的，则 List<String> 不是 List<Object> 的子类型；
     *
     * Java 通配符类型参数：? extends E 表示此方法接受 E 或 E 的一些子类型对象的集合，
     *   而不是 E 自身。
     *
     * Kotlin 想实现的为：CustomClass<out T> 可以支持 CustomClass<String> 是 CustomClass<Any> 的子类，
     * 可以直接将 C<String> 赋值为 C<Any> . 在 Java 中 C<String> 是不能赋值给 C<Object> 的。
     *
     * 此为 out T 协变。C<Derived> 可以赋值给 C<Base>。 也即是 C<Base> 可以作为 C<Derived> 的超类。 【 不同于 Java, 泛型也具备继承的概念！！！ 】
     *
     * int T 逆变，则为 C<Base> 可以赋值给 C<Derived>。
     */

    // Java 写法，vs 声明处型变
//    interface Source<T> {
//        T nextT();
//    }
//
//    void demo(Source<String> strs) {
//        Source<Object> objexts = strs;  // !!! 编译器报错
//    }

    /**
     * 2. 声明处型变
     * - 定义：out 修饰符为 型变注解，由其在 类型参数声明处提供，称为 声明处型变。
     * 只被生产（只能从中读取的对象）而不被消费（只能写入的对象）。
     *
     * - <out T>  协变 - 生产但不消费  生产者 out
     * - <in T>   逆变 - 消费但不生产  消费者 in  【案例：Comparable】
     */

    // <out T>  协变 - 生产但不消费
    interface Source<out T> {
        fun nextT(): T
    }

    fun demo(strs: Source<String>) {
        val objects: Source<Any> = strs   // 这个写法没问题，因为 T 是一个 out -参数
    }

    // <in T>   逆变 - 消费但不生产
    interface Comparable<in T> {
        operator fun compareTo(other: T): Int
    }

    fun demoCompare(x: Comparable<Number>) {
        x.compareTo(1.0) // 1.0 为 Double 是 Number 的子类
        val y: Comparable<Double> = x  // Comparable<Number> 赋值给 Comparable<Double>
    }

    /**
     * 3. 类型投影 - 使用处型变
     *
     * 背景：将类型参数 T 声明为 out 很方便，且能避免使用处 子类 型化的麻烦。
     * 缺点：但有些类实际上 不能限制为 只返回 T。一个很好的例子就是 Array.
     */

    fun copy(from: Array<Any>, to: Array<Any>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
        }
    }

    fun testArray() {
        val ints: Array<Int> = arrayOf(1, 2, 3)
        val any = Array(3) { "" }

//        copy(ints, any) // 报错：Array<Int> 和 Array<String> 都不是 Array<Any> 类型

//        copy2(ints, any)
    }

    /**
     * 上述报错说明，Array<T> 在 T 上是不型变的，因此 Array<Int> 和 Array<Any> 都互不为子类型。
     * 为什么？ 因为把 string 赋值给 Int，后面对 Int 取值，就会出现 ClassCastException 异常。
     * 解决：保证 copy() 不会出错，则阻止它 写到 from 数组。可以写成以下方式：
     */
    fun copy2(from: Array<out Any>, to: Array<Any>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
        }
    }

    /**
     * - Array<out Any> 对应于 Java 的 Array<? extends Object>
     * - Array<in String>  对应于 Java 的 Array<? super String>   即可传递 CharSequence 或 Object 数组
     */


    /**
     * 4. 泛型函数：类型参数放在 函数名称之前
     */
//    fun <T> singletonList(item: T): List<T> {
//        // ...
//       return List<T>()
//    }
//
//    fun <T> T.basicToString(): String { // 扩展函数
//        // ...
//        return ""
//    }
}