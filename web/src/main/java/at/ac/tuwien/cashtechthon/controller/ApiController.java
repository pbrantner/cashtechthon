package at.ac.tuwien.cashtechthon.controller;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping(value = "/role/{role}", method = RequestMethod.GET)
    public Boolean login(@PathVariable final String role,
                         final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");

        return ((List<String>) claims.get("roles")).contains(role);
    }

    @RequestMapping(value = "/classification", method = RequestMethod.POST)
    public ResponseEntity<?> processClassification(@RequestBody String classification) {
        System.out.println("new classification: " + classification);
        return new ResponseEntity<>("Classifications created", HttpStatus.CREATED);
    }
}
