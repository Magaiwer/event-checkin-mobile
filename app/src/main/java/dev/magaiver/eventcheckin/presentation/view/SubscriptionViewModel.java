package dev.magaiver.eventcheckin.presentation.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.magaiver.eventcheckin.domain.model.Subscription;
import dev.magaiver.eventcheckin.domain.repository.SubscriptionRepository;

public class SubscriptionViewModel extends AndroidViewModel {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionViewModel(@NonNull Application application) {
        super(application);
        subscriptionRepository = new SubscriptionRepository(application);
    }

    public void insertAll(List<Subscription> subscriptions) {
        subscriptionRepository.insertAll(subscriptions);
    }

    public void deleteAll() {
        subscriptionRepository.deleteAll();
    }

    public Subscription findById(final String eventId) {
        return subscriptionRepository.findById(eventId);
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public LiveData<List<Subscription>> findAllLiveData() {
        return subscriptionRepository.findAllLiveData();
    }

    public void syncCheckInsServer() {
        subscriptionRepository.syncCheckIns();
    }

    public void postCheckIn(Subscription sub) {
        subscriptionRepository.postCheckIn(sub);
    }

    public String subscribeEvent(Subscription subscription) {
        return subscriptionRepository.subscribeEvent(subscription);
    }

    public boolean verifyAlreadySubscribed(String eventId) {
        return (findById(eventId) != null);
    }

}
