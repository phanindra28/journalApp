package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")  // It is the path after the base URL for this controller
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryService.getAllJournalEntries();
    }

    @PostMapping
    public JournalEntry createJournalEntry(@RequestBody JournalEntry journalEntry) {  // @Request Body is like saying spring to take data from the request & convert it into a JAVA Object
        journalEntry.setCreatedDate(LocalDateTime.now());
        journalEntryService.saveJournalEntry(journalEntry);
        return journalEntry;
    }

    @GetMapping("/id/{id}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId id) {  // @PathVariable is used to bind the URI template variable to the method parameter
        return journalEntryService.getJournalEntryById(id).orElse(null);

    }

    @DeleteMapping("/id/{id}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId id) {
        journalEntryService.deleteJournalEntryById(id);
        return true;
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry) {
        JournalEntry old = journalEntryService.getJournalEntryById(id).orElse(null);
        if (old != null) {
            old.setTitle(journalEntry.getTitle() != null ? journalEntry.getTitle() : old.getTitle());
            old.setContent(journalEntry.getContent() != null ? journalEntry.getContent() : old.getContent());
        }
        journalEntryService.saveJournalEntry(old);
        return old;
    }
}
