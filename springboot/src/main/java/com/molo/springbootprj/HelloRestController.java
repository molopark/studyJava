package com.molo.springbootprj;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {
    
    @RequestMapping("/call")
    public String index(){
        return "Hello, Spring Boot!";
    }
}
