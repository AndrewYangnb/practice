package com.example;

import java.io.Console;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexTestHarness {

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Enter your regex: ");
            Pattern pattern =
                    Pattern.compile(scanner.nextLine(),
                    Pattern.CASE_INSENSITIVE);

            System.out.println("Enter input string to search: ");
            Matcher matcher =
                    pattern.matcher(scanner.nextLine());

            boolean found = false;
            while (matcher.find()) {
                System.out.printf("I found the text" +
                                " \"%s\" starting at " +
                                "index %d and ending at index %d.%n",
                        matcher.group(),
                        matcher.start(),
                        matcher.end());
                found = true;
            }
            if(!found){
                System.out.println("No match found.");
            }
        }
    }
}
