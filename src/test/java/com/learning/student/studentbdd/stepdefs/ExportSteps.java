package com.learning.student.studentbdd.stepdefs;

import com.learning.student.studentbdd.payload.StudentSearch;
import com.learning.student.studentbdd.util.TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.learning.student.studentbdd.util.Constants.EXPORT_SERVICE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class ExportSteps extends AbstractSteps {
    @Autowired
    private RestTemplate restTemplate;

    private String xmlResponse;
    private StudentSearch studentSearch;

    @Given("the imported student")
    public void theImportedStudent() {
        assertNotNull(testContext().getStudentSearch());
        studentSearch = testContext().getStudentSearch();
        log.info("given the imported student");
    }

    @When("user calls export endpoint")
    public void userCallsExportService() {
        ResponseEntity<String> response =
                restTemplate.exchange(EXPORT_SERVICE_URL + "/student/" + studentSearch.getId(),
                        HttpMethod.POST, null, String.class);
        xmlResponse = response.getBody();
        log.info("user calls export endpoint");
    }

    @Then("the student is exported")
    public void studentIsExported() {
        assertEquals(TestData.getStudentXml(studentSearch.getCnp()), xmlResponse.replace("\r\n", "\n"));
        log.info("the student is exported");
    }
}
