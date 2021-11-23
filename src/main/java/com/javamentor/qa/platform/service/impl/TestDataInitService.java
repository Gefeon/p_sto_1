package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.impl.model.question.AnswerServiceImpl;
import com.javamentor.qa.platform.service.impl.model.question.QuestionServiceImpl;
import com.javamentor.qa.platform.service.impl.model.question.TagServiceImpl;
import com.javamentor.qa.platform.service.impl.model.user.RoleServiceImpl;
import com.javamentor.qa.platform.service.impl.model.user.UserServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service

public class TestDataInitService {
    //Amount of test data
    private final static int usersNum = 10;
    private final static int rolesNum = 4;
    private final static int answersNum = 10;
    private final static int questionsNum = 10;
    private final static int tagsNum = 4;

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

    private final String[] randomWords = new String[]{
            "teach", "about", "you", "may", "back", "going to",
            "live", "destination", "tomorrow", "big", "date", "I",
            "walk", "theatre", "queue", "window", "package",
            "run", "into", "for", "over", "apple", "dark",
            "order", "seller", "headphone", "break", "buy"};

    private final String[] htmlTags = new String[]{
            "div", "span", "h1", "button", "b", "strong", "sup", "sub"};

    public TestDataInitService(UserServiceImpl userService, RoleServiceImpl roleService, AnswerServiceImpl answerService,
                               QuestionServiceImpl questionService, TagServiceImpl tagService) {
        this.userService = userService;
        this.roleService = roleService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.tagService = tagService;
    }

    @Getter
    @Setter
    private UserServiceImpl userService;
    @Getter
    @Setter
    private RoleServiceImpl roleService;
    @Getter
    @Setter
    private AnswerServiceImpl answerService;
    @Getter
    @Setter
    private QuestionServiceImpl questionService;
    @Getter
    @Setter
    private TagServiceImpl tagService;


    //fill related tables user_entity and role with test data
    public void fillTableWithTestData() {
        roleService.persistAll(getRandomRoles());
        List<Role> existingRoles = roleService.getAll();
        Set<User> usersToPersist = getRandomUsers();
        for (User user : usersToPersist) {
            user.setRole(existingRoles.get(getRandInt(0, existingRoles.size() - 1)));
        }
        userService.persistAll(usersToPersist);

        List<Tag> tags = getRandomTags();
        List<Question> questions = getRandomQuestions();
        List<Answer> answers = getRandomAnswers();

        tagService.persistAll(tags);

        List<User> existingUsers = userService.getAll();
        List<Tag> existingTags = tagService.getAll();
        int tagEndIndex = getRandInt(1, existingTags.size() - 1);
        for (Question question : questions) {
            question.setUser(existingUsers.get(getRandInt(0, existingUsers.size() - 1)));
            question.setTags(existingTags.subList(0, tagEndIndex));
        }
        questionService.persistAll(questions);

        Set<Answer> answersToPersist = new HashSet<>();
        for (Answer answer : answers) {
            answer.setUser(existingUsers.get(getRandInt(0, existingUsers.size() - 1)));
            answer.setQuestion(questions.get(getRandInt(0, questions.size() - 1)));
            answersToPersist.add(answer);
        }
        answerService.persistAll(answersToPersist);
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

    //generates tagsNum tags with random name and description
    private List<Tag> getRandomTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < tagsNum; i++) {
            String name = getRand(randomWords);
            StringBuilder description = new StringBuilder();
            for (int j = 0; j < getRandInt(3, 15); j++) {
                description.append(getRand(randomWords)).append(" ");
            }
            tags.add(new Tag(null, name, description.toString(), null, null));
        }
        return tags;
    }

    //generates answersNum answers with random title,
    // description and isDeleted value
    private List<Question> getRandomQuestions() {
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
        return questions;
    }

    //generates answersNum answers with random htmlBody,
    // dataAcceptTime and all Boolean values
    private List<Answer> getRandomAnswers() {
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
        return answers;
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
