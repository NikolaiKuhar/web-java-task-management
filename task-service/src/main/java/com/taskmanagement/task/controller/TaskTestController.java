package com.taskmanagement.task.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskTestController {

    @GetMapping("/tasks/hello")
    public String hello() {
        return "Task service is running";
    }
}