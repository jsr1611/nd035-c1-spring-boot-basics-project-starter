package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final AuthenticationService authService;
    private final FileMapper fileMapper;

    public FileServiceImpl(AuthenticationService authService, FileMapper fileMapper){
        this.authService = authService;
        this.fileMapper = fileMapper;
    }

    @Override
    public File getFileById(Integer id) {
        return fileMapper.findById(id);
    }

    @Override
    public List<File> getFilesByContentTypeAndUsername(String contentType) {
        return fileMapper.findAllFilesByContentTypeAndUsername(contentType, authService.getCurrentUser().getUserId());
    }

    @Override
    public List<File> getAllFilesByUsername() {
        try {
            return fileMapper.findAllFilesByUsername(authService.getCurrentUser().getUserId());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer insertFile(MultipartFile file){
        try {
            File file1 = new File();
            file1.setFileName(file.getOriginalFilename());
            file1.setFileSize(String.valueOf(file.getSize()));
            file1.setContentType(file.getContentType());
            User currentUser = authService.getCurrentUser();
            Integer userId = null;
            if(currentUser != null){
                userId = currentUser.getUserId();
            }else {
                System.out.println("User info not found. Logout and login please");
                return -1;
            }
            file1.setUserId(userId);
            byte[] fileData = file.getBytes();
            file1.setFileData(fileData);
            return fileMapper.insertFile(file1);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public File findFileByName(String filename) {
        return fileMapper.findByName(filename, authService.getCurrentUser().getUserId());
    }

    @Override
    public Integer delete(Integer fileId) {
        try {
            return fileMapper.deleteById(fileId);
        }catch (Exception e){
            e.printStackTrace();
            return -1;

        }
    }
}
