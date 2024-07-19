package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM Files WHERE fileid = #{id} AND userid = #{userId}")
    File findById(Integer id, Integer userId);

    @Select("SELECT * FROM Files WHERE userid = #{userId}")
    List<File> findAllFiles(Integer userId);

    @Insert("INSERT INTO Files (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}," +
            " #{fileSize}, #{userId}, #{fileData, typeHandler=com.udacity.jwdnd.course1.cloudstorage.utils.BlobTypeHandler})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(File file);

    @Delete("DELETE FROM Files WHERE fileId = #{fileId} AND userid = #{userId}")
    Integer deleteById(Integer fileId, Integer userId);
}
