package com.example;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class Stream {
    public static final Logger logger = LogManager.getLogger(Stream.class.getName());

    public static void main(String[] args) {
        List<String> list = Arrays.asList("abc1", "abc2", "abc3");
        list.stream().skip(1)
                .map(element -> element.substring(0, 3))
                .forEach(System.out::println);

        Optional<String> stream = list.stream().filter(element -> {
            logger.info("filter() was called");
            return element.contains("2");
        }).map(element -> {
            logger.info("map() was called");
            return element.toUpperCase();
        }).findFirst();

        logger.info(stream);

        String listToString = list.stream().collect(Collectors.joining(",", "[", "]"));

        logger.info(listToString);
//        System.out.println(size);
    }
}
