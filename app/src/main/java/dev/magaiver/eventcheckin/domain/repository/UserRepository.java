package dev.magaiver.eventcheckin.domain.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import dev.magaiver.eventcheckin.api.ApiTemplate;
import dev.magaiver.eventcheckin.api.Routes;
import dev.magaiver.eventcheckin.core.dao.UserDao;
import dev.magaiver.eventcheckin.core.database.AppDatabase;
import dev.magaiver.eventcheckin.domain.model.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getConnection(application);
        this.userDao = db.userDao();
    }

    public void insertAll(final List<User> users) {
        AppDatabase.getDbWriteExecutor().execute(() -> userDao.insertAll(users));
    }

    public void insert(final User user) {
        AppDatabase.getDbWriteExecutor().execute(() -> userDao.insert(user));
    }

    public void deleteAll() {
        AppDatabase.getDbWriteExecutor().execute(userDao::deleteAll);
    }

    public User findById(final String uuid) {
        return this.userDao.findById(uuid);
    }

    public List<User> findAll() {
        return this.userDao.findAll();
    }

    public LiveData<List<User>> findAllLiveData() {
        return this.userDao.findAllLiveData();
    }

    public void createUserServer(final User user) {
        try {
            ApiTemplate.post(Routes.URL_USER, user, "").enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("User repository", e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("User repository", Objects.requireNonNull(response.body()).string());
                }
            });
            insert(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

