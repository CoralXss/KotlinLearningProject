package com.coral.kotlin.chapter4

/**
 * 1. 集合操作 概述
 * - kotlin 标准库提供了 用于对 集合执行操作的 多种函数。
 * 包括简单的操作，如 获取 或 添加元素，
 * 以及更复杂的操作，如 搜索、排序、过滤、转换等。
 *
 * 2. 扩展函数与成员函数
 * - 集合操作 在 标准库中以两种方式声明：集合接口的 成员函数 和 扩展函数。
 * - 成员函数：定义了对于集合类型 必不可少的操作，如 isEmpty()、get() 等；
 * - 创建自己的集合接口实例，请使用标准库中的框架：AbstractCollection、AbstractList/Set/Map 及相应 可变抽象类。
 * - 扩展函数：除成员函数的 其他操作，如 map()、sort()、filter() 等。
 *
 * 3. 公共操作：用于 「只读集合 与 可变集合」。场景操作分类如下：
 * - 集合 转换
 * - 集合 过滤
 * - plus 与 minus 操作符
 * - 分组
 * - 取集合 子集
 * - 取集合 单个元素
 * - 集合 排序
 * - 集合 聚合操作
 *
 * 注：上述操作将返回其结果，不会影响 原始集合。
 * 如：一个 filter 操作产生一个新集合，包含与过滤词匹配的所有元素。
 *
 * 对于某些集合操作，可以 指定目标对象。
 * 目标是一个可变集合，该函数将其结果项附加到该 可变对象中，而不是在新的对象返回。
 *
 * 对于执行带有目标的操作，函数名称中带有「to」 后缀，如 filterTo()、associateTo()
 *
 * 4. 写操作：对可变集合，存在可更改几个状态的 写操作，如：
 * - 添加、删除、更新。
 *
 * 注：某些操作，含义相同，但是 一个函数直接应用该操作，另一个函数将结果作为答案度的集合返回。
 * 如 sort() 直接对集合进行排序，因此其状态发生了变化；
 *    sorted() 创建一个新集合，该集合包含 按排序顺序相同的元素。
 */
class CollectionOperationDemo01 {

    /**
     * 3. filter 与 filterTo 区别（除了 过滤，还有 map、associate、group、flat 等）
     */
    fun demo01() {
        val numbers = listOf("one", "two", "three", "four")
        val result = numbers.filter { it.length > 3 }
        println(numbers)  // [one, two, three, four]
        println(result)   // [three, four]

        val filterResults = mutableListOf<String>() // 目标对象
        numbers.filterTo(filterResults) { it.length > 3 }
        println(filterResults)  // [three, four]

        numbers.filterIndexedTo(filterResults) { index, _ -> index == 0 }
        println(filterResults)  // [three, four, one] 包含两个操作的结果
    }

    /**
     * 4. 可变集合写操作：sort() 与 sorted()
     */
    fun demo02() {
        val numbers = mutableListOf("one", "two", "three", "four")

        val sortedNumbers = numbers.sorted() // 创建新的集合
        println("$numbers,  $sortedNumbers") // [one, two, three, four],  [four, one, three, two]
        println(numbers == sortedNumbers)    // false

        numbers.sort()
        println(numbers)                     // 直接应用于原始集合：[four, one, three, two]
        println(numbers == sortedNumbers)    // true
    }

    // 二、集合转换：扩展函数，根据转换规则从现有集合中构建新集合。
    /**
     * 1. 映射 - map()
     * -「映射」转换，从 另一个集合的元素上的函数结果创建一个集合。
     * - 作用：将给定的 lambda 函数应用与 每个后续元素，并返回 lambda 结果列表。
     * - 结果：顺序与元素的顺序相同。
     * 另，使用元素索引作为参数的转换，mapIndexed() .
     *
     */
    fun demo03() {
        val numbers = setOf(1, 2, 3)
        //
        println(numbers.map { it * 3 })  // [3, 6, 9]
        // 使用 元素索引 作为参数的转换
        println(numbers.mapIndexed { index, value -> value * index }) // [0, 2, 6]

        // 在某些元素上产生 null 值，通过以下函数。
        println(numbers.mapNotNull { if (it == 2) null else it * 3 }) // [3, 9]
        println(numbers.mapIndexedNotNull { index, value -> if (index == 0) null else value * index }) // [2, 6]

        /**
         * 映射转换，有两个选择：转换键，值不变，反之亦然。
         * - 转换键：mapKeys()；
         * - 转换值：mapValues()
         */
        val numberMap = mapOf("k1" to 1, "k2" to 2, "k3" to 3, "k11" to 11)
        println(numberMap.mapKeys { it.key.toUpperCase() })       // {K1=1, K2=2, K3=3, K11=11}
        println(numberMap.mapValues { it.value + it.key.length }) // {k1=3, k2=4, k3=5, k11=14}
    }

