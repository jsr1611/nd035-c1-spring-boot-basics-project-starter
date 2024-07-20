package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;
    private final AuthenticationService authService;

    public FileServiceImpl(AuthenticationService authService, FileMapper fileMapper){
        this.authService = authService;
        this.fileMapper = fileMapper;
    }

    @Override
    public File getFileById(Integer id) {
        return fileMapper.findById(id, authService.getCurrentUser().getUserId());
    }

    @Override
    public List<File> getAllFiles() {
        try {
            return fileMapper.findAllFiles(authService.getCurrentUser().getUserId());
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
            file1.setContentType(file.getContentType());
            file1.setUserId(authService.getCurrentUser().getUserId());
            byte[] fileData = file.getBytes();
            file1.setFileData(fileData);
            file1.setFileSize(String.valueOf(file.getSize()));
            return fileMapper.insertFile(file1);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Integer deleteById(Integer fileId) {
        try {
            return fileMapper.deleteById(fileId, authService.getCurrentUser().getUserId());
        }catch (Exception e){
            e.printStackTrace();
            return -1;

        }
    }

    @Override
    public File findByFilename(String originalFilename) {
        return fileMapper.findByFilename(originalFilename, authService.getCurrentUser().getUserId());
    }
}
