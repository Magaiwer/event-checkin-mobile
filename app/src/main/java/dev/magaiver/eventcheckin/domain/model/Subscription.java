package dev.magaiver.eventcheckin.domain.model;

public class Subscription {

    private String id;
    private String eventId;
    private String userEmail;

    public Subscription(String id, String eventId, String userEmail) {
        this.id = id;
        this.eventId = eventId;
        this.userEmail = userEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
