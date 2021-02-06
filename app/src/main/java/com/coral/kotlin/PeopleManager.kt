package com.coral.kotlin

import java.util.ArrayList

class PeopleManager {

    private val peoples = ArrayList<People>()

    fun add(people: People) {
        if (!peoples.contains(people)) {
            peoples.add(people)
        }
    }

    companion object {  // static 区块：静态函数、静态属性

        private var sInstance: PeopleManager? = null

        // 顶层属性 访问 或 调用  ::
        private val TAG = PeopleManager::class.java.name

        val instance: PeopleManager
            get() {
//                if (sInstance == null) {
//                    sInstance = PeopleManager()
//                }
                return PeopleManager()
            }

        fun printAll() {

        }
    }
}
