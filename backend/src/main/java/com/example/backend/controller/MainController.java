package com.example.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /**
     * 메인페이지로 이동
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/gallery")
    public String showGalleryPage() {
        return "gallery";
    }
}
