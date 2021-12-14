package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.util.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/invite")
@RequiredArgsConstructor
public class InviteController {

    private final MailService mailService;

    @PostMapping
    public ResponseEntity<?> invite() {
        mailService.send("alexeyfaker@gmail.com", "Invite link to JM StackOverflow",
                "Welcome to our little community! You can log in via link we attached to this message. You are now " +
                        "added to database. Enjoy with new possibilities and experience of using StackOverflow by JavaMentor.");

        return ResponseEntity.ok("Cool");
    }
}
