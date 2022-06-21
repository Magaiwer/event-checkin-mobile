package dev.magaiver.eventcheckin.domain.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import dev.magaiver.eventcheckin.api.ApiTemplate;
import dev.magaiver.eventcheckin.api.ErrorMessage;
import dev.magaiver.eventcheckin.api.Routes;
import dev.magaiver.eventcheckin.api.StatusCode;
import dev.magaiver.eventcheckin.core.dao.SubscriptionDao;
import dev.magaiver.eventcheckin.core.database.AppDatabase;
import dev.magaiver.eventcheckin.domain.model.StatusSub;
import dev.magaiver.eventcheckin.domain.model.Subscription;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SubscriptionRepository {

    private final SubscriptionDao subscriptionDao;

    public SubscriptionRepository(Application application) {
        AppDatabase db = AppDatabase.getConnection(application);
        this.subscriptionDao = db.subscriptionDao();
    }

    public void insertAll(final List<Subscription> subscriptions) {
        AppDatabase.getDbWriteExecutor().execute(() -> subscriptionDao.insertAll(subscriptions));
    }

    public void insert(final Subscription subscription) {
        AppDatabase.getDbWriteExecutor().execute(() -> subscriptionDao.insert(subscription));
    }


    public void deleteAll() {
        AppDatabase.getDbWriteExecutor().execute(subscriptionDao::deleteAll);
    }

    public void delete(Subscription subscription) {
        AppDatabase.getDbWriteExecutor().execute(() -> subscriptionDao.delete(subscription));
    }

    public Subscription findById(final String eventId) {
        return this.subscriptionDao.findById(eventId);
    }

    public List<Subscription> findAll() {
        return this.subscriptionDao.findAll();
    }

    public LiveData<List<Subscription>> findAllLiveData() {
        return this.subscriptionDao.findAllLiveData();
    }

    public void syncCheckIns() {
        new Thread(() -> {
            List<Subscription> subscriptions = findAll();
            if (!subscriptions.isEmpty()) {
                for (Subscription sub : subscriptions) {
                    try {
                        ApiTemplate.post(Routes.URL_CHECK_IN, sub, "").enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                System.out.println(e.getMessage());
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) {
                                if (response.code() == StatusCode.OK.getValue() || response.code() == StatusCode.BAD_REQUEST.getValue()) {
                                    delete(sub);
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public String subscribeEvent(Subscription subscription) {
        final String[] responseStr = {""};
        String route = subscription.getStatus().equals(StatusSub.ENABLED.name())
                ? Routes.URL_SUBSCRIBE : Routes.URL_CANCEL_SUB;
        try {
            ApiTemplate.post(route, subscription, "").enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("Subscription Repository", e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() == StatusCode.BAD_REQUEST.getValue()) {
                        ErrorMessage errorMessage = ApiTemplate.getObjectMapper()
                                .readValue(Objects.requireNonNull(response.body()).string(), ErrorMessage.class);
                        responseStr[0] = errorMessage.userMessage;
                        Log.d("user", errorMessage.userMessage);

                    } else if (response.code() == StatusCode.CREATED.getValue()) {
                        responseStr[0] = Objects.requireNonNull(response.body()).string();
                    }
                    insert(subscription);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr[0];
    }

    public void postCheckIn(Subscription sub) {
        try {
            ApiTemplate.post(Routes.URL_CHECK_IN, sub, "").enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("Check In", e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() == StatusCode.BAD_REQUEST.getValue()) {
                        ErrorMessage errorMessage = ApiTemplate.getObjectMapper().readValue(Objects.requireNonNull(response.body()).string(), ErrorMessage.class);
                        Log.d("Check In", errorMessage.userMessage);
                    } else if (response.code() == StatusCode.OK.getValue()) {
                        Log.d("Check In", "Check in OK");
                        insert(sub);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}