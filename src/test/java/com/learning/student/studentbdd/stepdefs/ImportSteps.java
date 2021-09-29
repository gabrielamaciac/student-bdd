package com.learning.student.studentbdd.stepdefs;


import com.learning.student.studentbdd.payload.Student;
import com.learning.student.studentbdd.payload.StudentSearch;
import com.learning.student.studentbdd.util.TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static com.learning.student.studentbdd.util.Constants.IMPORTER_URL;
import static com.learning.student.studentbdd.util.Constants.SEARCH_SERVICE_URL;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class ImportSteps extends AbstractSteps {
    @Autowired
    private RestTemplate restTemplate;

    @Given("a valid student")
    public void prepareTheValidStudentJson() {
        Student student = TestData.getStudent();
        testContext().setStudent(student);
        log.info("given a valid student JSON");
    }

    @When("user calls import endpoint")
    public void clientCallsImporterService() {
        HttpEntity<Object> request = new HttpEntity<>(testContext().getStudent());
        ResponseEntity<String> response = restTemplate.exchange(IMPORTER_URL, HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        log.info("when user calls import endpoint");
    }

    @Then("the student is created")
    public void theStudentIsCreated() {
        Student student = testContext().getStudent();

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            ResponseEntity<StudentSearch> response = restTemplate.getForEntity(SEARCH_SERVICE_URL +
                    "/student?firstName=BDD&lastName=Test&cnp=" + TestData.TEST_CNP, StudentSearch.class);
            assertNotNull(response.getBody());
            testContext().setStudentSearch(response.getBody());
            assertEquals(student.getCnp(), response.getBody().getCnp());
        });
        log.info("then the student is created");
    }
}
