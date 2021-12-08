package dev.magaiver.eventcheckin.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.eventcheckin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;

import dev.magaiver.eventcheckin.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, MainActivity.class);

        new Handler().postDelayed( ()-> {
            startActivity(intent);
            finish();
        }, 3000);

    }
}