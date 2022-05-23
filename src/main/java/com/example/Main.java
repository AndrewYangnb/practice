package com.example;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.tuple.*;


import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");

        Set<Integer> set = new HashSet<>();
        Multiset<Integer> set1 = HashMultiset.create();
        for (int i = 0; i < 6; i++) {
            set.add(i);
            set1.add(i);
        }
        System.out.println(set);
        System.out.println(set1);

        // Array
        String[] s = new String[4];
        s[0] = "dede";
        s[1] = "ded";
        s[2] = "a";
        for (String value : s) {
            System.out.print(value + " ");
        }

        // List
        System.out.println();
        List<Integer> list = List.of(0, 1, 2);
        List<String> strings = new ArrayList<>();
        strings.add("e");
        strings.add("a");
        System.out.println(list);
        System.out.println(strings);

        final ImmutableSet<Color> GOOGLE_COLORS =
                ImmutableSet.<Color>builder()
                        .add(new Color(0, 191, 255))
                        .build();
        System.out.println(GOOGLE_COLORS);

        Pair<String,String> pair = Pair.of("left", "right");
        System.out.println(pair);
        System.out.println(pair.getLeft());
        System.out.println(pair.getRight());

    }
}
