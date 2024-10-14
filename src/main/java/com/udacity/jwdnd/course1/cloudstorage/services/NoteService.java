package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import java.util.List;

public interface NoteService {
    Note findById(Integer id);

    List<Note> findAllNotes();

    Integer insertNote(Note note);

    Integer updateNote(Note note);

    Integer deleteById(Integer noteId);
}
