package com.coral.kotlin.chapter2;

public class GenericJavaDemo01 {

    interface Source<T> {
        T nextT();
    }

    void demo(Source<String> strs) {
//        Source<Object> objexts = strs;  // !!! 编译器报错
    }
}
