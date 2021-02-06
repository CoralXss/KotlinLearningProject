package com.coral.kotlin.chapter4

import com.coral.kotlin.People
import com.coral.kotlin.Person
import java.util.*
import kotlin.collections.HashSet

class CollectionDemo01 {

    // 一、集合概述与类型

    /**
     * 1. Collection
     * - Collection<T> 是集合层次结构的根。
     * - 该接口表示一个「只读集合」的共同行为：检索大小、检测是否称为成员等。
     * - Collection 继承自 Iterable<T> 接口，定义了 迭代元素 的操作。
     * - 可使用 Collection 作为适用于 不同集合类型 的函数的参数。
     */
    fun demo01(strings: Collection<String>) {
        for (s in strings) print("$s ")
        println()
    }

    // 可变集合(带有接收者的函数字面值)
    fun List<String>.demo02(words: MutableList<String>, maxLen: Int) {
        this.filterTo(words) {
            it.length <= maxLen
        }
        val articles = setOf("a", "A", "an", "An", "the", "The")
        words -= articles
        // 过滤并筛选 不包含某些特定字符 && 低于特定长度的字符列表
    }

    fun callDemo02() {
        val words = "A long time ago in a galaxy far far away".split(" ")
        val shortWords = mutableListOf<String>()
        words.demo02(shortWords, 3)
        println(shortWords)  // [ago, in, far, far]
    }

    /**
     * 2. List
     * - List<T> 以指定的顺序存储元素，并使用索引访问元素，区间 [0, list.size - 1]
     * - 若两个 List 在相同的位置 具有 相同大小和相同结构，则认为是相等的。（然打印并不相等！！！）
     * - 默认实现 ArrayList .
     *
     * 数组 Array vs List
     * - 数组的大小是在初始化时定义的，永远不会改变；
     * - List 没有预定义的大小；作为写操作（增删改）的结果，List 大小可以改变。
     */
    fun demo03() {
        val bob = Person("Bob", 31)
        val people = listOf(Person("Adam", 20), bob, bob)
        val people2 = listOf(Person("Adam", 20), Person("Bob", 31), bob)
        println(people == people2)  // false

        bob.age = 32
        println(people == people2)  // false

        // 注：上述均为 只读列表，不能操作列表，虽然能改变列表中 item 的属性值。

        // 可写操作的 List，MutableLList<T>
        val numbers = mutableListOf(1, 2, 3, 4)
        numbers.add(5)
        numbers.removeAt(1)
        numbers[0] = 0
        numbers.shuffle()
        println(numbers)  // [0, 3, 4, 5]
    }

    /**
     * 3. Set
     * - 存储唯一的元素；顺序不固定。null 也是唯一的。
     * - set 大小相同 && 一个 set 中的元素都能在另一个 set 中相同，则两个 set 相等。
     * - 默认实现 - LinkedHashSet (保留元素插入的顺序，因此，依赖于顺序的函数，first() 和 last())
     * - 另一种实现 - HashSet (不声明元素的顺序，所以上述函数结果不可预测。优点：元素数量相同时，HashSet 使用的内存更少。)
     */
    fun demo04() {
        val numbers = setOf(1, 2, 3, 4)
        println("Numbers of elements: ${numbers.size}")
        if (numbers.contains(1)) {
            println("1 is in the set")
        }

        val numbersBackwards = setOf(4, 3, 2, 1)
        println("The sets are equal: ${numbers == numbersBackwards}")  // true

        // 可写操作的 Set, MutableSet
        println(numbers.first() == numbersBackwards.first())  // false
        println(numbers.first() == numbersBackwards.last())   // true
    }

