package dev.magaiver.eventcheckin.core.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.magaiver.eventcheckin.core.dao.EventDao;
import dev.magaiver.eventcheckin.core.dao.SubscriptionDao;
import dev.magaiver.eventcheckin.domain.model.Event;
import dev.magaiver.eventcheckin.domain.model.Subscription;

@Database(entities = {Event.class, Subscription.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract SubscriptionDao subscriptionDao();

    private static AppDatabase INSTANCE;
    static final ExecutorService dbWriteExecutor = Executors.newFixedThreadPool(3);

    public static AppDatabase getConnection(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "event.db").build();
            }
        }
        return INSTANCE;
    }

    public static ExecutorService getDbWriteExecutor() {
        return dbWriteExecutor;
    }
}
