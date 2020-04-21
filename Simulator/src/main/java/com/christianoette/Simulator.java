package com.christianoette;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = Simulator.class)
public class Simulator {

    public static void main(String[] args) {
        SpringApplication.run(Simulator.class);
    }
}
