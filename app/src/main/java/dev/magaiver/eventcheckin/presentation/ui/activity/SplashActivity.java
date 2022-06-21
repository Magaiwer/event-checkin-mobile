package dev.magaiver.eventcheckin.presentation.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.eventcheckin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.magaiver.eventcheckin.MainActivity;
import dev.magaiver.eventcheckin.presentation.ui.login.LoginActivity;
import dev.magaiver.eventcheckin.presentation.ui.login.LoginViewModel;
import dev.magaiver.eventcheckin.presentation.view.EventViewModel;
import dev.magaiver.eventcheckin.presentation.view.SubscriptionViewModel;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EventViewModel eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        SubscriptionViewModel subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        subscriptionViewModel.syncCheckInsServer();
        eventViewModel.syncEventsServer();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();

            Intent intent;
            if (currentUser == null) {
                intent = new Intent(this, LoginActivity.class);
            } else {
                LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
                loginViewModel.searchUserDb(currentUser.getUid());
                intent = new Intent(this, MainActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }, 4000);

    }
}