package com.javamentor.qa.platform.dao.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.IgnoredTagDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import org.springframework.stereotype.Repository;

@Repository
public class IgnoredTagDaoImpl extends ReadWriteDaoImpl<IgnoredTag, Long> implements IgnoredTagDao {
}
