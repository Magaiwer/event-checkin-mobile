package dev.magaiver.eventcheckin.domain.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import dev.magaiver.eventcheckin.core.dao.SubscriptionDao;
import dev.magaiver.eventcheckin.core.database.AppDatabase;
import dev.magaiver.eventcheckin.domain.model.Subscription;

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

    public Subscription findById(final String eventId, String userEmail) {
        return this.subscriptionDao.findById(eventId, userEmail);
    }

    public List<Subscription> findAll() {
        return this.subscriptionDao.findAll();
    }

    public LiveData<List<Subscription>> findAllLiveData() {
        return this.subscriptionDao.findAllLiveData();

    }

}