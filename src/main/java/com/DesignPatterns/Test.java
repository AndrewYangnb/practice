package com.DesignPatterns;

public class Test {
    public static void main(String[] args) {
        Computer computer = new Computer.Builder().basicInfo("i5", "三星").setDisplay("AOC").build();
        System.out.println(computer.cpu);
        System.out.println(computer.ram);
        System.out.println(computer.display);

    }
}
