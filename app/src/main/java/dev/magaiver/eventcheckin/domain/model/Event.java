package dev.magaiver.eventcheckin.domain.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dev.magaiver.eventcheckin.domain.model.converter.LocalDateTimeConverter;

@RequiresApi(api = Build.VERSION_CODES.O)
@Entity
public class Event implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String description;
    private String location;
    private int capacity;

    @Ignore
    private boolean subscribed;

    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime dateTime;

    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime dateTimeClose;

    public Event(@NotNull String id, String name, String description, int capacity, LocalDateTime dateTime, LocalDateTime dateTimeClose, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.dateTime = dateTime;
        this.dateTimeClose = dateTimeClose;
        this.location = location;
    }

    public Event() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public LocalDateTime getDateTimeClose() {
        return dateTimeClose;
    }

    public void setDateTimeClose(LocalDateTime dateTimeClose) {
        this.dateTimeClose = dateTimeClose;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String dateTimeStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.dateTime.format(formatter);
    }

    public String dateCloseStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.dateTimeClose.format(formatter);
    }

    public String status() {
        return isOpen() ? "OPEN" : "CLOSE";
    }

    public boolean isOpen() {
        return LocalDateTime.now().isBefore(this.dateTimeClose);
    }

    public String getLatitude() {
        return this.location != null ? this.location.split(",")[0]:"";
    }

    public String getLongitude() {
        return this.location != null ? this.location.split(",")[1]:"";
    }

    public boolean matchLatitude(Double lat) {
        BigDecimal near = new BigDecimal(lat).setScale(3, RoundingMode.DOWN);
        return getLatitude().startsWith(String.valueOf(near));
    }

    public boolean matchLongitude(Double longi) {
        BigDecimal near = new BigDecimal(longi).setScale(3, RoundingMode.DOWN);
        return getLongitude().startsWith(String.valueOf(near));
    }
}
