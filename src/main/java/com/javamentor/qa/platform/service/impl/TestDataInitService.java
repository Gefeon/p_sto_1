package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.impl.model.RoleServiceImpl;
import com.javamentor.qa.platform.service.impl.model.UserServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@NoArgsConstructor
public class TestDataInitService {
    //Amount of test data
    private final static int usersNum = 10;
    private final static int rolesNum = 4;

    //static fields for random values
    private final Character[] alphabet = "abcdefghijklmnopqrstuvwxyz"
            .chars()
            .mapToObj(c -> (char) c).toArray(Character[]::new);

    private final String[] firstNames = new String[]{
            "Harry", "Ross", "Bruce", "Cook", "Carolyn", "Morgan", "Albert",
            "Walker", "Randy", "Reed", "Larry", "Barnes", "Lois", "Wilson",
            "Jesse", "Campbell", "Ernest", "Rogers", "Theresa", "Patterson",
            "Henry", "Simmons", "Michelle", "Perry", "Frank", "Butler", "Shirley"};

    private final String[] middleNames = new String[]{
            "Brooks", "Rachel", "Edwards", "Christopher", "Perez", "Thomas",
            "Baker", "Sara", "Moore", "Chris", "Bailey", "Roger", "Johnson",
            "Marilyn", "Thompson", "Anthony", "Evans", "Julie", "Hall",
            "Paula", "Phillips", "Annie", "Hernandez", "Dorothy", "Murphy",
            "Alice", "Howard"};

    private final String[] lastNames = new String[]{
            "Ruth", "Jackson", "Debra", "Allen", "Gerald", "Harris", "Raymond",
            "Carter", "Jacqueline", "Torres", "Joseph", "Nelson", "Carlos",
            "Sanchez", "Ralph", "Clark", "Jean", "Alexander", "Stephen", "Roberts",
            "Eric", "Long", "Amanda", "Scott", "Teresa", "Diaz", "Wanda", "Thomas"};

    private final String[] domains = new String[]{
            "mail", "email", "gmail", "vk", "msn", "yandex", "yahoo", "edu.spbstu", "swebhosting"};

    private final String[] domainCodes = new String[]{
            "ru", "com", "biz", "info", "net", "su", "org"};

    private final String[] cities = new String[]{
            "Saint-Petersburg", "Moscow", "Vologda", "Volgograd", "Murmansk", "Vladivostok", "Novgorod", "Tula"};

    private final String[] abouts = new String[]{
            "student", "dentist", "engineer", "social worker", "nurse", "doctor"};

    private final String[] roles = new String[]{
            "ADMIN", "USER", "ANONYMOUS", "GUEST", "UNDEFINED", "MAIN"};


    @Getter
    @Setter
    private UserServiceImpl userService;
    @Getter
    @Setter
    private RoleServiceImpl roleService;

    public TestDataInitService(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //fill related tables user_entity and role with test data
    public void fillTableWithTestData() {
        roleService.persistAll(getRandomRoles());
        List<Role> existingRoles = roleService.getAll();
        Set<User> usersToPersist = getRandomUsers();
        for (User user : usersToPersist) {
            user.setRole(existingRoles.get(getRandInt(0, existingRoles.size())));
        }
        userService.persistAll(usersToPersist);
    }

    //generates rolesNum roles with random fields
    private Set<Role> getRandomRoles() {
        Set<Role> testRoles = new HashSet<>();
        while (testRoles.size() < rolesNum - 1) {
            testRoles.add(new Role(getRand(roles)));
        }
        return testRoles;
    }

    //generates usersNum users with random fields(all necessary without role)
    private Set<User> getRandomUsers() {
        Set<User> users = new HashSet<>();

        for (int i = 0; i < usersNum; i++) {
            String email = getRand(firstNames).toLowerCase() + "@" +
                    getRand(domains) + "." + getRand(domainCodes);
            String fullName = getRand(firstNames) + " " +
                    getRand(middleNames) + " " + getRand(lastNames);
            String password = getRandStr(4, 20);
            String city = getRand(cities);
            String linkSite = "https://" + getRandStr(10, 50);
            String linkGithub = "https://" + getRandStr(5, 30);
            String linkVk = "https://vk.com/" + getRandStr(1, 30);
            String about = getRand(abouts);
            String imageLink = getRandStr(10, 100);
            String nickname = fullName.substring(0, 3);

            users.add(new User(email, password, fullName, city,
                    linkSite, linkGithub, linkVk, about, imageLink, nickname));
        }
        return users;
    }

    private static int getRandInt(int lowBound, int upperBound) {
        return ThreadLocalRandom.current().nextInt(lowBound, upperBound);
    }

    private static <T> T getRand(T[] array) {
        return array[ThreadLocalRandom.current().nextInt(0, array.length)];
    }

    private String getRandStr(int lowBound, int upperBound) {
        StringBuilder stringBuilder = new StringBuilder();
        int strLength = getRandInt(lowBound, upperBound);
        for (int i = 0; i < strLength; i++) {
            stringBuilder.append(getRand(alphabet));
        }
        return stringBuilder.toString();
    }
}
