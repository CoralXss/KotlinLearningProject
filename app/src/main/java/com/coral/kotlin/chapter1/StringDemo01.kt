package com.coral.kotlin.chapter1

class StringDemo01 {

    // main 入口方法的写法
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            // 1. 不可变
            val s: String = "Hello"
            // 2. 支持索引运算符访问
            println(s[0])

            // 3. 支持 for 循环迭代字符串
            for (c in s) {
                println(c)
            }

            s.forEach { println(it) }

            // 4. 支持 + 操作符连接字符串，拼接的数据可以是其他类型，
            /**
             * 注意：
             * - 拼接表达式的第一个元素必须是字符串，换位置就会报错
             * - 大多数情况下，优先使用 字符串模板 或 原始字符串 而不是字符串连接
             */
            val s1 = "abc" + 1
            println(s1 + "def")

            // 5. 字符串字面值: 可以包含转义字符
            val s2 = "Hello, world!\n"

            // 6. 原始字符串写法：三个引号（"""） 分界符括起来，内部无转义 && 包括换行 + 任何其它字符
            val text = """
                for (c in "foo")
                    println(c)
            """.trimIndent()
            println(text)

            // 7. 字符串模板(变量表达式)
            val i = 10
            println("i = $i")

            val s3 = "abc"
            println("$s3.length is ${s3.length}")

            // 注：元素字符串 与 转义字符串内部都支持模板
            val price = """
                ${'$'}9.99
            """.trimIndent()

        }
    }
}