package by.bstu.razvod.lab3;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class Note {
    NoteDate date;
    String noteText;

    public NoteDate getDate()
    {
        return date;
    }

    public String getNoteText()
    {
        return noteText;
    }

    public Note(NoteDate date, String noteText){
        this.date = date;
        this.noteText = noteText;
    }
}

