package com.patterns;

import java.util.Iterator;

public class Test {

    public static void main(String[] args) {
        Class_Iterator Class = new Class_Iterator();
        Iterator<Student> iterator = Class.iterator();

        while (iterator.hasNext()){
            System.out.println(iterator.next().name);
        }

        for (Student aClass : Class) {
            System.out.println(aClass.age);
            System.out.println(aClass.name);
        }
    }

}
