package at.ac.tuwien.service;

import at.ac.tuwien.shared.dtos.LoginRequest;

public interface AuthService {
    void authenticate(LoginRequest request);
}
