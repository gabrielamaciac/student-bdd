package com.learning.student.studentbdd.features.importer;

import com.learning.student.studentbdd.CucumberStepDefinitions;
import com.learning.student.studentbdd.payload.Student;
import com.learning.student.studentbdd.payload.StudentSearch;
import com.learning.student.studentbdd.util.TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.learning.student.studentbdd.util.Constants.IMPORTER_URL;
import static com.learning.student.studentbdd.util.Constants.SEARCH_SERVICE_URL;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SearchStudentTest extends CucumberStepDefinitions {
    @Autowired
    private RestTemplate restTemplate;

    private Student student;
    private StudentSearch studentFound;
    private List<StudentSearch> studentsFound;
    private String uniqueCnp;

    @Given("an already imported student")
    public void prepareTheValidStudentJson() {
        student = TestData.getStudent();
        uniqueCnp = UUID.randomUUID().toString().replace("-", "");
        student.setCnp(uniqueCnp);
        HttpEntity<Student> request = new HttpEntity<>(student);
        ResponseEntity<String> response = restTemplate.exchange(IMPORTER_URL, HttpMethod.POST, request, String.class);
        assertNotNull(response.getBody());
    }


    @When("user calls search by name and cnp endpoint")
    public void userSearchesByNameAndCnp() {
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            ResponseEntity<StudentSearch> response = restTemplate.getForEntity(SEARCH_SERVICE_URL +
                    "/student?firstName=BDD&lastName=Test&cnp=" + uniqueCnp, StudentSearch.class);
            studentFound = response.getBody();
        });
    }

    @Then("the student is found")
    public void studentIsFound() {
        assertNotNull(studentFound);
        assertEquals(student.getCnp(), studentFound.getCnp());
    }


    @When("user calls search by search term endpoint")
    public void userSearchesBySearchTerm() {
        ResponseEntity<List<StudentSearch>> response = getSearchResponse("?searchTerm=BDD");
        studentsFound = response.getBody();
    }


    @When("user calls search by isValid endpoint")
    public void userSearchesByIsValid() {
        ResponseEntity<List<StudentSearch>> response = getSearchResponse("/student/isValid/true");
        studentsFound = response.getBody();
    }

    @Then("the student list is found")
    public void studentListIsFound() {
        assertFalse(studentsFound.isEmpty());
    }


    private ResponseEntity<List<StudentSearch>> getSearchResponse(String path) {
        ParameterizedTypeReference<List<StudentSearch>> typeRef = new ParameterizedTypeReference<>() {};
        return restTemplate.exchange(SEARCH_SERVICE_URL + path, HttpMethod.GET, null, typeRef);
    }
}
