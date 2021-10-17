package ru.golovchen.spring;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.golovchen.spring.examples.CianParserService;

public class SpringMain {
    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        CianParserService cianParserService = context.getBean(CianParserService.class);
        context.close();
    }
}
