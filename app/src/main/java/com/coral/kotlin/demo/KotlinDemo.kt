package com.coral.kotlin.demo

import java.util.ArrayList

class KotlinDemo {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val user = User()
            user.name = "cc"
            user.age = 24
            user.address = "Shanghai"

            UserManager.getInstance().addUser(user)
            UserManager.getInstance().addUser(User("kk", 25))
        }
    }

    class UserManager private constructor() {

        private val users = ArrayList<User>()

        fun addUser(user: User) {
            if (!users.contains(user)) {
                users.add(user)
            }
        }

        fun addUsers(users: List<User>) {
            if (!this.users.containsAll(users)) {
                this.users.addAll(users)
            }
        }

        fun removeUser(user: User) {
            users.remove(user)
        }

        fun removeByIndex(index: Int) {
            users.removeAt(index)
        }

        fun clear() {
            users.clear()
        }

        companion object {
            // 单例模式
            private var sInstance: UserManager? = null
                get() {
                    return field ?: UserManager()
                }

            @JvmStatic
            @Synchronized
            fun getInstance(): UserManager {
                // synchronized(UserManager::class.java)
                return requireNotNull(sInstance)
            }
        }
    }


    class User {
        var name: String? = null

        var age: Int = 0

        var address: String? = null

        constructor() {}

        constructor(name: String, age: Int) {
            this.name = name
            this.age = age
        }
    }
}