    /**
     * 2. 合拢 - zip()
     * -「合拢」转换，根据两个集合中 具有相同位置的 元素构建配对。
     * 也即是在一个集合（数组）上以另一个集合（数组）作为参数调用。
     *
     * - zip() 返回 Pair 对象的列表 (List)。
     * - 若集合的大小不同，则 zip() 的结果为较小集合的大小；结果中 不包含较大集合的后续元素。
     * - zip() 也可以用 中缀形式 调用「a zip b」。
     *
     * - 可以使用 两个参数的转换函数来 调用 zip()：接收元素 和 参数元素。
     *
     * 当拥有 Pair 的 List 时，可以进行反向 unzipping - 从这些键值对中构建两个列表：
     * - 第一个列表 包含 原始列表中的每个 Pair 的键。
     * - 第二个列表 包含 原始列表中的每个 Pair 的值。
     * 分割键值对，使用 unzip() .
     */
    fun demo04() {
        val colors = listOf("red", "brown", "grey")
        val animals = listOf("fox", "bear", "wolf")
        println(colors zip animals)      // [(red, fox), (brown, bear), (grey, wolf)]

        val twoAnimals = listOf("fox", "bear")
        println(colors.zip(twoAnimals))  // [(red, fox), (brown, bear)]

        // [The Fox is red, The Bear is brown, The Wolf is grey]
        println(colors.zip(animals) { color, animal -> "The ${animal.capitalize()} is $color" })

        // unzip() 用法
        val numberPairs = listOf("one" to 1, "two" to 2, "three" to 3, "four" to 4)
        // [(one, 1), (two, 2), (three, 3), (four, 4)]
        println(numberPairs)
    }

    /**
     * 3. 关联 - associateWith() - 构建 Map
     * -「关联」转换允许 从集合元素 与其关联的 某些值 构成 Map。
     *   不同的关联类型中，元素可以是关联 Map 中的键和值。
     * - 基本关联函数 —— associateWith() 创建一个 Map，其中原始集合的元素是 键，
     * 通过给定的转换函数从中产生值。  若两个元素相等，则仅保留最后一个在 Map 中。
     *
     * associateBy()
     * - 使用集合元素 作为 value 构建 Map：根据元素值返回 key （默认为元素索引，另外还可以根据元素值进行转换） 。
     * - 若 元素相等，则仅保留最后一个。
     *
     * associate()
     * - 构建 Map，key 和 value 都是通过集合元素生成的。
     * - 需要一个 lambda 函数，该函数返回 Pair：键和相应 Map 条目的值。
     *
     * 注：associate() 会生成临时的 Pair 对象，可能影响性能。
     */
    fun demo05() {
        val numbers = listOf("one", "two", "three", "four")

        // 将 原始元素 与 关联函数值，组合起来，返回一个 Map
        println(numbers.associateWith { it.length }) // {one=3, two=3, three=5, four=4}

        // associateBy
        println(numbers.associateBy { it.first().toUpperCase() }) // {O=one, T=three, F=four}
        println(numbers.associateBy(
            keySelector = { it.first().toUpperCase() },           // {O=3, T=5, F=4}
            valueTransform = { it.length } )
        )

        // associate
        val names = listOf("Alice Adams", "Brian Brown", "Clara Campbell")
//        println(names.associate { name -> parseFullName(name).let { it.lastName to it.firstName } })
    }

    fun parseFullName(fullName: String) {

    }

    /**
     * 4. 打平 - flatten
     *
     * flatten()
     * - 对 嵌套的集合 进行打平访问，返回类型：List
     *
     * flatMap()
     * - 使用一个函数 将一个集合元素 映射到 另一个集合。
     * - 返回 单个列表中 包含所有元素的值。
     * - flatMap() 表现为 map()[以集合作为映射结果] 与 flatten() 的连续调用。
     */
    fun demo06() {
        // flatten()
        val numberSet = listOf(setOf(1, 2, 3), setOf(4, 5, 6), setOf(1, 2))
        println(numberSet.flatten())  // [1, 2, 3, 4, 5, 6, 1, 2]

        // flatMap()
        val containers = listOf(
            StringContainer(
                listOf(
                    "one",
                    "two",
                    "three"
                )
            ),
            StringContainer(
                listOf(
                    "four",
                    "five",
                    "six"
                )
            ),
            StringContainer(
                listOf(
                    "seven",
                    "eight"
                )
            )
        )
        println(containers.flatMap { it.values })  // [one, two, three, four, five, six, seven, eight]
    }

