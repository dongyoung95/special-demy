package com.specialdemy.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicPageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/instructors")
    public String instructors() {
        return "instructors";
    }

    @GetMapping("/programs")
    public String programs() {
        return "programs";
    }

    @GetMapping("/reviews")
    public String reviews() {
        return "reviews";
    }

    @GetMapping("/contents")
    public String contents() {
        return "contents";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }
}
