package dev.magaiver.eventcheckin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.eventcheckin.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import dev.magaiver.eventcheckin.domain.model.LoggedInUser;
import dev.magaiver.eventcheckin.domain.repository.LoginRepository;
import dev.magaiver.eventcheckin.presentation.ui.activity.EventListActivity;
import dev.magaiver.eventcheckin.presentation.ui.login.LoginActivity;
import dev.magaiver.eventcheckin.presentation.view.EventViewModel;
import dev.magaiver.eventcheckin.presentation.view.SubscriptionViewModel;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private Button btnSynchronize;
    private LinearProgressIndicator spinner;
    private EventViewModel eventViewModel;
    private SubscriptionViewModel subscriptionViewModel;
    private TextView txtUserName;
    private TextView txtUserEmail;
    private LoginRepository loginRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar materialToolbar = findViewById(R.id.materialToolbar);
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        setSupportActionBar(materialToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, materialToolbar, R.string.toggle_open, R.string.toggle_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


        loginRepository = LoginRepository.getInstance(getApplication());
        LoggedInUser loggedInUser = loginRepository.getUser();

        txtUserName = navigationView.getHeaderView(0).findViewById(R.id.txt_user_name);
        txtUserEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_user_email);
        if (loggedInUser != null) {
            txtUserName.setText(loggedInUser.getDisplayName());
            txtUserEmail.setText(loggedInUser.getEmail());
        }

        spinner = findViewById(R.id.progressIndicator);
        spinner.setVisibility(View.GONE);

        btnSynchronize = findViewById(R.id.btnSynchronize);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        initListener();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_event:
                createEventListIntent();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initListener() {
        btnSynchronize.setOnClickListener(view -> {
            spinner.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {
                subscriptionViewModel.syncCheckInsServer();
                eventViewModel.syncEventsServer();
                spinner.setVisibility(View.GONE);
            }, 3000);

        });
    }


    private void createEventListIntent() {
        Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
        startActivity(intent);
    }
}