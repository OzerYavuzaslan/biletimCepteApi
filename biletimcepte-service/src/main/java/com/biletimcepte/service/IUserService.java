package com.biletimcepte.service;

import com.biletimcepte.dto.request.LoginRequest;
import com.biletimcepte.dto.request.RegisterRequest;
import com.biletimcepte.dto.response.UserResponse;
import com.biletimcepte.dto.response.UpdateResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface IUserService {
    List<UserResponse> listUsers();
    String login(LoginRequest loginRequest) throws InvalidKeySpecException, NoSuchAlgorithmException;
    String registerUser(RegisterRequest registerRequest) throws InvalidKeySpecException, NoSuchAlgorithmException;
    UpdateResponse updateUser(RegisterRequest registerRequest) throws InvalidKeySpecException, NoSuchAlgorithmException;
}
