package com.example.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final static RateLimiter RATE_LIMITER = RateLimiter.create(5.0);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        // BUG: name is assumed to never be null, but no validation is done
        return "Hello, " + name.toUpperCase() + "!";
    }

    @GetMapping("/limited-endpoint")
    public ResponseEntity<String> limitedEndpoint() {
        boolean allowed = RATE_LIMITER.tryAcquire(1, TimeUnit.MILLISECONDS); // non-blocking

        if (!allowed) {
            return ResponseEntity
                    .status(429)
                    .body("Too Many Requests - try again later.");
        }

        // Proceed with normal logic
        return ResponseEntity.ok("Request successful!");
    }

    @PostMapping("/parse-json")
    public ResponseEntity<String> parseJson(@RequestBody String jsonString) {
        try {
            Person person = objectMapper.readValue(jsonString, Person.class);
            return ResponseEntity.ok("Parsed person: " + person.name() + ", age: " + person.age());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON: " + e.getMessage());
        }
    }
}
