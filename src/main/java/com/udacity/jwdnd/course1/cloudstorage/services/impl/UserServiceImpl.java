package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.HashService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserServiceImpl(UserMapper userMapper, HashService hashService){
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public User findUserById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public Integer insertUser(User user) {
        User userExists = userMapper.getUserByUsername(user.getUsername());
        if(userExists == null){
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            System.out.println("salt: ");
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
//            System.out.println("encodedSalt: " + encodedSalt);
            String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
//            System.out.println("hashedPassword: " + hashedPassword);
            return userMapper.insertUser(new User(user.getUsername(), encodedSalt, hashedPassword, user.getFirstname(), user.getLastname()));
        }
        return -1;
    }
}
