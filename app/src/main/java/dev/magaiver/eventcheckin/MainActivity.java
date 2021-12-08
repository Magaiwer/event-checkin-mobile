package dev.magaiver.eventcheckin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.eventcheckin.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import dev.magaiver.eventcheckin.api.ApiTemplate;
import dev.magaiver.eventcheckin.api.Routes;
import dev.magaiver.eventcheckin.api.StatusCode;
import dev.magaiver.eventcheckin.domain.model.Event;
import dev.magaiver.eventcheckin.domain.model.Subscription;
import dev.magaiver.eventcheckin.domain.repository.EventRepository;
import dev.magaiver.eventcheckin.domain.repository.SubscriptionRepository;
import dev.magaiver.eventcheckin.presentation.ui.activity.EventListActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private Button btnSynchronize;
    private LinearProgressIndicator spinner;

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

        spinner = findViewById(R.id.progressIndicator);
        spinner.setVisibility(View.GONE);

        btnSynchronize = findViewById(R.id.btnSynchronize);
        initListener();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_event:
                Intent intent = new Intent(this, EventListActivity.class);
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

            new Handler().postDelayed( ()-> {
                syncCheckIns();
                syncEvents();
            }, 3000);

        });
    }

    private  void syncEvents() {
        EventRepository eventRepository = new EventRepository(getApplication());
        try {

            ApiTemplate.get(Routes.URL_EVENT, "", "").enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e.getMessage());
                    createEventListIntent();
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
                        eventRepository.deleteAll();
                        eventRepository.insertAll(events);

                        createEventListIntent();
                    }
                }
            });

            spinner.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private synchronized void syncCheckIns() {

        new Thread(() -> {
            SubscriptionRepository subscriptionRepository = new SubscriptionRepository(getApplication());
            List<Subscription> subscriptions = subscriptionRepository.findAll();

            if (!subscriptions.isEmpty()) {

                for (Subscription sub : subscriptions) {

                    try {
                        ApiTemplate.post(Routes.URL_CHECK_IN, sub, "").enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                System.out.println(e.getMessage());
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response)  {

                                if (response.code() == StatusCode.OK.getValue() || response.code() == StatusCode.BAD_REQUEST.getValue() ) {
                                    subscriptionRepository.delete(sub);
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

    private void createEventListIntent() {
        Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
        startActivity(intent);
    }
}