package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class ReadAndWrite {

    public static void main(String[] args) throws IOException {
//        BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
//
//        int i = Integer.parseInt(buffReader.readLine());
//
//        System.out.println(i);

        Scanner scanner = new Scanner(System.in);
//
//        String gender = scanner.next();
//
//        System.out.println(gender);

        System.out.println(scanner.hasNext(Pattern.compile("www.baeldung.com")));
    }

}

