package dev.magaiver.eventcheckin.presentation.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.eventcheckin.R;
import com.google.firebase.auth.FirebaseAuth;

import dev.magaiver.eventcheckin.domain.model.User;
import dev.magaiver.eventcheckin.presentation.view.UserViewModel;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RegisterActivity extends AppCompatActivity {

    private EditText edEmail;
    private EditText edName;
    private EditText edPassword;
    private EditText edConfirmPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        initComponents();
        initListeners();
    }

    private void initComponents() {
        edEmail = findViewById(R.id.edEmail);
        edName = findViewById(R.id.edName);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
    }

    private void initListeners() {
        btnRegister.setOnClickListener(view -> createUser());
    }

    private void createUser() {
        String email = edEmail.getText().toString();
        String name = edName.getText().toString();
        String password = edPassword.getText().toString();
        String confirmPassword = edConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            edEmail.setError(getString(R.string.invalid_username));
            edEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            edName.setError(getString(R.string.invalid_username));
            edName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edPassword.setError(getString(R.string.invalid_password));
            edPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            edConfirmPassword.setError(getString(R.string.invalid_password));
            edConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            edConfirmPassword.setError(getString(R.string.not_match_password));
            edConfirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, R.string.user_registrated_succ, Toast.LENGTH_LONG).show();
                User user = new User(task.getResult().getUser().getUid(), name, email);
                user.setPassword(password);
                userViewModel.createUserServer(user);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.user_registrated_error + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}