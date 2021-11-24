package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class QuestionResourceController {



    public QuestionResourceController() {
    }

    @PostMapping("/question")
    public QuestionDto getUserDto(@RequestBody QuestionCreateDto questionCreateDto) {
        return null;
    }
}
