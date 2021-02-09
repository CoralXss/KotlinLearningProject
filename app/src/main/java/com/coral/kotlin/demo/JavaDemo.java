package com.coral.kotlin.demo;

import java.util.ArrayList;
import java.util.List;

public class JavaDemo {

    public static void main(String[] args) {
        User user = new User();
        user.name = "cc";
        user.age = 24;
        user.address = "Shanghai";

        UserManager instance = UserManager.getInstance();

        UserManager.getInstance().addUser(user);
        UserManager.getInstance().addUser(new User("kk", 25));
    }

    public static class UserManager {

        private static UserManager sInstance;

        private List<User> users = new ArrayList<>();

        public static UserManager getInstance() {
            if (sInstance == null) {
                synchronized(UserManager.class) {
                    if (sInstance == null) {
                        sInstance = new UserManager();
                    }
                }
            }
            return sInstance;
        }

        private UserManager() {}

        public void addUser(User user) {
            if (!users.contains(user)) {
                users.add(user);
            }
        }

        public void addUsers(List<User> users) {
            if (!this.users.containsAll(users)) {
                this.users.addAll(users);
            }
        }

        public void removeUser(User user) {
            users.remove(user);
        }

        public void removeByIndex(int index) {
            users.remove(index);
        }

        public void clear() {
            users.clear();
        }
    }


    public static class User {
        private String name;

        private int age;

        private String address;

        public User() {}

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
