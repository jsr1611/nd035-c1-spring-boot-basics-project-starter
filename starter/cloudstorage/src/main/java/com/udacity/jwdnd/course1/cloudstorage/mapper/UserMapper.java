package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM Users WHERE userId = #{userId}")
    User findById(Integer userId);

    @Select("SELECT * FROM Users")
    List<User> getAllUsers();

    @Insert("INSERT INTO Users (username, salt, password, firstname, lastname) VALUES (#{username}, #{salt}, #{password}" +
            ",#{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer insertUser(User user);

    @Select("SELECT * FROM Users WHERE username = #{username}")
    User getUserByUsername(String username);
}
