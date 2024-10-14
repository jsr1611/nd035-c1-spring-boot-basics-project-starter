package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.apache.ibatis.annotations.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private final NoteService noteService;
    private final AuthenticationService authService;
    private final Logger logger = LoggerFactory.getLogger(NotesController.class);

    public NotesController(NoteService noteService, AuthenticationService authService) {
        this.noteService = noteService;
        this.authService = authService;
    }

    @GetMapping
    public String getAllNotes(Model model, Principal principal){
        if(principal == null || authService.getCurrentUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("noteList", noteService.findAllNotes());
        model.addAttribute("page", "notes");
        return "home";
    }

    @PostMapping
    public String insertOrUpdateNote(@ModelAttribute Note note, Model model){
        if(authService.getCurrentUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("page", "notes");
        if(note != null && !note.getNoteTitle().isBlank()){
            if(note.getNoteId() != null){
                noteService.updateNote(note);
            }else {
                noteService.insertNote(note);
            }
            model.addAttribute("success", true);
        }else {
            model.addAttribute("error", true);
        }
        return "redirect:notes";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteNote(@RequestBody Map<String, Integer> requestBody){
        if(authService.getCurrentUser() == null){
            Map<String, String> data = new HashMap<>();
            data.put("data", "Unauthorized access. Please, login and try again later.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data);
        }

        Integer noteId = requestBody.get("noteId");
        logger.info("POST request to delete a note with id: {}", noteId);
        Integer rowsUpdated = noteService.deleteById(noteId);
        if (rowsUpdated != null && rowsUpdated > 0) {
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Note deletion was unsuccessful"));
        }
    }
}
