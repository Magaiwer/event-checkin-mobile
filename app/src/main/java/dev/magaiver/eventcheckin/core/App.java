package dev.magaiver.eventcheckin.core;

import android.app.Application;

public class App extends Application {
    static App instance;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