    class StringContainer(val values: List<String>)

    /**
     * 5. 字符串表示
     * - joinToString() 与 joinTo() : 将集合转换为字符串，以 可读形式 检索集合内容。
     *
     * joinToString()
     * - 根据提供的参数 从集合元素 构建单个 String。
     *
     * joinTo()
     * - 操作一致，不过会将结果「追加」到给定的 Appendable 对象。
     * - 各元素的字符串表示形式 以空格分隔而成的 String .
     *
     * 注：
     * - 要构建自定义字符串 表现形式，可以指定参数：separator、prefix 与 postfix。
     * 结果字符串 以 prefix 开头，postfix 结尾。除最后一个元素外，separator 将位于每个元素之后。
     *
     * - 对于较大的集合，可能需要指定 limit —— 将包含在结果中元素的数量。
     * 若集合大小超出 limit，所有其他元素将被 truncated 参数的答案个值替换。
     */
    fun demo07() {
        val numbers = listOf("one", "two", "three", "four")

        println(numbers)  // [one, two, three, four]
        println(numbers.joinToString()) // one, two, three, four
        println(numbers.joinToString(";")) // one;two;three;four

        val listString = StringBuffer("The list of numbers: ")
        numbers.joinTo(listString)
        println(listString) // The list of numbers: one, two, three, four

        // start: one | two | three | four: end
        println(numbers.joinToString(separator = " | ", prefix = "start: ", postfix = ": end"))

        // Elements: ONE, Elements: TWO, Elements: THREE, Elements: FOUR
        println(numbers.joinToString { "Elements: ${it.toUpperCase()}" })
    }

    /**
     * 6. 过滤 - filter() / filterIndexed() / filterNot()
     * - 最常用的集合处理任务之一。
     * - 接受一个集合元素，返回布尔值的 lambda 表达式：true - 说明给定元素与 谓词 匹配；反之 false .
     * - 扩展函数：不会改变原始集合。因此可 用于 可变集合与只读集合。
     * - 过滤操作结果：赋值给变量 或 链接给其他函数。
     *
     * filter()
     * - 返回与谓词匹配的 集合元素。
     * - 过滤结果：List 和 Set 过滤，结果为 List；Map 为 Map .
     *
     * 注：filter() 中的谓词仅能检查 元素的值，若想使用 元素在集合中的位置，则使用以下函数：
     *
     * filterIndexed()
     * - 接收两个参数的谓词：元素的索引 + 元素的值
     *
     * 注：若相关使用 否定条件 过滤集合，则使用:
     *
     * filterNot()
     * - 输出 不满足过滤条件的 元素列表。
     *
     * filterIsInstance()
     * - 通过 过滤规定类型的元素 来缩小元素的类型。
     * - 返回给定类型的集合元素。在 List<Any> 上调用时，该函数 filterIsInstance<T> 返回一个 List<T>，从而能够在
     */
    fun demo08() {
        val numbers = listOf("one", "two", "three", "four")
        val longerThan3 = numbers.filter { it.length > 3 }
        println(longerThan3)

        val numbersMap = mapOf("k1" to 1, "k2" to 2, "k3" to 3, "k11" to 11)
        val filteredMap = numbersMap.filter { (k, v) -> k.endsWith("1") && v > 10 }
        println(filteredMap)    // {k11=11}

        val filteredIndex = numbers.filterIndexed { index, s -> (index != 0) && (s.length < 5) }
        println(filteredIndex)  // [two, four]

        val filteredNot = numbers.filterNot { it.length <= 3 }
        println(filteredNot)    // [three, four]

        /**
         * All String elements in upper case:
         * TWO
         * FOUR
         */
        val numbers2 = listOf(null, 1, "two", 3.0, "four")
        println("All String elements in upper case: ")
        numbers2.filterIsInstance<String>().forEach { println(it.toUpperCase()) }
    }

