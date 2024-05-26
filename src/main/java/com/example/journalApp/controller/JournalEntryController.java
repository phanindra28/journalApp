package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")  // It is the path after the base URL for this controller
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntries() {
        List<JournalEntry> allJournalEntries = journalEntryService.getAllJournalEntries();
        if(allJournalEntries == null || allJournalEntries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allJournalEntries, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {  // @Request Body is like saying spring to take data from the request & convert it into a JAVA Object
        try{
            journalEntry.setCreatedDate(LocalDateTime.now());
            journalEntryService.saveJournalEntry(journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {  // @PathVariable is used to bind the URI template variable to the method parameter
        Optional<JournalEntry> journalEntryById = journalEntryService.getJournalEntryById(id);
        if(journalEntryById.isPresent()) {
            return new ResponseEntity<>(journalEntryById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id) {
        journalEntryService.deleteJournalEntryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry) {
        JournalEntry old = journalEntryService.getJournalEntryById(id).orElse(null);
        if (old != null) {
            old.setTitle(journalEntry.getTitle() != null ? journalEntry.getTitle() : old.getTitle());
            old.setContent(journalEntry.getContent() != null ? journalEntry.getContent() : old.getContent());
            journalEntryService.saveJournalEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
