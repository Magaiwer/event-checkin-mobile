package dev.magaiver.eventcheckin.presentation.ui.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.eventcheckin.R;

public class EventActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private TextView txtViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initComponents();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private void initComponents() {
        materialToolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtViewTitle = findViewById(R.id.txtToolbarTitle);
        txtViewTitle.setText(getString(R.string.activity_Event_title));
    }
}