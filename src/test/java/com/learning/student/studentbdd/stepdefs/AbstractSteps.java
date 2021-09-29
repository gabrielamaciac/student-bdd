package com.learning.student.studentbdd.stepdefs;

import com.learning.student.studentbdd.CucumberStepDefinitions;
import com.learning.student.studentbdd.config.TestContext;

import static com.learning.student.studentbdd.config.TestContext.CONTEXT;

public class AbstractSteps extends CucumberStepDefinitions {
    public TestContext testContext() {
        return CONTEXT;
    }
}
