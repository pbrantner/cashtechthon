package at.ac.tuwien.cashtechthon.controller;

import at.ac.tuwien.cashtechthon.service.IClassificationService;
import at.ac.tuwien.cashtechthon.service.ICustomerService;
import at.ac.tuwien.shared.dtos.Classification;
import at.ac.tuwien.shared.dtos.Customer;
import io.jsonwebtoken.Claims;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private Log logger = LogFactory.getLog(ApiController.class);

    private ICustomerService customerService;
    private IClassificationService classificationService;

    @Autowired
    public ApiController(ICustomerService customerService,
                         IClassificationService classificationService) {
        this.customerService = customerService;
        this.classificationService = classificationService;
    }

    @RequestMapping(value = "/role/{role}", method = RequestMethod.GET)
    public Boolean login(@PathVariable final String role,
                         final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");
        return ((List<String>) claims.get("roles")).contains(role);
    }

    /**
     * Persists a new customer
     *
     * @param customer
     * @return id of the created customer
     */
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    public ResponseEntity<Long> processCustomer(@RequestBody Customer customer) {
        logger.info("new customer: " + customer);
        at.ac.tuwien.cashtechthon.domain.Customer persistedCustomer = customerService.save(customer);
        logger.info("customer persisted: " + persistedCustomer);
        return new ResponseEntity<>(persistedCustomer.getId(), HttpStatus.CREATED);
    }

    /**
     * Persists a new classification
     * @param classification
     * @return id of the created classification
     */
    @RequestMapping(value = "/classification", method = RequestMethod.POST)
    public ResponseEntity<Long> processClassification(@RequestBody Classification classification) {
        logger.info("new classification arrived: " + classification);
        at.ac.tuwien.cashtechthon.domain.Classification persistedClassification = classificationService.save(classification);
        logger.info("classification persisted: " + persistedClassification);
        return new ResponseEntity<>(persistedClassification.getId(), HttpStatus.CREATED);
    }
}
