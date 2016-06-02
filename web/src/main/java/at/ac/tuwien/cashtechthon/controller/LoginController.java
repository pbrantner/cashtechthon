package at.ac.tuwien.cashtechthon.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import java.util.*;

@Controller
@RequestMapping("/login")
public class LoginController extends AbstractController {

    @Override
    protected String getViewDir() {
        return "login";
    }

    private static final String SECRET_KEY = "secretkey";

    private final Map<String, List<String>> userDb = new HashMap<>();

    public LoginController() {
        userDb.put("thomas", Arrays.asList("user", "admin"));
        userDb.put("patrick", Arrays.asList("user", "admin"));
    }

    /**
     * Renders the login form.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showLogin() {
        return createViewPath("login");
    }

    /**
     * Renders the login form with an additional error message.
     *
     * @return
     */
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String showLoginError(Model model) {
        model.addAttribute("loginError", true);
        return createViewPath("login");
    }

    /**
     * Login for the REST endpoint
     *
     * @param login user credentials
     * @return token if login successful
     * @throws ServletException
     */
    @RequestMapping(value = "/api", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse login(@RequestBody final UserLogin login) throws ServletException {
        if (login.username == null || !userDb.containsKey(login.username)) {
            throw new ServletException("Invalid login");
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(Jwts.builder().setSubject(login.username)
                .claim("roles", userDb.get(login.username)).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact());

        return loginResponse;
    }

    public static class UserLogin {
        public String username;
        public String password;
    }

    public static class LoginResponse {
        public String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
