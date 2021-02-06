package com.coral.kotlin.chapter3

// 区间与数列
class RangeAndSequenceDemo01 {

    /**
     * 1. 区间与数列
     * - kotlin.ranges 包中的 rangeTo() 函数及其操作符形式 .. ，可创建区间。
     * - rangeTo() 会辅以 in 或 !in 函数。
     * - 整数类型区间（IntRange、LongRange、CharRange）：可以对其进行迭代，可用于 for 循环。
     *
     */
    fun demo01() {
        val i = 2
        if (i in 1..4) {
            println(i)
        }

        // 区间 for 循环
        for (j in 1..4) {
            print("$j, ")
        }

        // downTo 函数：反向迭代数字
        for (j in 4 downTo 1) {
            print("$j, ")
        }
        println()

        // step 函数：通过任意步长（不一定为 1）迭代数字
        for (j in 1..8 step 2) {
            print("$j, ")
        }
        println()
        for (j in 8 downTo 1 step 2) {
            print("$j, ")
        }
        println()

        // until 函数：迭代不包含其结束的是数字区间
        for (i in 1 until 10) { // 区间 [1, 10)
            println(i)
        }
    }

    /**
     * 2. 区间
     * - 定义：由两个端点值定义，两个端点值再该区间内。
     * - 有序，可以定义 任何实例 是否在给定实例区间内。
     * - 主要操作是 contains，一般以 in 与 !in 操作符形式使用。
     */

    class Version(val i: Int, val j: Int)

    // 为类创建一个区间
    fun demo02() {
//        val versionRange = Version(1, 10)..Version(1, 30)
//        println(Version(0, 9) in versionRange)  // false
//        println(Version(1, 20) in versionRange) // true
    }

    /**
     * 3. 数列
     *
     * - 证书类型的区间（如 Int, Long, Char） 可视为「等差数列」。Kotlin 中，这些数列由特殊类型定义：
     * IntProgression、LongProgression 与 CharProgression .
     *
     * - 数列具有三个基本属性：first、last、和一个非零的 step 元素。
     *
     * 类似 js
     * for (int i= first; i <= last; i += step) { // ... }
     */

    /**
     * 4. 序列
     *
     * - 序列的多步处理在可能的情况下，会延迟执行：仅当请求整个处理链的结果时，才进行实际计算。
     * - Sequence 对每个元素 逐个执行所有处理步骤，不会生成 中间步骤的结果，从而提高整个集合处理链的性能。
     *
     * Sequence vs Iterable
     * - 两者函数一致
     * - Iterable 每个处理步骤完成并返回结果 —— 中间集合。
     * - Sequence 仅当请求整个处理链的结果时，才进行实际计算。
     *
     * 注：需要注意在哪些场景使用 Iterable 或 Sequence 。
     *
     * 构造序列的方式
     * - sequenceOf() 函数
     * - 调用 List/Set 的 asSequence()
     * - 通过 函数作为参数构建
     */
    fun demo03() {
        // 1. 通过 sequenceOf() 函数，创建序列
        val numberSequence = sequenceOf("four", "three", "two", "one")

        // 2. 通过 Iterable 对象（List 或 Set），调用 asSequence() 创建一个序列
        val numbers = listOf("one", "two", "three", "four")
        val numbersSequence = numbers.asSequence()

        // 3. 基于函数 generateSequence() 构建序列
        println("----- by 函数 -----")
        val oddNumbers = generateSequence(1) {
            it + 2    // it 是上一个元素
        }
        println(oddNumbers.take(5).toList())  // [1, 3, 5, 7, 9]
//        println(oddNumbers.count())      // 运行报错：Count overflow has happened. 因为序列是无限的。

        // 使用 generateSequence() 创建有限序列，则最好一个元素之后返回为 null
        val oddNumbersLessThan5 = generateSequence(1) {
            if (it + 2 < 5) it + 2 else null
        }
        println(oddNumbersLessThan5.count())   // 2

        // 4. 有一个函数可以 逐个或任意大小的组块生成序列元素 —— sequence() 函数。采用 laambda 表达式，包含 yield() 与 yieldAll() 函数的调用。
        val oddNumbers2 = sequence {
            yield(1)  // 将一个元素返回给序列使用者，并暂停 sequence() 的执行，知道使用者请求下一个元素。
            yieldAll(listOf(3, 5))  // ??????
            yieldAll(generateSequence(7) { it + 2 })
        }
        println(oddNumbers2.take(5).toList()) // [1, 3, 5, 7, 9]

        /**
         * 5. 序列操作
         * -「无状态」操作不需要状态，且 可以独立处理每个元素，如 map() 或 filter()。
         * -「无状态」操作可能需要少量常数个状态来处理元素，如 take() 和 drop()。
         * -「有状态」操作需要大量状态，通常与序列中元素的数量成正比。
         *
         * 注：若序列操作，返回延迟生成了另一个序列，则成为「中间序列」，该操作为 末端操作，如 toList() 或 sum().
         * 只能通过末端操作才能 检索序列元素。
         */
        // 序列操作示例
        val words = "The quick brown fox jumps over the lazy dog".split(" ")
        val lengthList = words.filter {
            println("filter: $it")
            it.length > 3
        }.map {
            println("length: ${it.length}")
            it.length
        }.take(4)
        println("Lengths of first 4 words longer than 3 chars: ")
        println(lengthList)
        /**
         *  运行结果如下：先 filter 过滤元素，再对过滤后的元素进行长度打印
        filter: The
        filter: quick
        filter: brown
        filter: fox
        filter: jumps
        filter: over
        filter: the
        filter: lazy
        filter: dog
        length: 5
        length: 5
        length: 5
        length: 4
        length: 4
        Lengths of first 4 words longer than 3 chars:
        [5, 5, 5, 4]
         */

        // 将上述例子改为 sequence
        val wordsSequence = words.asSequence()
        val lengthSequence = wordsSequence.filter {
            println("filter: $it")
            it.length > 3
        }.map {
            println("length: ${it.length}")
            it.length
        }.take(4)
        println("Lengths of first 4 words longer than 3 chars: ")
        println(lengthSequence.toList())
        /**
         * 打印结果如下：仅在 构建结果列表后，再调用 filter() 和 map()。当结果大小达到 4 时，处理停止。
        Lengths of first 4 words longer than 3 chars:
        filter: The
        filter: quick
        length: 5
        filter: brown
        length: 5
        filter: fox
        filter: jumps
        length: 5
        filter: over
        length: 4
        [5, 5, 5, 4]
         */

        // 两种操作对比：序列需要 18个步骤，而不是 23个步骤 来执行列表操作。
    }



    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = RangeAndSequenceDemo01()

            d.demo01()

            d.demo02()

            d.demo03()
        }
    }
}