    /**
     * 7. 划分 - 过滤函数
     *
     * partition()
     * - 通过一个 谓词 过滤集合 并且将不匹配的元素存放在一个单独的列表中。
     * - 返回值：Pair<List, List> 第一个列表包含与 谓词匹配的元素，第二个列表包含 原始集合中的所有其他元素。
     *
     */
    fun demo09() {
        val numbers = listOf("one", "two", "three", "four")
        val (match, rest) = numbers.partition { it.length > 3 }
        println(match)  // [three, four]
        println(rest)   // [one, two]
    }

    /**
     * 8. 检验谓词
     * - 有些函数，仅针对集合元素简单地检测一个谓词：
     * 1）若 至少有一个元素 匹配 给定谓词，则 any() 返回 true。
     * 2）若 没有元素 匹配 给定谓词，则 none() 返回 true
     * 3）若 所有元素 匹配 给到谓词，则 all() 返回 true 。
     *
     * 注：在一个空集合上 使用 任何有效的谓词去调用 all() 都会返回 true。这种行为在逻辑上被称为 vacuous truth .
     *
     * any() 和 none() 不带谓词时，可用于 检查集合是否为空。
     * - 有元素，则 any() 返回 true，none() 返回 false .
     */
    fun demo10() {
        val numbers = listOf("one", "two", "three", "four")

        println(numbers.any { it.endsWith("e") })  // true
        println(numbers.none { it.endsWith("a") }) // true
        println(numbers.all { it.endsWith("e") })  // false

        val emptyList = emptyList<Int>()
        println(emptyList.all { it > 5 }) // true - 空集合调用 all() 均返回 true

        println(numbers.any())   // true
        println(numbers.none())  // false

        println(emptyList.any()) // false
        println(emptyList.none()) // true
    }

    /**
     * 9. plus 与 minus 操作符
     * - 集合操作符：plus(+) 和 minus(-) 操作符。
     * - 第一个操作数是 集合，第二个操作数是 一个元素或另一个集合。返回一个新的 只读集合。
     * - plus  取并集
     * - minus 取差集
     *
     * 备注：map 的 + 与 - 操作。
     */
    fun demo11() {
        val numbers = listOf("one", "two", "three", "four")

        val plusList = numbers + "five"
        val minusList = numbers - listOf("three", "four", "siz")
        println(plusList)  // [one, two, three, four, five]
        println(minusList) // [one, two]
    }

    /**
     * 10. 分组
     * - 扩展函数，对集合元素进行分组。
     *
     * groupBy()
     * - 基本函数 groupBy() 使用一个 lambda 函数并返回一个 Map .
     * key 为 lambda 结果，value 为 返回此结果的元素 List .
     *
     * groupingBy()
     */
    fun demo12() {
        val numbers = listOf("one", "two", "three", "four", "five")

        // {O=[one], T=[two, three], F=[four, five]}
        println(numbers.groupBy { it.first().toUpperCase() })

        // {o=[ONE], t=[TWO, THREE], f=[FOUR, FIVE]}
        println(numbers.groupBy(
            keySelector = { it.first() },
            valueTransform = { it.toUpperCase() }
        ))
    }

    /**
     * 11. 取集合的一部分
     *
     * slice()
     * - 返回具有 给定索引的集合元素列表。索引既可以是作为 区间 传入，也可以是作为 整数值 的几个传入。
     *
     * take()
     * - 从头开始 获取 指定数量 的元素。
     * takeLast()
     * - 从尾开始 获取 指定数量 的元素。
     * takeWhile()
     * - 带谓词的 take()，不停获取元素 直到排除与谓词匹配的首个元素。若首个集合元素与谓词匹配，则结果为空。
     * - takeLastWhile()\dropWhile()\dropLastWhile()
     *
     * 注：当调用的数字 大于集合的大小时，上述两个函数，均返回整个集合。
     *
     * drop() / dropLast()
     * - 从头 或 从尾 去除给定数量的元素。
     *
     * chunked()
     * - 将集合分解为给定大小的 "块"。返回一个一个 List，其中包含 给定大小的 List. List<List> .
     *
     * windowed()
     * - 检索给定大小 的几个元素中所有可能区间。返回一个元素区间列表 List .
     * zipWithNext()
     * - 创建两个元素的窗口。
     *
     */
    fun demo13() {
        // slice() - 切片
        val numbers = listOf("one", "two", "three", "four", "five", "six")
        println(numbers.slice(1..3))        // ["two", "three", "four"]
        println(numbers.slice(0..4 step 2)) // [one, three, five]
        println(numbers.slice(setOf(3, 5, 0)))      // [four, six, one]

        // take()\ drop()
        println(numbers.take(3))
        println(numbers.takeLast(3))
        println(numbers.drop(1))
        println(numbers.dropLast(5))

        // chunked()
        println(numbers.chunked(3))  // [[one, two, three], [four, five, six]]
        println(numbers.chunked(4))  // [[one, two, three, four], [five, six]]
        // 对返回的块 应用转换，使用 lambda 表达式：输出各分块列表元素加总或的结果
        val numbers2 = (0..13).toList()
        println(numbers2.chunked(3) { it.sum() }) // [3, 12, 21, 30, 25]

        // windowed()
        println(numbers.windowed(3)) // [[one, two, three], [two, three, four], [three, four, five], [four, five, six]]

        // zipWithNext()
        println(numbers.zipWithNext()) // [(one, two), (two, three), (three, four), (four, five), (five, six)]
        println(numbers.zipWithNext() { s1, s2 -> s1.length > s2.length }) // [false, false, true, false, true]

    }

