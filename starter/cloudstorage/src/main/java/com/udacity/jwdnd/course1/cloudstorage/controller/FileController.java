package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/files")
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;
    private final AuthenticationService authService;

    public FileController(FileService fileService, AuthenticationService authService) {
        this.fileService = fileService;
        this.authService = authService;
    }

    @GetMapping
    public String getAllUserFiles(Model model, Principal principal) {
        if (principal == null || authService.getCurrentUser() == null) {
            return "redirect:/login";
        }
        List<File> fileList = fileService.getAllFiles();
        model.addAttribute("fileList", fileList);
        model.addAttribute("page", "files");
        return "home";
    }


    @GetMapping("/{fileId}")
    public String getFileById(@PathVariable("fileId") Integer fileId, Model model) {
        if (authService.getCurrentUser() == null) {
            return "redirect:/login";
        }
        File file = fileService.getFileById(fileId);
        if (file == null) {
            return "home";
        }
        FileDTO fileDTO = FileDTO.toDTO(file);
        model.addAttribute("file", fileDTO);

        return "home";
    }

    @PostMapping
    public String postFile(@RequestParam("fileUpload") MultipartFile file, Model model) throws IOException {
        model.addAttribute("page", "files");
        if (authService.getCurrentUser() == null) {
            return "redirect:/login";
        }
        if(file.getBytes().length > 10485760){
            String errMsg = String.format("Sorry! The request was rejected because file size (%d) exceeds the configured maximum (%d)", file.getBytes().length, 10485760);
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", errMsg);
            return "result";
        }else if(file.getBytes().length == 0 || file.getOriginalFilename().isBlank()){
            model.addAttribute("error", true);
            String errMsg = String.format("No file was uploaded (file size: %d) or please check file name (filename: %s) if it is available.", file.getBytes().length, file.getOriginalFilename());
            model.addAttribute("errorMsg", errMsg);
            return "result";
        }
        Integer integer = fileService.insertFile(file);
        if (integer != null && integer > 0){
            model.addAttribute("success", true);
            model.addAttribute("successMsg", "File upload was successful!");
        }
        else{
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "File saving failed.");
        }
        return "result";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestBody Map<String, Integer> requestBody) {
        if (authService.getCurrentUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("data", "Unauthorized access. Please, login and try again later."));
        }
        Integer fileId = requestBody.get("fileId");
        logger.info("POST request to delete a file with id: {}", fileId);
        Integer rowsUpdated = fileService.deleteById(fileId);
        if (rowsUpdated != null && rowsUpdated > 0) {
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "File deletion was unsuccessful"));
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(value = "fileId") Integer fileId) {
        if (authService.getCurrentUser() == null) {
            throw new RuntimeException("Unauthorized access. Please, login and then try!");
        }
        File storedFile = fileService.getFileById(fileId);

        if (storedFile == null) {
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
