package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File getFileById(Integer id);

    List<File> getAllFiles();

    Integer insertFile(MultipartFile file);

    Integer deleteById(Integer fileId);

    File findByFilename(String originalFilename);
}
