package at.ac.tuwien.service;

import at.ac.tuwien.persistence.CustomerPersistence;
import at.ac.tuwien.shared.dtos.Classification;
import at.ac.tuwien.shared.dtos.Customer;
import at.ac.tuwien.shared.dtos.ExtendedCustomer;
import at.ac.tuwien.shared.dtos.LoginRequest;
import at.ac.tuwien.persistence.ClassificationPersistence;
import at.ac.tuwien.shared.util.PropertiesReader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class GeneratorServiceImpl implements GeneratorService {
    private final static Logger logger = Logger.getLogger(GeneratorServiceImpl.class);
    @Autowired
    private ClassificationGenerator classificationGenerator;
    @Autowired
    private ClassificationPersistence classificationPersistence;
    @Autowired
    private CustomerPersistence customerPersistence;
    @Autowired
    private AuthService authService;
    private String username;
    private String password;
    private int interval;

    @PostConstruct
    private void init() {
        PropertiesReader pr = new PropertiesReader();
        username = pr.getString("username");
        password = pr.getString("password");
        interval = pr.getInt("interval");
    }

    @Override
    public void start() throws InterruptedException {
        logger.debug("Generator started");

        if (!authService.authenticate(new LoginRequest(username, password))) {
            logger.debug("Stopping GeneratorService since authentication failed");
            return;
        }

        List<ExtendedCustomer> customers = classificationGenerator.getCustomers();
        customerPersistence.save(customers);

        while (true) {
            List<Classification> c = classificationGenerator.generate();
            classificationPersistence.save(c);

            logger.debug("Generated " + c.toString());

            Thread.sleep(interval);
        }
    }
}
