package com.secure.docs.controller;

import com.secure.docs.model.Note;
import com.secure.docs.service.NoteService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public Note creatNote(@RequestBody String content,
                          //inject currently logged users in to the controller
                          @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        System.out.println("user details: "+ username);
        return noteService.createNoteForUsers(content,username);
    }

    // retrieve notes of particular user
    @GetMapping
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails){
        String username= userDetails.getUsername();
        return noteService.getNotesForUser(username);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNode(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        noteService.deleteNoteForUsers(noteId,username);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                          @RequestBody String content,
                          @AuthenticationPrincipal UserDetails userDetails){

        String username= userDetails.getUsername();
        return noteService.updateNoteForUsers(noteId,content,username);
    }


}
