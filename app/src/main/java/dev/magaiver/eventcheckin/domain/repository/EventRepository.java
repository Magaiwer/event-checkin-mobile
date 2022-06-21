package dev.magaiver.eventcheckin.domain.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fasterxml.jackson.core.type.TypeReference;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import dev.magaiver.eventcheckin.api.ApiTemplate;
import dev.magaiver.eventcheckin.api.Routes;
import dev.magaiver.eventcheckin.api.StatusCode;
import dev.magaiver.eventcheckin.core.dao.EventDao;
import dev.magaiver.eventcheckin.core.database.AppDatabase;
import dev.magaiver.eventcheckin.domain.model.Event;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EventRepository {

    private final EventDao eventDao;

    public EventRepository(Application application) {
        AppDatabase db = AppDatabase.getConnection(application);
        this.eventDao = db.eventDao();
    }

    public void insertAll(final List<Event> events) {
        AppDatabase.getDbWriteExecutor().execute(() -> eventDao.insertAll(events));
    }

    public void deleteAll() {
        AppDatabase.getDbWriteExecutor().execute(eventDao::deleteAll);
    }

    public Event findById(final String id) {
        return this.eventDao.findById(id);
    }

    public List<Event> findAll() {
        return this.eventDao.findAll();
    }

    public LiveData<List<Event>> findAllLiveData() {
        return this.eventDao.findAllLiveData();
    }

    public void syncEvents() {
        try {
            ApiTemplate.get(Routes.URL_EVENT, "", "").enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    List<Event> events = ApiTemplate.getObjectMapper().readValue(Objects.requireNonNull(response.body()).string(), new TypeReference<List<Event>>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    });

                    if (response.code() == StatusCode.OK.getValue()) {
                        insertAll(events);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

