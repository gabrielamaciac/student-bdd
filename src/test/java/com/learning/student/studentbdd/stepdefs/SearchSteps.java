package com.learning.student.studentbdd.stepdefs;

import com.learning.student.studentbdd.payload.Student;
import com.learning.student.studentbdd.payload.StudentSearch;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.learning.student.studentbdd.util.Constants.SEARCH_SERVICE_URL;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class SearchSteps extends AbstractSteps {
    @Autowired
    private RestTemplate restTemplate;
    private Student student;
    private List<StudentSearch> studentSearchList;


    @Given("an already imported student")
    public void prepareTheValidStudentJson() {
        student = testContext().getStudent();
        assertNotNull(student);
        log.info("given an already imported student");
    }

    @When("user calls search by name and cnp")
    public void userSearchesByNameAndCnp() {
        // polling rate
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            ResponseEntity<StudentSearch> response = restTemplate.getForEntity(SEARCH_SERVICE_URL +
                    "/student?firstName=BDD&lastName=Test&cnp=" + student.getCnp(), StudentSearch.class);
            assertNotNull(response.getBody());
            testContext().setStudentSearch(response.getBody());
        });
        log.info("when user calls search by name and cnp endpoint");
    }

    @Then("the student is found")
    public void studentIsFound() {
        assertNotNull(testContext().getStudentSearch());
        assertEquals(student.getCnp(), testContext().getStudentSearch().getCnp());
        log.info("then the student is found");
    }


    @When("user calls search by search term")
    public void userSearchesBySearchTerm() {
        ResponseEntity<List<StudentSearch>> response = getSearchResponse("?searchTerm=BDD");
        studentSearchList = response.getBody();
        log.info("when user calls search by term endpoint");
    }


    @When("user calls search by isValid")
    public void userSearchesByIsValid() {
        ResponseEntity<List<StudentSearch>> response = getSearchResponse("/student/isValid/true");
        studentSearchList = response.getBody();
        log.info("when user calls search by isValid endpoint");
    }

    @Then("the student list is found")
    public void studentListIsFound() {
        assertFalse(studentSearchList.isEmpty());
        log.info("then student list is found");
    }

    private ResponseEntity<List<StudentSearch>> getSearchResponse(String path) {
        ParameterizedTypeReference<List<StudentSearch>> typeRef = new ParameterizedTypeReference<>() {};
        return restTemplate.exchange(SEARCH_SERVICE_URL + path, HttpMethod.GET, null, typeRef);
    }
}
