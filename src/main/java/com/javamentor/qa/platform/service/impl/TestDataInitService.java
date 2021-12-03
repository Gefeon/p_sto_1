package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.question.TagService;
import com.javamentor.qa.platform.service.abstracts.model.user.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TestDataInitService {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    //Amount of test data
    private final static int usersNum = 10;
    private final static int rolesNum = 7;
    private final static int answersNum = 10;
    private final static int questionsNum = 10;
    private final static int tagsNum = 4;

    //static fields for random values
    private static final Character[] alphabet = "abcdefghijklmnopqrstuvwxyz"
            .chars()
            .mapToObj(c -> (char) c).toArray(Character[]::new);

    private static final String[] firstNames = new String[]{
            "Harry", "Ross", "Bruce", "Cook", "Carolyn", "Morgan", "Albert",
            "Walker", "Randy", "Reed", "Larry", "Barnes", "Lois", "Wilson",
            "Jesse", "Campbell", "Ernest", "Rogers", "Theresa", "Patterson",
            "Henry", "Simmons", "Michelle", "Perry", "Frank", "Butler", "Shirley"};

    private static final String[] middleNames = new String[]{
            "Brooks", "Rachel", "Edwards", "Christopher", "Perez", "Thomas",
            "Baker", "Sara", "Moore", "Chris", "Bailey", "Roger", "Johnson",
            "Marilyn", "Thompson", "Anthony", "Evans", "Julie", "Hall",
            "Paula", "Phillips", "Annie", "Hernandez", "Dorothy", "Murphy",
            "Alice", "Howard"};

    private static final String[] lastNames = new String[]{
            "Ruth", "Jackson", "Debra", "Allen", "Gerald", "Harris", "Raymond",
            "Carter", "Jacqueline", "Torres", "Joseph", "Nelson", "Carlos",
            "Sanchez", "Ralph", "Clark", "Jean", "Alexander", "Stephen", "Roberts",
            "Eric", "Long", "Amanda", "Scott", "Teresa", "Diaz", "Wanda", "Thomas"};

    private static final String[] domains = new String[]{
            "mail", "email", "gmail", "vk", "msn", "yandex", "yahoo", "edu.spbstu", "swebhosting"};

    private static final String[] domainCodes = new String[]{
            "ru", "com", "biz", "info", "net", "su", "org"};

    private static final String[] cities = new String[]{
            "Saint-Petersburg", "Moscow", "Vologda", "Volgograd", "Murmansk", "Vladivostok", "Novgorod", "Tula"};

    private static final String[] abouts = new String[]{
            "student", "dentist", "engineer", "social worker", "nurse", "doctor"};

    private static final String[] roles = new String[]{
            "ROLE_ADMIN", "ROLE_USER", "ROLE_ANONYMOUS", "ROLE_GUEST", "ROLE_UNDEFINED", "ROLE_MAIN"};

    private static final String[] randomWords = new String[]{
            "teach", "about", "you", "may", "back", "going to",
            "live", "destination", "tomorrow", "big", "date", "I",
            "walk", "theatre", "queue", "window", "package",
            "run", "into", "for", "over", "apple", "dark",
            "order", "seller", "headphone", "break", "buy"};

    private static final String[] htmlTags = new String[]{
            "div", "span", "h1", "button", "b", "strong", "sup", "sub"};

    public TestDataInitService(UserService userService, RoleService roleService, AnswerService answerService,
                               QuestionService questionService, TagService tagService) {
        this.userService = userService;
        this.roleService = roleService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.tagService = tagService;
    }


    private final UserService userService;
    private final RoleService roleService;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final TagService tagService;


    //fill related tables user_entity and role with test data
    public void fillTableWithTestData() {
        addRandomRoles();
        addRandomUsers();
        addRandomTags();
        addRandomQuestions();
        addRandomAnswers();
    }

    private void addRandomAnswers() {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < answersNum; i++) {
            String first = getRand(htmlTags);
            String second = getRand(htmlTags);
            String third = getRand(htmlTags);
            String htmlBody = "<" + first + ">" + getRand(randomWords) + "</" + first + ">" + "\n" +
                    "<" + second + ">" + getRand(randomWords) + "</" + second + ">" + "\n" +
                    "<" + third + ">" + getRand(randomWords) + "</" + third + ">";
            LocalDateTime dateAcceptTime = LocalDateTime.of(2021, getRandInt(1, 12),
                    getRandInt(1, 28), getRandInt(0, 23),
                    getRandInt(0, 59));
            Answer answer = new Answer(null, null, htmlBody, i % 5 == 0, i % 2 == 0, i % 6 == 0);
            answer.setDateAcceptTime(dateAcceptTime);
            answers.add(answer);
        }

        List<User> existingUsers = userService.getAll();
        List<Question> existingQuestions = questionService.getAll();
        for (Answer answer : answers) {
            answer.setUser(existingUsers.get(getRandInt(0, existingUsers.size())));
            answer.setQuestion(existingQuestions.get(getRandInt(0, existingQuestions.size())));
        }
        answerService.persistAll(answers);
    }

    private void addRandomQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < questionsNum; i++) {
            String title = getRand(randomWords);
            StringBuilder description = new StringBuilder();
            for (int j = 0; j < getRandInt(3, 15); j++) {
                description.append(getRand(randomWords)).append(" ");
            }
            questions.add(new Question(null, title, description.toString(), null,
                    null, null, null, i % 3 == 0,
                    null, null, null, null));
        }

        List<User> existingUsers = userService.getAll();
        List<Tag> existingTags = tagService.getAll();
        for (Question question : questions) {
            question.setUser(existingUsers.get(getRandInt(0, existingUsers.size())));
            int tagEndIndex = getRandInt(1, existingTags.size());
            question.setTags(existingTags.subList(0, tagEndIndex));
        }
        questionService.persistAll(questions);
    }

    private void addRandomTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < tagsNum; i++) {
            String name = getRand(randomWords);
            StringBuilder description = new StringBuilder();
            for (int j = 0; j < getRandInt(3, 15); j++) {
                description.append(getRand(randomWords)).append(" ");
            }
            tags.add(new Tag(null, name, description.toString(), null, null));
        }
        tagService.persistAll(tags);
    }

    private void addRandomUsers() {
        List<User> users = new ArrayList<>();
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

            users.add(new User(email, passwordEncoder.encode(password), fullName, city,
                    linkSite, linkGithub, linkVk, about, imageLink, nickname));
        }

        List<Role> existingRoles = roleService.getAll();
        for (User user : users) {
            user.setRole(existingRoles.get(getRandInt(0, existingRoles.size())));
        }

        users.get(0).setEmail("user");
        users.get(0).setPassword(passwordEncoder.encode("user"));
        users.get(0).setRole(roleService.getById(1L).orElse(existingRoles.get(0)));

        userService.persistAll(users);
    }

    private void addRandomRoles() {
        Set<Role> testRoles = new HashSet<>();
        while (testRoles.size() < rolesNum - 1) {
            testRoles.add(new Role(getRand(roles)));
        }
        roleService.persistAll(testRoles);
    }


    private static int getRandInt(int lowBound, int upperBound) {
        return ThreadLocalRandom.current().nextInt(lowBound, upperBound);
    }

    private static <T> T getRand(T[] array) {
        return array[ThreadLocalRandom.current().nextInt(0, array.length)];
    }

    private static String getRandStr(int lowBound, int upperBound) {
        StringBuilder stringBuilder = new StringBuilder();
        int strLength = getRandInt(lowBound, upperBound);
        for (int i = 0; i < strLength; i++) {
            stringBuilder.append(getRand(alphabet));
        }
        return stringBuilder.toString();
    }
}
