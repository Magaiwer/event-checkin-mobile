package dev.magaiver.eventcheckin.domain.repository;

import android.app.Application;

import java.util.List;

import dev.magaiver.eventcheckin.core.dao.EventDao;
import dev.magaiver.eventcheckin.core.database.AppDatabase;
import dev.magaiver.eventcheckin.domain.model.Event;

public class EventRepository {

    private EventDao eventDao;

    public EventRepository(Application application) {
        AppDatabase db = AppDatabase.getConnection(application);
        this.eventDao = db.eventDao();
    }

    public void insertAll(final List<Event> events) {
        AppDatabase.getDbWriteExecutor().execute( () -> eventDao.insertAll(events));
    }

    public void deleteAll() {
        AppDatabase.getDbWriteExecutor().execute( () -> eventDao.deleteAll());
    }

    public Event findById(final String id) {
        return this.eventDao.findById(id);
    }

    public List<Event> findAll() {
        return this.eventDao.findAll();
    }
}

