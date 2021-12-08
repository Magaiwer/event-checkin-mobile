package dev.magaiver.eventcheckin.api;

public enum StatusCode {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404);

    StatusCode(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
