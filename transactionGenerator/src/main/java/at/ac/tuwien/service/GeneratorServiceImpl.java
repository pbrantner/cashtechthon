package at.ac.tuwien.service;

import at.ac.tuwien.shared.dtos.Classification;
import at.ac.tuwien.shared.dtos.LoginRequest;
import at.ac.tuwien.persistence.ClassificationPersistence;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneratorServiceImpl implements GeneratorService {
    private final static Logger logger = Logger.getLogger(GeneratorServiceImpl.class);
    @Autowired
    private ClassificationGenerator classificationGenerator;
    @Autowired
    private ClassificationPersistence classificationPersistence;
    @Autowired
    private AuthService authService;
    private String username = "patrick"; //TODO: autowire and move to properties file
    private String password = "password"; //TODO: autowire and move to properties file
    private int interval = 5000; //TODO: autowire and move to properties file

    @Override
    public void start() throws InterruptedException {
        logger.debug("Generator started");

        authService.authenticate(new LoginRequest(username, password));

        while(true) {
            List<Classification> c = classificationGenerator.generate();
            classificationPersistence.save(c);

            logger.debug("Generated " + c.toString());

            Thread.sleep(interval);
        }
    }
}
