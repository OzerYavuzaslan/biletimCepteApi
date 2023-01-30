package com.biletimcepte.repository;

import com.biletimcepte.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    @Query(nativeQuery = true, value = "select * from users where email = :email and user_status = :userstatus order by id")
    Optional<User> checkTheUserAdmin(String email, String userstatus);

    @Query(nativeQuery = true, value = "select * from users where email = :email and user_status <> :userstatus order by id")
    Optional<User> selectByEmail(String email, String userstatus);

    @Query(nativeQuery = true, value = "select * from users where user_status <> :userstatus order by id")
    List<User> getAllUsers(String userstatus);
}
