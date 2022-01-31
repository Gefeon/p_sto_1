package com.javamentor.qa.platform.api.listener;

import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class CustomTestExecutionListener implements TestExecutionListener {

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        CacheManager cacheManager = testContext.getApplicationContext().getBean(CacheManager.class);
        cacheManager.getCache("user-email").clear();
    }

}
