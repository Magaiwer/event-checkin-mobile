package dev.magaiver.eventcheckin.domain.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dev.magaiver.eventcheckin.domain.model.converter.LocalDateTimeConverter;

@RequiresApi(api = Build.VERSION_CODES.O)
@Entity
public class Event {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String description;
    private int capacity;

    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime dateTime;

    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime dateClose;

    public Event(@NotNull String id, String name, String description, int capacity, LocalDateTime dateTime, LocalDateTime dateClose) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.dateTime = dateTime;
        this.dateClose = dateClose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateClose() {
        return dateClose;
    }

    public void setDateClose(LocalDateTime dateClose) {
        this.dateClose = dateClose;
    }

    public String dateTimeStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.dateTime.format(formatter);
    }

    public String dateCloseStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.dateClose.format(formatter);
    }

    public String status() {
        return LocalDateTime.now().isBefore(this.dateTime) ? "OPEN" : "CLOSE";
    }
}
