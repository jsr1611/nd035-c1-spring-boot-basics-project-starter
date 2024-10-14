package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM Credentials WHERE credentialid = #{id} and userid = #{userId}")
    Credential findById(Integer id, Integer userId);

    @Select("SELECT * FROM Credentials WHERE userid = #{userId}")
    List<Credential> findAllCredentials(Integer userId);

    @Insert("INSERT INTO Credentials (url, username, ckey, password, userid) VALUES (#{url}, #{username}," +
            " #{ckey}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Integer insertCredential(Credential credential);

    @Update("UPDATE Credentials SET url = #{url}, username = #{username}, ckey = #{ckey}, password = #{password} WHERE userid = #{userId} and credentialid = #{credentialId}")
    Integer updateCredential(Credential credential);

    @Delete("DELETE FROM Credentials WHERE credentialid = #{credentialId} and userid = #{userId}")
    Integer deleteById(Integer credentialId, Integer userId);
}
