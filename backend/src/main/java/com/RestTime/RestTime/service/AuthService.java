package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.LoginRequest;

public interface AuthService {

     AuthResponse authenticate(LoginRequest request) ;

}