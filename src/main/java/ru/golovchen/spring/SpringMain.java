package ru.golovchen.spring;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.golovchen.spring.examples.MainExampleService;

public class SpringMain {
    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        MainExampleService mainExampleService = context.getBean(MainExampleService.class);
        context.close();
    }
}
