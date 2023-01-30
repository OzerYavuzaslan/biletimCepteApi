package com.biletimcepte.controller;

import com.biletimcepte.dto.request.LoginRequest;
import com.biletimcepte.dto.request.RegisterRequest;
import com.biletimcepte.dto.response.UserResponse;
import com.biletimcepte.dto.response.UpdateResponse;
import com.biletimcepte.service.IUserService;
import lombok.Data;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Data
@RestController
@RequestMapping("/users")
public class UserController {
    private IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService){
        setIUserService(iUserService);
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) throws InvalidKeySpecException, NoSuchAlgorithmException, PSQLException {
        return ResponseEntity.ok(getIUserService().registerUser(registerRequest));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listOfUsers(){
        return ResponseEntity.ok(getIUserService().listUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return ResponseEntity.ok(getIUserService().login(loginRequest));
    }

    @PutMapping
    public ResponseEntity<UpdateResponse> update(@RequestBody RegisterRequest registerRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return ResponseEntity.ok(getIUserService().updateUser(registerRequest));
    }
}
