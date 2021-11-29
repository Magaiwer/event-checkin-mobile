package dev.magaiver.eventcheckin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.eventcheckin.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import dev.magaiver.eventcheckin.presentation.ui.event.EventActivity;
import dev.magaiver.eventcheckin.presentation.ui.event.EventListActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MaterialToolbar materialToolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        materialToolbar = findViewById(R.id.materialToolbar);
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        setSupportActionBar(materialToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, materialToolbar, R.string.toggle_open, R.string.toggle_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        fab = findViewById(R.id.fabAction);

        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);

        initListener();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                showMessage("Home");
                break;
            case R.id.nav_category:
                showMessage("Category");
                break;
            case R.id.nav_account:
                showMessage("Account");
                break;
            case R.id.nav_account_balance:
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

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void initListener() {
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, EventActivity.class);
            startActivity(intent);
        });
    }
}