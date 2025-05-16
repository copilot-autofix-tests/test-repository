package com.example.demo;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        // BUG: name is assumed to never be null, but no validation is done
        return "Hello, " + name.toUpperCase() + "!";
    }
}
