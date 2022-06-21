package dev.magaiver.eventcheckin.presentation.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.magaiver.eventcheckin.domain.model.User;
import dev.magaiver.eventcheckin.domain.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void insertAll(List<User> users) {
        userRepository.insertAll(users);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public User findById(String uuid) {
        return userRepository.findById(uuid);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public LiveData<List<User>> findAllLiveData() {
        return userRepository.findAllLiveData();
    }

    public void createUserServer(final User user) {
         userRepository.createUserServer(user);
    }
}
