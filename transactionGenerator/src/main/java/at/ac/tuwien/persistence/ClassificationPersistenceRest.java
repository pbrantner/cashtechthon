package at.ac.tuwien.persistence;

import at.ac.tuwien.shared.dtos.Classification;
import at.ac.tuwien.service.HttpSingleton;
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
public class ClassificationPersistenceRest implements ClassificationPersistence {
    private final static Logger logger = Logger.getLogger(ClassificationPersistenceRest.class);

    @Autowired
    private HttpSingleton httpSingleton;
    private String path;
    private RestTemplate template;

    @PostConstruct
    private void init() {
        path = httpSingleton.getPath() + "api/classification";
        template = httpSingleton.getRestTemplate();
    }

    @Override
    public void save(List<Classification> cs) {
        HttpHeaders headers = httpSingleton.getHttpHeaders();
        for (Classification c : cs) {
            try {
                HttpEntity<Classification> entity = new HttpEntity<>(c, headers);
                ResponseEntity<?> response = template.postForEntity(path, entity, null);

                logger.debug("Saved transaction via REST - response: " + response.getStatusCode().toString());
            } catch (HttpClientErrorException e) {
                logger.error("Saving transaction via REST failed and resulted in HttpStatus " + e.getStatusText().toString());
            } catch (ResourceAccessException e) {
                logger.error("Saving transaction via REST failed since Server was not available");
            }
        }
    }
}
