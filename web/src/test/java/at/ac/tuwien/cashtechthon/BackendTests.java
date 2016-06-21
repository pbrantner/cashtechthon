package at.ac.tuwien.cashtechthon;

import at.ac.tuwien.cashtechthon.dao.IClassificationDao;
import at.ac.tuwien.cashtechthon.service.IClassificationService;
import at.ac.tuwien.cashtechthon.service.ICustomerService;
import at.ac.tuwien.shared.dtos.*;
import at.ac.tuwien.shared.dtos.Classification;
import at.ac.tuwien.shared.dtos.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PLAYfulSavingApiApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class BackendTests {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private IClassificationService classificationService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IClassificationDao classificationDao;

    @Test
    public void testPersistClassification() {
        assertTrue(classificationDao.findAll().size() == 0);

        Customer customer = generateCustomer();
        at.ac.tuwien.cashtechthon.domain.Customer persistedCustomer = customerService.save(customer);
        customer.setId(persistedCustomer.getId());

        Classification classification = new Classification();
        classification.setCustomer(customer);
        classification.setClassification("Test");
        classification.setCurrency(Currency.getInstance("EUR"));
        classification.setAmount(new BigDecimal(100));
        at.ac.tuwien.cashtechthon.domain.Classification persistedClassification = classificationService.save(classification);
        assertNotNull(persistedClassification);
        assertEquals(classificationDao.findAll().size(), 1);

        Page<at.ac.tuwien.cashtechthon.domain.Classification> pages = classificationDao.findByCustomer(persistedCustomer, new PageRequest(0, 10));
        assertEquals(pages.getTotalElements(), 1);
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
}