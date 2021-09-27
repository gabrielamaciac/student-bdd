package com.learning.student.studentbdd.features.importer;

import com.learning.student.studentbdd.CucumberStepDefinitions;
import com.learning.student.studentbdd.payload.Student;
import com.learning.student.studentbdd.payload.StudentSearch;
import com.learning.student.studentbdd.util.TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

public class ImportStudentTest extends CucumberStepDefinitions {
    @Autowired
    private RestTemplate restTemplate;

    private Student student;
    private String uniqueCnp;
    private StudentSearch importedStudent;
    private String studentXml;

    @Given("a valid student JSON")
    public void prepareTheValidStudentJson() {
        student = TestData.getStudent();
        uniqueCnp = UUID.randomUUID().toString().replace("-", "");
        student.setCnp(uniqueCnp);
    }

    @When("user calls import endpoint")
    public void clientCallsImporterService() {
        HttpEntity<Student> request = new HttpEntity<>(student);
        ResponseEntity<String> response = restTemplate.exchange(IMPORTER_URL, HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Then("the student is created")
    public void theStudentIsCreated() {
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            ResponseEntity<StudentSearch> response = restTemplate.getForEntity(SEARCH_SERVICE_URL +
                    "/student?firstName=BDD&lastName=Test&cnp=" + uniqueCnp, StudentSearch.class);
            assertNotNull(response.getBody());
            importedStudent = response.getBody();
            assertEquals(student.getCnp(), importedStudent.getCnp());
        });
    }
}
