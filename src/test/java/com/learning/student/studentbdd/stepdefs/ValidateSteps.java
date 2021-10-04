package com.learning.student.studentbdd.stepdefs;

import com.learning.student.studentbdd.payload.StudentSearch;
import com.learning.student.studentbdd.payload.ValidationDetail;
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
import static com.learning.student.studentbdd.util.Constants.VALIDATION_SERVICE_URL;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class ValidateSteps extends AbstractSteps {
    @Autowired
    private RestTemplate restTemplate;

    private String uniqueCnp = UUID.randomUUID().toString().replace("-", "");
    private String id;
    private ValidationDetail[] validationDetails;

    @Then("the student is valid")
    public void theStudentIsValid() {
        assertTrue(testContext().getStudentSearch().isValid());
    }

    @Given("an invalid student is imported")
    public void importInvalidStudent() {
        HttpEntity<Object> request = new HttpEntity<>(TestData.getInvalidStudent(uniqueCnp));
        ResponseEntity<String> response = restTemplate.exchange(IMPORTER_URL, HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        log.info("an invalid student is imported");
    }

    @When("user calls validation")
    public void userCallsValidation() {
        searchTheStudent();
        ResponseEntity<ValidationDetail[]> response = validateStudent();
        validationDetails = response.getBody();
        log.info("user calls validation");
    }

    @Then("the error is returned")
    public void errorListIsReturned() {
        assertEquals(id, validationDetails[0].getStudentId());
        assertEquals("Grade average below 5.", validationDetails[0].getErrorName());
        log.info("the error is returned");
    }

    public void searchTheStudent() {
        // polling rate
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            ResponseEntity<StudentSearch> response = restTemplate.getForEntity(SEARCH_SERVICE_URL +
                    "/student?firstName=BDD&lastName=Test&cnp=" + uniqueCnp, StudentSearch.class);
            id = response.getBody().getId();
        });
    }

    private ResponseEntity<ValidationDetail[]> validateStudent() {
        return restTemplate.getForEntity(VALIDATION_SERVICE_URL + "/student/" + id, ValidationDetail[].class);
    }
}
