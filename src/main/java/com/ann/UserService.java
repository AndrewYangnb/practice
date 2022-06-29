package com.ann;


import org.springframework.stereotype.Component;

@Component
public class UserService {
    @MetricTime("user")
    public void user() {
        System.out.println("Hello, " + "Bob");
    }
    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.user();
    }
}
