package dev.magaiver.eventcheckin.domain.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity(primaryKeys = {"eventId", "userEmail"})
public class Subscription implements Serializable {

    @NotNull private String eventId;
    @NotNull private String userEmail;
    @NotNull private String status;

    public Subscription(@NotNull String eventId, @NotNull String userEmail, @NonNull String status) {
        this.eventId = eventId;
        this.userEmail = userEmail;
        this.status = status;
    }

    public String getEventId() {
        return eventId;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public String getStatus() {
        return status;
    }

}
