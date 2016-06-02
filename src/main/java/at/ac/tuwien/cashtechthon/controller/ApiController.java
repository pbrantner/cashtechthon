package at.ac.tuwien.cashtechthon.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final String SECRET_KEY = "secretkey";

    private final Map<String, List<String>> userDb = new HashMap<>();

    public ApiController() {
        userDb.put("thomas", Arrays.asList("user", "admin"));
        userDb.put("patrick", Arrays.asList("user", "admin"));
    }

    @RequestMapping(value = "/role/{role}", method = RequestMethod.GET)
    public Boolean login(@PathVariable final String role,
                         final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");

        return ((List<String>) claims.get("roles")).contains(role);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login) throws ServletException {
        if (login.username == null || !userDb.containsKey(login.username)) {
            throw new ServletException("Invalid login");
        }

        return new LoginResponse(Jwts.builder().setSubject(login.username)
                .claim("roles", userDb.get(login.username)).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact());
    }

    private static class UserLogin {
        public String username;
        public String password;
    }

    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}
