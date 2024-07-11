package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface FileService {
    File getFileById(Integer id);
    List<File> getFilesByContentTypeAndUsername(String contentType);
    List<File> getAllFilesByUsername();
    Integer insertFile(MultipartFile file);

    File findFileByName(String filename);

    Integer delete(Integer fileId);
}
