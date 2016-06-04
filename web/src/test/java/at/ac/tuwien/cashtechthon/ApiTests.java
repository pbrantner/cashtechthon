package at.ac.tuwien.cashtechthon;

import static org.junit.Assert.assertEquals;

import at.ac.tuwien.shared.dtos.Classification;
import at.ac.tuwien.shared.dtos.LoginRequest;
import at.ac.tuwien.shared.dtos.TokenLoginResponse;
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
        LoginRequest userLogin = new LoginRequest();
        userLogin.setUsername("user");
        userLogin.setPassword("password");
        ResponseEntity<TokenLoginResponse> response = new TestRestTemplate().postForEntity("http://localhost:" + port + "/login/api", userLogin, TokenLoginResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // token must has header, payload and signature separated by two dots
        assertEquals(3, response.getBody().getToken().split("\\.").length);
    }

    @Test
    public void testLoggedInClassification() {
        LoginRequest userLogin = new LoginRequest();
        userLogin.setUsername("user");
        userLogin.setPassword("password");
        ResponseEntity<TokenLoginResponse> response = new TestRestTemplate().postForEntity("http://localhost:" + port + "/login/api", userLogin, TokenLoginResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Classification classification = new Classification();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + response.getBody().getToken());
        HttpEntity<Classification> entity = new HttpEntity<>(classification, httpHeaders);
        ResponseEntity<String> classificationResponse = new TestRestTemplate().postForEntity("http://localhost:" + port + "/api/classification", entity, null);
        assertEquals(HttpStatus.CREATED, classificationResponse.getStatusCode());
    }
}