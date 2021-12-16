package com.javamentor.qa.platform.service.util;

import com.javamentor.qa.platform.exception.InviteUserException;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.user.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.concurrent.ThreadLocalRandom.current;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    public void invite(String email) {
        if (userService.findByEmail(email).isEmpty()) {
            Optional<Role> roleOpt = roleService.findRoleByName("ROLE_USER");
            User user = new User();
            user.setEmail(email);
            String password = generatePassword();
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(roleOpt.orElseThrow(NoSuchElementException::new));
            userService.persist(user);

            mailService.send(email, "Приглашение на StackOverflow by JavaMentor",
                    "Вам пришло приглашение присоединиться к сообществу StackOverflow by JavaMentor. \n" +
                            "Мы вас уже зарегистрировали. Ваши регистрационные данные: \n" +
                            "логин: " + email + "\n" +
                            "пароль: " + password + "\n" +
                            "Осталось только пройти по ссылке <ссылка>, ввести свои регистрационные данные и начать пользоваться сайтом!");
            return;
        }
        throw new InviteUserException("User with this email is already exist");
    }

     public String generatePassword() {
        final String signs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ0123456789/][?><:}{~!@#$%^&*()_+=-";
        return Arrays.stream(signs.split(""))
                .map(s -> signs.charAt(current().nextInt(signs.length() - 1)))
                .limit(15)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
