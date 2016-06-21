package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.service.ICustomerService;
import at.ac.tuwien.shared.dtos.Classification;
import at.ac.tuwien.shared.dtos.Customer;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private ICustomerService customerService;

    @Autowired
    public ApiController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/role/{role}", method = RequestMethod.GET)
    public Boolean login(@PathVariable final String role,
                         final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");

        return ((List<String>) claims.get("roles")).contains(role);
    }

    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    public ResponseEntity<Long> processCustomer(@RequestBody Customer customer) {
        System.out.println("new customer: " + customer);
        at.ac.tuwien.cashtechthon.domain.Customer persistedCustomer = customerService.save(customer);
        return new ResponseEntity<>(persistedCustomer.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/classification", method = RequestMethod.POST)
    public ResponseEntity<?> processClassification(@RequestBody Classification classification) {
        System.out.println("new classification: " + classification);
        return new ResponseEntity<>("Classifications created", HttpStatus.CREATED);
    }
}
