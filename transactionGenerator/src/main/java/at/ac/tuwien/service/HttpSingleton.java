package at.ac.tuwien.service;

import at.ac.tuwien.shared.util.PropertiesReader;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Scope("singleton")
public class HttpSingleton {
    private HttpHeaders headers;
    private String path;

    public HttpSingleton() {
        headers = new HttpHeaders();
        path = new PropertiesReader().getString("path");
    }

    public String getPath() {
        return path;
    }

    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public HttpHeaders getHttpHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        headers.add(key, value);
    }

    public void removeHeader(String key) {
        headers.remove(key);
    }
}
