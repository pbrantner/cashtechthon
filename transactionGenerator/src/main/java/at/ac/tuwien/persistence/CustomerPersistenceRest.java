package at.ac.tuwien.persistence;

import at.ac.tuwien.service.HttpSingleton;
import at.ac.tuwien.shared.dtos.Customer;
import at.ac.tuwien.shared.dtos.ExtendedCustomer;
import at.ac.tuwien.shared.util.PropertiesReader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class CustomerPersistenceRest implements CustomerPersistence {
    private final static Logger logger = Logger.getLogger(ClassificationPersistenceRest.class);

    @Autowired
    private HttpSingleton httpSingleton;
    private String path;
    private RestTemplate template;

    @PostConstruct
    private void init() {
        String endpoint = new PropertiesReader().getString("endpoint.customer");
        path = httpSingleton.getPath() + endpoint;
        template = httpSingleton.getRestTemplate();
    }

    @Override
    public void save(List<ExtendedCustomer> cs) {
        HttpHeaders headers = httpSingleton.getHttpHeaders();
        for (Customer c : cs) {
            try {
                HttpEntity<Customer> entity = new HttpEntity<>(c, headers);
                ResponseEntity<Customer> response = template.postForEntity(path, entity, null);

                c.setId(response.getBody().getId());

                logger.debug("Saved customer via REST - response: " + response.getStatusCode().toString());
            } catch (HttpClientErrorException e) {
                logger.error("Saving customer via REST failed and resulted in HttpStatus " + e.getStatusText().toString());
            } catch (ResourceAccessException e) {
                logger.error("Saving customer via REST failed since Server was not available");
            }
        }
    }
}
