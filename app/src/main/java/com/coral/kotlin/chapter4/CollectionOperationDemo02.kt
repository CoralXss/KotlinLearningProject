package com.coral.kotlin.chapter4

/**
 * 承接 CollectionOperationDemo01
 */
class CollectionOperationDemo02 {

    /**
     * 16. List 相关操作
     *
     * 1）按索引取元素
     * elementAt()
     * first()
     * last()
     * get(index)
     * list[index]
     *
     * 注：避免索引取值越界，使用以下函数避免异常：
     * getOrElse()
     * getOrNull()
     *
     * 2）取列表的一部分
     * subList()
     *
     * 3）查找元素
     * indexOf()
     * lastIndexOf()
     * binarySearch()
     *
     * add()
     * addAll()
     * set()
     * fill() - 将所有集合元素替换为指定值。
     *
     * removeAt()
     * removeFirst()
     * removeLast()
     */
    fun demo18() {
        val numbers = listOf(1, 2, 3, 4)
        println(numbers.get(0))
        println(numbers.getOrNull(5))       // null
        println(numbers.getOrElse(5, { it })) // 5

        val n2 = mutableListOf(1, 2, 3, 4)
        n2.fill(3)    // 所有集合元素替换为 3 : [3, 3, 3, 3]
        println(n2)
    }

    /**
     * 17. List 写操作
     *
     */
    fun demo19() {

    }

    /**
     * 18. Set 相关操作
     *
     * 常用操作：查找交集、并集 或 差集。
     *
     * - 并集：union()。 均有中缀形式 a union b
     * - 交集：intersect()
     * - 差集：subtract()
     */
    fun demo20() {

    }

    /**
     * 19. Map 相关操作
     *
     * put()
     * putAll()
     *
     * [key]
     * remove
     *
     * .keys
     * .values
     *
     * +
     * -
     *
     */
    fun demo21() {

    }

    /**
     * 20. Map 写操作
     *
     */
    fun demo22() {

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val d = CollectionOperationDemo02()

            d.demo18()

            d.demo19()

            d.demo20()

            d.demo21()

            d.demo22()
        }
    }
}
