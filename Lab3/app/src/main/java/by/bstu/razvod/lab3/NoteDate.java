package by.bstu.razvod.lab3;

import java.util.Objects;

public class NoteDate {

    int day;
    int month;
    int year;

    public NoteDate(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteDate noteDate = (NoteDate) o;
        return day == noteDate.day &&
                month == noteDate.month &&
                year == noteDate.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }
}
