package com.areshok.animelz.service;


import com.areshok.animelz.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

public interface UserService {

    boolean addUser(User user);

    boolean activateUser(String code);

    void sendMessage(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    void updateProfileImageByUserId(String userName, String urlImage);

    void userSave(String username, Map<String, String> form, User user);

    void updateProfile(User user, String username, String password, String email);

    User findByUsername(String username);

    User findByActivationCode(String code);

    User save(User user);
}