    /**
     * 4. Map
     * - Map<K, V> 不是 Collection 接口的子类；但也是一种集合类型。
     * - 无论键值对的顺序如何，包含相同键值对的两个 Map 是相等的。
     * - 默认实现 - LinkedHashMap（保留元素插入的顺序）
     * - 另一种实现 - HashMap (不声明元素的顺序)
     */
    fun demo05() {
        val numbersMap = mapOf("k1" to 1, "k2" to 2, "k3" to 3, "k4" to 1)
        println("All keys: ${numbersMap.keys}")
        println("All values: ${numbersMap.values}")

        if ("k2" in numbersMap) {
            println("Value by \"k2\": ${numbersMap["k2"]}")  // 通过键值取值
        }
        if (1 in numbersMap.values) {
            println("The value 1 is in the map")
        }
        if (numbersMap.containsValue(1)) {
            println("The value 1 is in the map")
        }

        // 值相同的 map 是相等的，无论键值对的顺序是否一致
        val numbersMap2 = mapOf("k2" to 2, "k3" to 3, "k1" to 1, "k4" to 1)
        println(numbersMap == numbersMap2)  // true
    }

    // 二、构造集合

    /**
     * 1. 由元素构造
     * - 创建集合的最常用方法是 使用标准库函数 listOf<T>()、setOf<T>()、mutableListOf<T>()、mutableSetOf<T>()。
     * 若以逗号分隔的集合元素列表作为参数，编译器会自动检测元素类型。创建空集合时，须明确指定类型。
     *
     * - 同样地，Map 也有函数：mapOf() 和 mutableMapOf()。映射的键和值作为 Pair 对象传递
     * （通常使用 中缀函数 to 创建）。
     * Tips：
     * - to 符号创建了一个短时存活的 Pair 对象，因此建议仅在 性能不重要时 才使用。
     * - 为避免过多的内存使用，可创建 可写 Map 并使用写入操作填充：apply() 函数可保持 流畅地初始化。
     *
     * 2. 空集合
     * - 没有任何元素的集合函数：emptyList()、emptySet()、emptyMap()
     * - 创建集合时，应指定集合将包含的元素类型。
     *
     * 3. list 的初始化函数
     * - 有一个 接收 List 大小与 初始化函数的 构造函数，该初始化函数，根据 索引定义元素的值。
     *
     * 4. 具体类型构造函数
     * - 要创建具体类型的集合，如 ArrayList 或 LinkedList，可以使用这些类型的构造函数。
     * 类似的构造函数对于 Set 与 Map 的各实现中均有提供。
     *
     * 5. 复制
     * - 标准库中的 集合复制为 浅复制，创建了具有相同元素所有的集合，因此，对集合元素改变，会反映在所有的副本中。
     * - 通过复制集合函数，如 toList()、toMutableList()、toSet() 等，创建了集合的快照。
     * 结果是：创建了一个具有相同元素的新集合，若在源集中变更元素，则不会影响副本。副本的修改也不会影响源集。
     *
     * 6. 迭代器
     * - 对于遍历集合元素，kotlin 标准库支持 迭代器 的常用机制 —— 对象 可按 顺序 提供对元素的访问权限，
     * 而不会暴露集合的底层结果。
     * - 当需要 逐个处理集合的所有元素时，迭代器非常有用。
     *
     * Iterable<T> 接口的继承者（Set 与 List）可通过 iterator() 函数获得 迭代器。
     * - 一旦获取，迭代器就指向集合的第一个元素；调用 next() 函数将返回此元素，并将迭代器指向下一个元素。
     * - 一旦 迭代器 通过了 最后一个元素，就不能再用于检索元素；也无法重新指向到以前的任何位置。
     * 要再次遍历集合，需要重新创建一个迭代器。
     */
    fun demo06() {
        val numberSet = setOf("one", "two", "three", "four")
        val emptySet = mutableSetOf<String>()

        // 使用 apply() 函数构造 map
        val optimizeMap = mutableMapOf<String, String>().apply { this["one"] = "1"; this["two"] = "2" }

        // 2. 空集合，要指定集合将包含的元素类型
        val emptyList = emptyList<String>()
        val emptySet2 = emptySet<String>()
        val emptyMap = emptyMap<String, String>()

        // 3. list 的初始化函数
        val doubled = List(3) { it * 2 } // = List(3, { it * 2 })
        println("doubled = $doubled") // [0, 2, 4]

        // 4. 创建具体类型构造函数
        val linkedList = LinkedList<String>(listOf("one", "two", "three"))
        val presizedSet = HashSet<Int>(32)

        // 5. 复制
        val sourceList = mutableListOf(1, 2, 3)
        val copyList = sourceList.toMutableList()  // 构建新的集合，副本与源集相互不影响
        val readOnlyCopyList = sourceList.toList()

        sourceList.add(4)
        println("Copy size: ${copyList.size}")  // 3
//        readOnlyCopyList.add(4) // 编译异常
        println("Read-only copy size: ${readOnlyCopyList.size}")  // 3

        // 将 List 转成 Set
        val copySet = sourceList.toMutableSet()
        copySet.add(3)
        copySet.add(4)
        println("copySet = $copySet")  // copySet = [1, 2, 3, 4]

        // 浅拷贝，直接赋值，将穿件新的引用
        val referenceList = sourceList
        referenceList.add(4)
        println("sourceList = ${sourceList.size}")  // sourceList = 5

        // referenceList vs referenceList2，集合的初始化可用于限制其 可变性。
        // 若构建了一个 MutableList 的 List 引用，当修改该引用时，会报错，因为 List 是不可变的。
        val referenceList2: List<Int> = sourceList
//        referenceList2.add(4)  // 编译错误

        // 6. 调用集合函数 构建集合：
        val numbers = listOf("one", "two", "three")

        // 如「过滤」 列表会创建与过滤器匹配的 新元素列表：
        val longerThan3Numbers = numbers.filter { it.length > 3 }
        println(longerThan3Numbers)  // [three]

        // 如「映射」生成转换结果 列表
        val numberSet2 = setOf(1, 2, 3)
        val convertNumbers = numberSet2.map { it * 3 }
        val convertNumbers2 = numberSet2.mapIndexed { idx, value -> value * idx }
        println("convertNumbers = $convertNumbers")    // [3, 6, 9]
        println("convertNumbers2 = $convertNumbers2")  // [0, 2, 6]

        // 如「关联」生成 Map
        println(numbers.associateWith { it.length })  // {one=3, two=3, three=5}

        // 7. 迭代器
        val numbersIterator = numbers.iterator()
        while (numbersIterator.hasNext()) {
            println(numbersIterator.next())
        }

        // for 循环，隐式获取迭代器
        for (item in numbers) {
            println(item)
        }

        // forEach() 函数，可自动迭代集合，并为每个元素执行给定的代码
        numbers.forEach {
            println(it)
        }

        // 8. List 迭代器 —— ListIterator，支持双向迭代：正向与反向，如此在到达最后一个元素后仍可以使用。
        val listIterator = numbers.listIterator()
        while (listIterator.hasNext())
            listIterator.next()
        println("Iterating backwards: ")
        while (listIterator.hasPrevious()) {
            print("Index: ${listIterator.previousIndex()}")
            println(", ${listIterator.previous()}")
        }

        // 9. 可变迭代器 —— MutableIterator，具有 remove() 函数，在迭代时从集合中删除元素。
        val mutableNumbers = mutableListOf("one", "two", "three")
        val mutableIterator = mutableNumbers.iterator()
        mutableIterator.next()
        mutableIterator.remove()  // 只有删除，没有 add 方法
        println("After removal: $mutableNumbers")  // After removal: [two, three]

        // 使用 MutableListIterator 可在迭代列表时 add 和 update 元素。
        val mutableListIterator = mutableNumbers.listIterator()
        mutableListIterator.next()
        mutableListIterator.add("two")
        mutableListIterator.next()
        mutableListIterator.set("threeV2")
        println("$mutableNumbers")  // [two, two, threeV2]
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val c = CollectionDemo01()

            // demo01
            var stringList = listOf("one", "two", "three")
            c.demo01(stringList)

            var stringSet = setOf("one", "two", "three")
            c.demo01(stringSet)

            // demo02 可变集合
            c.callDemo02()

            c.demo03()

            c.demo04()

            c.demo05()

            c.demo06()

            val oddNumLessThan10 = generateSequence(1) {
                if (it + 2 < 10) {
                    it + 2
                } else {
                    null
                }
            }

            oddNumLessThan10.forEach {
                println(it)  // 1,3,5,7,9
            }
            println(oddNumLessThan10.count())
        }
    }
}