package com.secure.docs.service;

import com.secure.docs.model.Note;
import com.secure.docs.repository.NoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public Note createNoteForUsers(String username, String content){
            Note note = new Note();
            note.setContent(content);
            note.setOwnerUsername(username);
            Note savedNote = noteRepository.save(note);
            return savedNote;
    }

    public Note updateNoteForUsers(Long noteId ,String userName, String content ){
            Note note= noteRepository.findById(noteId).orElseThrow(()-> new RuntimeException("Note Not Found"));
            note.setContent(content);
            Note updatedNote = noteRepository.save(note);
            return updatedNote;
    }

    public void deleteNoteForUsers(Long noteId, String username){
            noteRepository.deleteById(noteId);
    }

    public List<Note> getNotesForUser(String username){
        List<Note> personalNotes= noteRepository.findByOwnerUsername(username);
        return personalNotes;
    }

}
