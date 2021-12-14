package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.question.VoteAnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.user.ReputationDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.question.VoteAnswerService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.javamentor.qa.platform.models.entity.question.answer.VoteType.UP_VOTE;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    private final AnswerDao answerDao;
    private final VoteAnswerDao voteAnswerDao;
    private final ReputationDao reputationDao;

    @Value("${reputation.answer.vote.up}")
    private Integer voteUpReputation;
    @Value("${reputation.answer.vote.down}")
    private Integer voteDownReputation;

    public VoteAnswerServiceImpl(AnswerDao answerDao, VoteAnswerDao voteAnswerDao, ReputationDao reputationDao) {
        super(voteAnswerDao);
        this.answerDao = answerDao;
        this.voteAnswerDao = voteAnswerDao;
        this.reputationDao = reputationDao;
    }

    @Override
    @Transactional
    public Long vote(Long answerId, User user, VoteType voteType) {
        Optional<Answer> answerOpt = answerDao.getById(answerId);

        if (answerOpt.isPresent()) {
            Answer answer = answerOpt.get();
            Optional<VoteAnswer> voteAnswerOpt = voteAnswerDao.findByAnswerAndUser(answerId, user.getId());
            Optional<Reputation> reputationOpt = reputationDao.findByAnswerAndSender(answerId, user.getId());

            if (voteAnswerOpt.isEmpty() && reputationOpt.isEmpty()) {
                Reputation rep = new Reputation();
                rep.setAuthor(answer.getUser());
                rep.setSender(user);
                rep.setCount(getReputationCount(voteType));
                rep.setAnswer(answer);
                rep.setType(ReputationType.VoteAnswer);

                voteAnswerDao.persist(new VoteAnswer(user, answer, voteType));
                reputationDao.persist(rep);

                return voteAnswerDao.getReputationCount(answer.getId());
            }
        }
        return null;
    }

    private Integer getReputationCount(VoteType voteType) {
        return voteType == UP_VOTE ? voteUpReputation : voteDownReputation;
    }
}
