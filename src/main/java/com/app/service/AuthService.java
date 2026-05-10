package com.app.service;

import com.app.dto.LoginRequest;
import com.app.dto.LoginResponse;


public interface AuthService {

   

     LoginResponse login(LoginRequest request) ;
}