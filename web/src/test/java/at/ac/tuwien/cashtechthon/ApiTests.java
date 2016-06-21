package at.ac.tuwien.cashtechthon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import at.ac.tuwien.shared.dtos.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PLAYfulSavingApiApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ApiTests {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testClassificationProtected() {
        ResponseEntity<String> response = new TestRestTemplate().postForEntity("http://localhost:" + port + "/api/classification", "", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testLoginNotProtectedAndTokenReturned() {
        ResponseEntity<TokenLoginResponse> loginResponse = requestLoginResponse();

        // token must has header, payload and signature separated by two dots
        assertEquals(3, loginResponse.getBody().getToken().split("\\.").length);
    }

    /**
     * Logs in and returns corresponding login header
     * @return corresponding login header such as token
     */
    private ResponseEntity<TokenLoginResponse> requestLoginResponse() {
        LoginRequest userLogin = new LoginRequest();
        userLogin.setUsername("user");
        userLogin.setPassword("password");
        ResponseEntity<TokenLoginResponse> loginResponse = new TestRestTemplate().postForEntity("http://localhost:" + port + "/login/api", userLogin, TokenLoginResponse.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        return loginResponse;
    }

    /**
     * Prepares the login headers
     * @return login headers with authorization token
     */
    private HttpHeaders prepareLoginHeaders() {
        ResponseEntity<TokenLoginResponse> loginResponse = requestLoginResponse();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + loginResponse.getBody().getToken());
        return httpHeaders;
    }

    private Customer generateCustomer() {
        Customer customer = new Customer();
        customer.setFirstname("Thomas");
        customer.setLastname("Muster");
        customer.setLocation("Vienna");
        customer.setGender(Gender.Male);
        LocalDate birthDate = LocalDate.now();
        customer.setDateOfBirth(birthDate);
        return customer;
    }

    @Test
    public void testLoggedInAddNewCustomer() {
        Customer customer = generateCustomer();
        HttpHeaders httpHeaders = prepareLoginHeaders();

        HttpEntity<Customer> entity = new HttpEntity<>(customer, httpHeaders);
        ResponseEntity<Long> response = new TestRestTemplate().postForEntity("http://localhost:" + port + "/api/customer", entity, Long.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() > 0);
    }

    @Test
    public void testLoggedInAddClassification() {
        Customer customer = generateCustomer();
        customer.setId(1L);
        HttpHeaders httpHeaders = prepareLoginHeaders();

        Classification classification = new Classification();
        classification.setCustomer(customer);
        classification.setAmount(new BigDecimal(100));
        classification.setCurrency(Currency.getInstance("EUR"));
        classification.setClassificationDate(LocalDateTime.now());

        HttpEntity<Classification> entity = new HttpEntity<>(classification, httpHeaders);
        ResponseEntity<Long> response = new TestRestTemplate().postForEntity("http://localhost:" + port + "/api/classification", entity, Long.class);
        assertTrue(response.getBody() > 0);
    }
}