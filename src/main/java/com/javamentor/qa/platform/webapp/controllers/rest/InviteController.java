package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.user.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import com.javamentor.qa.platform.service.util.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.concurrent.ThreadLocalRandom.current;

@RestController
@RequestMapping("api/invite")
@RequiredArgsConstructor
public class InviteController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @PostMapping("/{email}")
    public ResponseEntity<?> invite(@NotBlank @PathVariable String email) {
        Optional<Role> roleOpt = roleService.findRoleByName("ROLE_USER");
        if (roleOpt.isPresent()) {
            User user = new User();
            user.setEmail(email);
            String password = generatePassword();
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(roleOpt.get());
            userService.persist(user);

            mailService.send(email, "Приглашение на StackOverflow by JavaMentor",
                    "Вам пришло приглашение присоединиться к сообществу StackOverflow. \n" +
                            "Мы вас уже зарегистрировали. Ваши регистрационные данные: \n" +
                            "логин: " + email + "\n" +
                            "пароль: " + password + "\n" +
                            "Осталось только пройти по ссылке <ссылка>, и начать пользоваться сайтом!");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(500).body("User cannot be created");
    }

    private String generatePassword() {
        String signs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ0123456789/][?><:}{~!@#$%^&*()_+=-";
        return Arrays.stream(signs.split(""))
                .map(s -> signs.charAt(current().nextInt(signs.length() - 1)))
                .limit(15)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
