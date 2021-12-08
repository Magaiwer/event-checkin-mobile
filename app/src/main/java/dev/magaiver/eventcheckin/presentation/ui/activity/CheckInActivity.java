package dev.magaiver.eventcheckin.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eventcheckin.R;
import com.google.android.material.appbar.MaterialToolbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import dev.magaiver.eventcheckin.api.ApiTemplate;
import dev.magaiver.eventcheckin.api.ErrorMessage;
import dev.magaiver.eventcheckin.api.Routes;
import dev.magaiver.eventcheckin.api.StatusCode;
import dev.magaiver.eventcheckin.domain.model.Subscription;
import dev.magaiver.eventcheckin.domain.repository.SubscriptionRepository;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CheckInActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private TextView txtViewTitle;
    private TextView txtUserEmail;
    private Button btnSend;
    private Subscription subscription;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initComponents();
        initListeners();
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
        txtViewTitle.setText(getString(R.string.Checkin_Title));
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserEmail.setText("");
        btnSend = findViewById(R.id.btnSend);

        Intent intent = getIntent();
        eventId = (String) intent.getSerializableExtra("eventId");

    }

    private void initListeners() {
        btnSend.setOnClickListener(v -> {
            SubscriptionRepository subscriptionRepository = new SubscriptionRepository(getApplication());
            subscription = new Subscription(eventId, txtUserEmail.getText().toString());
            try {
                ApiTemplate.post(Routes.URL_CHECK_IN, subscription, "").enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        subscriptionRepository.insert(subscription);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String message = "";

                        if (response.code() == StatusCode.BAD_REQUEST.getValue()) {
                            ErrorMessage errorMessage = ApiTemplate.getObjectMapper().readValue(Objects.requireNonNull(response.body()).string(), ErrorMessage.class);
                            message = errorMessage.userMessage;
                        } else if (response.code() == StatusCode.OK.getValue()) {
                            message = "Check in OK";
                        }

                        showToast(message);
                    }
                });
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showToast(final String message) {
        runOnUiThread(() -> {
            Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        });
    }
}