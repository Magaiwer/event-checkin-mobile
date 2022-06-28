package dev.magaiver.eventcheckin.presentation.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.eventcheckin.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.time.LocalDate;
import java.util.Locale;

import dev.magaiver.eventcheckin.domain.model.Event;
import dev.magaiver.eventcheckin.domain.model.LoggedInUser;
import dev.magaiver.eventcheckin.domain.model.StatusSub;
import dev.magaiver.eventcheckin.domain.model.Subscription;
import dev.magaiver.eventcheckin.domain.repository.LoginRepository;
import dev.magaiver.eventcheckin.presentation.view.EventViewModel;
import dev.magaiver.eventcheckin.presentation.view.SubscriptionViewModel;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CheckInActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private TextView txtViewTitle;
    private TextView txtEvent;
    private TextView txtDescription;
    private TextView txtDate;
    private TextView txtDateClose;
    private TextView txtLocalization;
    private TextView barcodeResultView;
    private Button btnCheckin;
    private Subscription subscription;
    private String eventId;
    private EventViewModel eventViewModel;
    private SubscriptionViewModel sbViewModel;
    private Event event;
    private LoggedInUser loggedInUser;
    private FusedLocationProviderClient fusedLocationClient;
    private Location myLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        sbViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initComponents();
        initListeners();
        loadEvent(eventId);
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
        txtEvent = findViewById(R.id.txtEvent);
        txtDescription = findViewById(R.id.txtDescriptionEvent);
        txtDate = findViewById(R.id.txtDateEvent);
        txtDateClose = findViewById(R.id.txtDtClose);
        txtLocalization = findViewById(R.id.txtLocation);
        btnCheckin = findViewById(R.id.btnSend);
        barcodeResultView = findViewById(R.id.txtBarcode);
        Intent intent = getIntent();
        eventId = (String) intent.getSerializableExtra("eventId");
        loggedInUser = LoginRepository.getInstance(getApplication()).getUser();

    }

    @SuppressLint("MissingPermission")
    private void initListeners() {+
        btnCheckin.setOnClickListener(v -> {
            subscription = new Subscription(event.getId(), loggedInUser.getEmail(), StatusSub.PRESENT.name());

            GmsBarcodeScanner gmsBarcodeScanner = GmsBarcodeScanning.getClient(this);
            gmsBarcodeScanner
                    .startScan()
                    .addOnSuccessListener(barcode -> {
                        String idEvent = barcode.getDisplayValue();
                        barcodeResultView.setText(idEvent);
                        if (isSameEvent(idEvent)) {
                            sbViewModel.postCheckIn(subscription);
                            eventViewModel.syncEventsServer();
                            showToast(getString(R.string.checkin_succefully));
                            finish();
                        } else {
                            showToast(getString(R.string.error_checkin_location));
                        }

                        sbViewModel.syncCheckInsServer();
                    })
                    .addOnFailureListener(e -> barcodeResultView.setText(getErrorMessage((MlKitException) e)));


        });

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        myLocation = location;
                    }
                });
    }

    private boolean isEventLocation() {
        if (myLocation == null) return true;
        return event.matchLatitude(myLocation.getLatitude())
                && event.matchLongitude(myLocation.getLongitude());
    }

    private boolean isSameEvent(String idEvent) {
        return event.getId().equals(idEvent);
    }

    private boolean isEventDay() {
        return event.getDateTime().getDayOfMonth() == LocalDate.now().getDayOfMonth();
    }

    private String getErrorMessage(MlKitException e) {
        switch (e.getErrorCode()) {
            case MlKitException.CODE_SCANNER_CANCELLED:
                return getString(R.string.error_scanner_cancelled);
            case MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED:
                return getString(R.string.error_camera_permission_not_granted);
            case MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE:
                return getString(R.string.error_app_name_unavailable);
            default:
                return getString(R.string.error_default_message, e);
        }
    }

    public void showToast(final String message) {
        runOnUiThread(() -> {
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        });
    }


    private void bind(Event event) {
        txtEvent.setText(event.getName());
        txtDescription.setText(getString(R.string.txt_desc_event, event.getDescription()));
        txtDate.setText(getString(R.string.txt_label_date, event.dateTimeStr()));
        txtDateClose.setText(getString(R.string.txt_label_date_close, event.dateTimeStr()));
        txtLocalization.setText(getString(R.string.txt_location, event.getLocation()));
        btnCheckin = findViewById(R.id.btnSend);
    }

    private synchronized boolean enableCheckInIf() {
        return isEventDay()
                && event.isSubscribed();
                //&& isEventLocation();
    }

    private synchronized void loadEvent(String eventId) {
        runOnUiThread(() -> {
            event = eventViewModel.findById(eventId);
            boolean isSubscribed = sbViewModel.verifyAlreadySubscribed(eventId);
            event.setSubscribed(isSubscribed);
            btnCheckin.setEnabled(enableCheckInIf());
            bind(event);
        });
    }
}