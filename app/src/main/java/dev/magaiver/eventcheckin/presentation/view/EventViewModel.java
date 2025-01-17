package dev.magaiver.eventcheckin.presentation.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.magaiver.eventcheckin.domain.model.Event;
import dev.magaiver.eventcheckin.domain.repository.EventRepository;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository = new EventRepository(application);
    }

    public void insertAll(List<Event> events) {
        eventRepository.insertAll(events);
    }

    public void deleteAll() {
        eventRepository.deleteAll();
    }

    public Event findById(String id) {
        return eventRepository.findById(id);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public LiveData<List<Event>> findAllLiveData() {
        return eventRepository.findAllLiveData();
    }

    public void syncEventsServer() {
        eventRepository.syncEvents();
    }
}
