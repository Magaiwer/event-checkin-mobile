package dev.magaiver.eventcheckin.presentation.ui.event;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.eventcheckin.R;
import com.google.android.material.appbar.MaterialToolbar;

import dev.magaiver.eventcheckin.presentation.ui.fragment.EventFragment;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventListActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private TextView txtViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
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
        txtViewTitle.setText(getString(R.string.activity_event_list_title));


        EventFragment eventFragment = new EventFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_Event, eventFragment)
                .commit();
    }

}