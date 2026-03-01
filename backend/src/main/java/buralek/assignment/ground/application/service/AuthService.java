package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.LoginRequest;
import buralek.assignment.ground.application.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
}
