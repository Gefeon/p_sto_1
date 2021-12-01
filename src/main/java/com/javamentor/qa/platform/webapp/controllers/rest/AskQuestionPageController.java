package com.javamentor.qa.platform.webapp.controllers.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/question")
public class AskQuestionPageController {

    @GetMapping("/ask")
    public String askQuestion() {
        return "askQuestion";
    }
}