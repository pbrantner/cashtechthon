package at.ac.tuwien.service;

import at.ac.tuwien.shared.dtos.LoginRequest;
import at.ac.tuwien.shared.dtos.TokenLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceToken implements AuthService {
    @Autowired
    private HttpSingleton httpSingleton;

    @Override
    public void authenticate(LoginRequest request) {
        String path = httpSingleton.getPath() + "login/api";
        RestTemplate template = httpSingleton.getRestTemplate();
        HttpHeaders headers = httpSingleton.getHttpHeaders();
        HttpEntity<LoginRequest> entity = new HttpEntity<LoginRequest>(request, headers);

        TokenLoginResponse response = template.postForObject(path, entity, TokenLoginResponse.class);

        httpSingleton.addHeader("Authorization", "Bearer " + response.getToken());
    }
}
