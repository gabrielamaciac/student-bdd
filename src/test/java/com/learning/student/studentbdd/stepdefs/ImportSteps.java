package com.learning.student.studentbdd.stepdefs;


import com.learning.student.studentbdd.payload.Student;
import com.learning.student.studentbdd.payload.StudentSearch;
import com.learning.student.studentbdd.util.FileGenerator;
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

import java.util.UUID;
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
    @Autowired
    private FileGenerator fileGenerator;

    private String uniqueCnp = UUID.randomUUID().toString().replace("-", "");

    @Given("a valid student payload is prepared")
    public void prepareTheValidStudentPayload() {
        Student student = TestData.getStudent();
        testContext().setStudent(student);
        log.info("given a valid student JSON");
    }

    @When("user calls import")
    public void clientCallsImporterService() {
        HttpEntity<Object> request = new HttpEntity<>(testContext().getStudent());
        ResponseEntity<String> response = restTemplate.exchange(IMPORTER_URL, HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        log.info("when user calls import");
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

    @When("the user pastes a student xml file")
    public void createAnXmlFile() {
        fileGenerator.createFile(TestData.getStudentXml(uniqueCnp));
        log.info("user pastes a student xml file");
    }

    @Then("the student is imported")
    public void theStudentWasImported() {
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            ResponseEntity<StudentSearch> response = restTemplate.getForEntity(SEARCH_SERVICE_URL +
                    "/student?firstName=BDD&lastName=Test&cnp=" + uniqueCnp, StudentSearch.class);
            assertNotNull(response.getBody());
            assertEquals(uniqueCnp, response.getBody().getCnp());
        });
        log.info("then the student is created");
    }

}
