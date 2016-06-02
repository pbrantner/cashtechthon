package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.shared.dtos.LoginRequest;
import at.ac.tuwien.shared.dtos.TokenLoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final Map<String, List<String>> restUserDb = new HashMap<>();

    public LoginController() {
        restUserDb.put("user", Arrays.asList("user", "admin"));
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
    public ResponseEntity<?> login(@RequestBody final LoginRequest login) {
        if (login.getUsername() == null || !restUserDb.containsKey(login.getUsername())) {
            return new ResponseEntity<>("Invalid login", HttpStatus.UNAUTHORIZED);
        }

        TokenLoginResponse loginResponse = new TokenLoginResponse();
        loginResponse.setToken(Jwts.builder().setSubject(login.getUsername())
                .claim("roles", restUserDb.get(login.getUsername())).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact());

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
