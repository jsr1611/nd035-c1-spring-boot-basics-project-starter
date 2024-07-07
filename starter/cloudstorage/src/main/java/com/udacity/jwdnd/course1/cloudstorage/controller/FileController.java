package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;

    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    @GetMapping("/{filename}")
    public String getFileByName(@PathVariable("filename") String filename, Model model){
        File file = fileService.findFileByName(filename);
        if(file == null){
            return "home";
        }
        FileDTO fileDTO = FileDTO.toDTO(file);
        model.addAttribute("file", fileDTO);
        return "home";
    }

    @PostMapping
    public String postFile(@RequestParam("fileUpload") MultipartFile file, Model model) {
        // TODO: 7/8/24 file store logic here
        Integer integer = fileService.insertFile(file);
        if(integer != null && integer > 0)
            model.addAttribute("uploadSuccess", true);
        else
            model.addAttribute("uploadError", "File saving failed.");
        return "redirect:files";
    }

    @GetMapping
    public String getAllUserFiles(Model model){
        // TODO: 7/8/24 return user files logic here
        List<File> fileList = fileService.getAllFilesByUsername();
        model.addAttribute("fileList", fileList);
        return "home";
    }
}
