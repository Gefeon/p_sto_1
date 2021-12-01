package com.javamentor.qa.platform.webapp.controllers.example;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.example.ExampleControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExampleController {

    @Autowired
    private ExampleControllerService ecService;

    @GetMapping("/listRoles")
    public List<Role> getListOfRoles() {
        return ecService.getListOfRoles();
    }

    @GetMapping("/listUsers")
    public List<User> getListOfUsers() {
        return ecService.getListOfUsers();
    }
}
