package dev.magaiver.eventcheckin.presentation.ui.login;

import android.app.Application;
import android.util.Patterns;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventcheckin.R;
import com.google.firebase.auth.FirebaseAuth;

import dev.magaiver.eventcheckin.domain.model.LoggedInUser;
import dev.magaiver.eventcheckin.domain.model.User;
import dev.magaiver.eventcheckin.domain.repository.LoginRepository;
import dev.magaiver.eventcheckin.domain.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;

    public LoginViewModel(Application application) {
        super(application);
        this.loginRepository = LoginRepository.getInstance(application);
        userRepository = new UserRepository(application);
        mAuth = FirebaseAuth.getInstance();
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uuid = task.getResult().getUser().getUid();
                searchUserDb(uuid);
            } else {
                loginResult.setValue(new LoginResult(task.getException().getMessage()));
            }
        });
    }

    public void searchUserDb(String uuid) {
        new Thread(() -> {
            User user = userRepository.findById(uuid);
            if (user != null) {
                loginRepository.setLoggedInUser(new LoggedInUser(user.getUuid(), user.getName(), user.getEmail()));
                loginResult.postValue(new LoginResult(new LoggedInUserView(user.getEmail())));
            }
        }).start();

    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}