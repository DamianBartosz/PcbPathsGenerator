package com.example.pcbgenerator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Metoda zwraca stronę zawierającą opis usługi z możliwością jej wypróbowania.
     */
    @GetMapping("")
    public String index() {
        return "index";
    }
}