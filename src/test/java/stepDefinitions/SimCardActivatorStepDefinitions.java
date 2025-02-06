package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import au.com.telstra.simcardactivator.foundation.SimCard;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    private SimCard simCard;
    private ResponseEntity<Void> activationResponse;
    private Boolean activationStatus;

    private final String BASE_URL = "http://localhost:8080";

    @Given("the SIM card with ICCID {string} and email {string}")
    public void the_sim_card_with_iccid_and_email(String iccid, String email) {
        simCard = new SimCard(iccid, email, false);
    }

    @When("I send an activation request")
    public void i_send_an_activation_request() {
        activationResponse = restTemplate.postForEntity(BASE_URL + "/activate", simCard, Void.class);
    }

    @Then("the activation should be successful")
    public void the_activation_should_be_successful() {
        checkActivationStatus(true);
    }

    @Then("the activation should fail")
    public void the_activation_should_fail() {
        checkActivationStatus(false);
    }

    private void checkActivationStatus(boolean expectedStatus) {
        // Assuming the first record in the database has ID = 1, second has ID = 2, and so on.
        Long simCardId = 1L; // Modify accordingly if necessary
        ResponseEntity<Boolean> response = restTemplate.getForEntity(BASE_URL + "/activation-status?simCardId=" + simCardId, Boolean.class);
        activationStatus = response.getBody();
        
        Assertions.assertEquals(expectedStatus, activationStatus, "Activation status did not match expected result.");
    }
}
