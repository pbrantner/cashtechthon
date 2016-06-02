package at.ac.tuwien.cashtechthon;

import static org.junit.Assert.assertEquals;

import at.ac.tuwien.cashtechthon.controller.ApiController;
import at.ac.tuwien.cashtechthon.controller.LoginController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.data.web.PageableDefault;
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
    public void classificationProtected() {
        System.out.println(port);
        ResponseEntity<String> response = new TestRestTemplate().postForEntity("http://localhost:" + port + "/api/classification", "", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void loginNotProtectedAndTokenReturned() {
        LoginController.UserLogin userLogin = new LoginController.UserLogin();
        userLogin.username = "thomas";
        userLogin.password = "pw";
        ResponseEntity<LoginController.LoginResponse> response = new TestRestTemplate().postForEntity("http://localhost:" + port + "/login/api", userLogin, LoginController.LoginResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // token must has header, payload and signature separated by two dots
        assertEquals(3, response.getBody().token.split("\\.").length);
    }
}