package com.demo.admin.demo;

import java.math.BigDecimal;

/**
 * 独立测试类，用来练习 Java 基础语法。
 * 不影响 Spring Boot 业务代码。
 *
 * 运行方式：
 * 1. 打开本文件，点击 main 方法上方的 Run
 * 2. 或终端：mvn -q exec:java -Dexec.mainClass="com.demo.admin.demo.HelloWorld"
 */
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Hello, Java!");

        String keyword = "admin";
        System.out.println("keyword123 = " + keyword);
        System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));

        // add 有返回值，可以接住再用
        int sum = add(1, 2);
        System.out.println("sum = " + sum);
    }

    public static int add(int a, int b) {
        return a + b;
    }
}
