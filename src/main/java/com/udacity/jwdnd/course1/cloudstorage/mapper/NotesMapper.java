package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Select("SELECT * FROM Notes WHERE noteid = #{id}")
    Note findById(Integer id, Integer userId);

    @Select("SELECT * FROM Notes WHERE userid = #{userId}")
    List<Note> findAllNotes(Integer userId);

    @Insert("INSERT INTO Notes (notetitle, notedescription, userid) VALUES (#{noteTitle}, #{noteDescription}," +
            " #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insertNote(Note note);

    @Update("UPDATE Notes SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE userid = #{userId} and noteid = #{noteId}")
    Integer updateNote(Note note);

    @Delete("DELETE FROM Notes WHERE noteid = #{noteId} and userid = #{userId}")
    Integer deleteById(Integer noteId, Integer userId);
}
