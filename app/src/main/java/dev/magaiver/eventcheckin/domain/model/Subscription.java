package dev.magaiver.eventcheckin.domain.model;


import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity(primaryKeys = {"eventId", "userEmail"})
public class Subscription implements Serializable {

    @NotNull private String eventId;
    @NotNull private String userEmail;

    public Subscription(@NotNull String eventId, @NotNull String userEmail) {
        this.eventId = eventId;
        this.userEmail = userEmail;
    }

    public String getEventId() {
        return eventId;
    }

    public String getUserEmail() {
        return userEmail;
    }

}
