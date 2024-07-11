package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM Files WHERE fileid = #{id}")
    File findById(Integer id);

    @Select("SELECT * FROM Files WHERE userid = #{userId}")
    List<File> findAllFilesByUsername(Integer userId);

    @Select("SELECT * FROM Files WHERE userid = #{userId} AND contenttype = #{contentType}")
    List<File> findAllFilesByContentTypeAndUsername(String contentType, Integer userId);

    @Insert("INSERT INTO Files (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}," +
            " #{fileSize}, #{userId}, #{fileData, typeHandler=com.udacity.jwdnd.course1.cloudstorage.utils.BlobTypeHandler})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(File file);

    @Select("SELECT * FROM Files WHERE filename = #{filename} AND userid = #{userId}")
    File findByName(String filename, Integer userId);

    @Delete("DELETE FROM Files WHERE fileId = #{fileId}")
    Integer deleteById(Integer fileId);
}
