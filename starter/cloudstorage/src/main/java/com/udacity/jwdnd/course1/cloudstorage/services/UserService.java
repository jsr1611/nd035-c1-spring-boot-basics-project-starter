package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.User;

public interface UserService {

    User getUserByUsername(String username);

    User findUserById(Integer id);

    Integer insertUser(User user);
}
