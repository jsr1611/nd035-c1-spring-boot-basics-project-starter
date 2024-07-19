package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NotesMapper noteMapper;
    private final AuthenticationService authService;

    public NoteServiceImpl(NotesMapper noteMapper, AuthenticationService authService) {
        this.noteMapper = noteMapper;
        this.authService = authService;
    }

    @Override
    public Note findById(Integer id) {
        return noteMapper.findById(id, authService.getCurrentUser().getUserId());
    }

    @Override
    public List<Note> findAllNotes() {
        return noteMapper.findAllNotes(authService.getCurrentUser().getUserId());
    }

    @Override
    public Integer insertNote(Note note) {
        note.setUserId(authService.getCurrentUser().getUserId());
        return noteMapper.insertNote(note);
    }

    @Override
    public Integer updateNote(Note note) {
        note.setUserId(authService.getCurrentUser().getUserId());
        return noteMapper.updateNote(note);
    }

    @Override
    public Integer deleteById(Integer noteId) {
        return noteMapper.deleteById(noteId, authService.getCurrentUser().getUserId());
    }
}