    /**
     * 12. 取单个元素
     *
     * 1）按位置取
     *
     * elementAt()
     * - 得到指定位置的集合元素，[0, size - 1]
     * 注：List 一般使用 索引访问操作符 获取元素 （get() 或 []）
     *
     * first() / last()
     * - 第一个和最后一个元素。
     *
     * elementAtOrNull()
     * - 避免 检索位置不存在的元素 越界异常。当越界时，返回 null
     *
     * elementAtOrElse
     * - 当数据越界时，可返回给定值调用 lambda 表达式的结果。
     *
     * 2）按条件取
     * first()
     * last()
     * find() / findOrNull()
     * findLast() / lastOrNull()
     *
     * 3）随机取元素
     * random()
     *
     * 4）检测元素存在
     * contains()
     * containsAll()
     * isEmpty()
     * isNotEmpty()
     */
    fun demo14() {
        // elementAt()
        val numbers = listOf("one", "two", "three", "four", "five", "six")
        println(numbers.elementAtOrNull(5))
        println(numbers.elementAtOrElse(5) { index -> "The value for index $index is undefined" })


    }

    /**
     * 13. 集合排序
     *
     * Kotlin 中可以通过多种方式定义对象的顺序。
     * - 基础 Comparable，实现 compareTo()
     *
     * 1）自然顺序：安装自然顺序 升序和降序排列
     * sorted()
     * sortedDescending()
     *
     * 2）自定义顺序
     * sortedBy()
     * sortedByDescending()
     * sortedWith()
     *
     * 3）倒序
     * reversed()
     * asReversed()
     *
     * 4）随机顺序
     * shuffled()
     *
     */
    fun demo15() {
        println(
            Version(
                1,
                2
            ) > Version(1, 3)
        )
        println(
            Version(
                2,
                0
            ) > Version(1, 5)
        )
    }
    class Version(val major: Int, val minor: Int): Comparable<Version> {
        override fun compareTo(other: Version): Int {
            if (this.major != other.major) {
                return this.major - other.major
            } else if (this.minor != other.minor) {
                return this.minor - other.minor
            } else return 0
        }
    }

    /**
     * 14. 集合聚合操作
     *
     * - min() \ max() 返回最小、最大元素；
     * - average() 返回数字集合中元素的平均值；
     * - sum() 元素总和
     * - count() 元素数量
     *
     * reduce() 和 fold()
     * - 将提供的操作 应用与 集合元素，并返回累积的结果。
     * - 操作参数：先前的累积值 和 集合元素。
     *
     * 区别：
     * - fold() 接受一个初始值并将其作为 第一步的累计值
     * - reduce() 第一步将第一个和第二个元素作为 第一步的操作参数
     */
    fun demo16() {
        val numbers = listOf(5, 2, 10, 4)
        val sum = numbers.reduce { sum, element -> sum + element }
        println(sum)

        val sumDoubled = numbers.fold(0) { sum, element -> sum + element * 2}
        println(sumDoubled)
    }

    /**
     * 15. 集合写操作
     *
     * add()
     * addAll()
     * +
     *
     * remove()
     * removeAll()
     * clear()
     * -
     */
    fun demo17() {

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = CollectionOperationDemo01()

            d.demo01()

            d.demo02()

            d.demo03()

            d.demo04()

            d.demo05()

            d.demo06()

            d.demo07()

            d.demo08()
            d.demo09()
            d.demo10()
            d.demo11()
            d.demo12()
            d.demo13()
            d.demo14()
            d.demo15()
            d.demo16()
            d.demo17()
        }
    }
}
