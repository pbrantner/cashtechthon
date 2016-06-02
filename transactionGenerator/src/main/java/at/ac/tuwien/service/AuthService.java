package at.ac.tuwien.service;

import at.ac.tuwien.domain.LoginRequest;

public interface AuthService {
    void authenticate(LoginRequest request);
}
