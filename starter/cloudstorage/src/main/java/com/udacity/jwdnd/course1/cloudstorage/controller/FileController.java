package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/{fileId}")
    public String getFileById(@PathVariable("fileId") Integer fileId, Model model){
        File file = fileService.getFileById(fileId);
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


    @PostMapping("/delete")
    public String deleteFileById(@RequestBody Map requestBody, Model model){

        Integer fileId = (Integer) requestBody.get("fileId");
        logger.info("fileId: {}", fileId);
        Integer rowsUpdated = fileService.delete(fileId);
        if(rowsUpdated != null && rowsUpdated > 0){
            model.addAttribute("success", true);
        }else {
            model.addAttribute("error", "File deletion was unsuccessful");
        }
        return "home";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(value = "fileId") Integer fileId) {
        File storedFile = fileService.getFileById(fileId);

            if(storedFile == null){
                throw new RuntimeException("File not found with id: " + fileId);
            }
                byte[] fileData = storedFile.getFileData();
                ByteArrayInputStream bis = new ByteArrayInputStream(fileData);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", storedFile.getFileName());
                headers.setContentLength(fileData.length);
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .body(new InputStreamResource(bis));
    }

}
