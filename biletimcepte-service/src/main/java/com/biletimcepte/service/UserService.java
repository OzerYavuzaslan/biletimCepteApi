package com.biletimcepte.service;

import com.biletimcepte.converter.UserConverter;
import com.biletimcepte.dto.request.LoginRequest;
import com.biletimcepte.dto.request.NotificationRequest;
import com.biletimcepte.dto.request.RegisterRequest;
import com.biletimcepte.dto.response.UpdateResponse;
import com.biletimcepte.dto.response.UserResponse;
import com.biletimcepte.exception.UserNotFoundException;
import com.biletimcepte.model.User;
import com.biletimcepte.repository.IUserRepository;
import com.biletimcepte.util.LoggerUtilization;
import com.biletimcepte.util.PasswordUtil;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;

import static com.biletimcepte.util.Constants.*;

@Data
@Service
public class UserService implements IUserService {
    private IUserRepository iUserRepository;
    private UserConverter userConverter;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public UserService(IUserRepository iUserRepository, UserConverter userConverter, RabbitTemplate rabbitTemplate) {
        setIUserRepository(iUserRepository);
        setUserConverter(userConverter);
        setRabbitTemplate(rabbitTemplate);
    }

    @Override
    public List<UserResponse> listUsers() {
        return getUserConverter().convert(getIUserRepository().getAllUsers("ADMIN"));
    }

    @Override
    public String login(LoginRequest loginRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User specificUser = getIUserRepository()
                    .selectByEmail(loginRequest.getEmail(), "ADMIN")
                    .orElseThrow(() -> new UserNotFoundException("User " + loginRequest.getEmail() + " not found"));

        String hashedPassword = PasswordUtil.preparePasswordHash(loginRequest.getPassword(), loginRequest.getEmail());
        boolean isValid = PasswordUtil.validatePassword(hashedPassword, specificUser.getPassword());
        LoggerUtilization.getLogger().log(Level.INFO, "UserService -> userLogin : " + loginRequest.getEmail() + (isValid ? " success." : " failed."));
        return isValid ? LOGIN_SUCCESSFUL : LOGIN_NOT_SUCCESSFUL;
    }

    @Override
    public String registerUser(RegisterRequest registerRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {
        var checkUser = getIUserRepository()
                .selectByEmail(registerRequest.getEmail(), "ADMIN");

        if (checkUser.isPresent())
            return REGISTER_NOT_SUCCESSFUL + registerRequest.getEmail();
        else {
            String hashedPassword = PasswordUtil.preparePasswordHash(registerRequest.getPassword(), registerRequest.getEmail());
            User user = getIUserRepository().save(getUserConverter().convert(registerRequest, hashedPassword));

            getUserConverter().convert(user);

            LoggerUtilization.getLogger().log(Level.INFO, "UserService -> registerUser: " + registerRequest.getEmail());
            getRabbitTemplate().convertAndSend("notification", new NotificationRequest("User successfully created with email: " + registerRequest.getEmail(), "EMAIL", registerRequest.getEmail()));

            return REGISTER_SUCCESSFUL;
        }
    }

    @Override
    public UpdateResponse updateUser(RegisterRequest registerRequest) throws InvalidKeySpecException, NoSuchAlgorithmException{
        //var isRecordExisted = getIUserRepository().findByEmail(registerRequest.getEmail());
        var isRecordExisted = getIUserRepository().selectByEmail(registerRequest.getEmail(), "ADMIN");

        String updateMessage;

        if (isRecordExisted.isPresent()) {
            User specificUser = isRecordExisted.get();
            updateMessage = USER_FOUND_AND_UPDATED;
            String hashedPassword = PasswordUtil.preparePasswordHash(registerRequest.getPassword(), registerRequest.getEmail());
            getIUserRepository().save(updateUser(specificUser, registerRequest, hashedPassword));
            getRabbitTemplate().convertAndSend("notification", new NotificationRequest("User successfully updated with email: " + registerRequest.getEmail(), "EMAIL", registerRequest.getEmail()));
        }
        else
            updateMessage = USER_NOT_FOUND;

        LoggerUtilization.getLogger().log(Level.INFO, "UserService -> updateUser : " + registerRequest.getEmail());
        return getUserConverter().convert(updateMessage, registerRequest);
    }

    private User updateUser(User specificUser, RegisterRequest registerRequest, String hashedPassword){
        specificUser.setUsername(registerRequest.getUsername());
        specificUser.setPassword(hashedPassword);
        specificUser.setEmail(registerRequest.getEmail());
        specificUser.setName(registerRequest.getName());
        specificUser.setSurname(registerRequest.getSurname());
        specificUser.setAge(registerRequest.getAge());
        specificUser.setGenderType(registerRequest.getGenderType());
        specificUser.setUpdateDate(LocalDateTime.now());
        specificUser.setPhoneNumber(registerRequest.getPhoneNumber());
        specificUser.setUserType(registerRequest.getUserType());

        return specificUser;
    }
}
