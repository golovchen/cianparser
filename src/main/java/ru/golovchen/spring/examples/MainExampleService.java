package ru.golovchen.spring.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainExampleService {
    @Autowired
    private HelperService helperService;
}
