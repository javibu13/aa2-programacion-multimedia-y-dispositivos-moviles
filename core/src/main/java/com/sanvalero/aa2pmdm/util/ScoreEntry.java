package com.sanvalero.aa2pmdm.util;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreEntry {
    private String name;
    private int score;
    private float time;
    private String dateTime;
    private transient boolean isNewEntry = false;

    public ScoreEntry(String name, int score, float time, LocalDateTime dateTime) {
        this.name = name;
        this.score = score;
        this.time = time;
        this.dateTime = dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ScoreEntry(String name, int score, float time, LocalDateTime dateTime, boolean isNewEntry) {
        this.name = name;
        this.score = score;
        this.time = time;
        this.dateTime = dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.isNewEntry = isNewEntry;
    }

    @Override
    public String toString() {
        return name + " (" + dateTime + ") | " + score + " points | " + formatPlayerTime();
    }

    public String formatPlayerTime() {
        int hours = (int) time / 3600;
        int minutes = (int) (time % 3600) / 60;
        int seconds = (int) time % 60;
        int milliseconds = (int) ((time - (int) time) * 1000);
        if (hours > 0) {
            return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
        } else {
            return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
        }
    }
}
