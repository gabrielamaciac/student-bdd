package com.learning.student.studentbdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
//        glue = "com.learning.student.studentbdd.features.importer",
        features = "src/test/resources"
)
public class CucumberTestRunner {

}
