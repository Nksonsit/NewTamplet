package com.drkeironbrown.lifecoach.model;

import java.io.Serializable;

public class Journal implements Serializable{
    private int JournalId;
    private String Journal;

    public int getJournalId() {
        return JournalId;
    }

    public void setJournalId(int journalId) {
        JournalId = journalId;
    }

    public String getJournal() {
        return Journal;
    }

    public void setJournal(String journal) {
        Journal = journal;
    }
}
