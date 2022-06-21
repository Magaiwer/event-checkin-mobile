package dev.magaiver.eventcheckin.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import kotlin.jvm.Transient;

@Entity
public class User implements Serializable {

    @PrimaryKey
    @NonNull
    private String uuid;
    private String name;
    private String email;
    @Ignore
    private String password;

    public User(@NonNull String uuid, String name, String email) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
