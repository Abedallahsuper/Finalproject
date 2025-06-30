package com.example.publiclibrary.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.publiclibrary.R;
import com.example.publiclibrary.helper.DatabaseHelper;

public class Login extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogin;
    TextView btnSwapRegister;
    DatabaseHelper db;

    private static final String SHARED_PREFS_NAME = "LibraryAppSession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtloginemail);
        txtPassword = findViewById(R.id.txtloginpassword);
        btnLogin = findViewById(R.id.btnlogin);
        btnSwapRegister = findViewById(R.id.btnswapregister);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Please enter email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError("Please enter password");
                    return;
                }

                if (db.loginUser(email, password)) {
                    String role = db.getUserRole(email);
                    int userId = db.getUserIdByEmail(email);

                    //  حفظ البيانات في SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("role", role);
                    editor.putInt("user_id", userId);
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Toast.makeText(Login.this, "Login successful as " + role, Toast.LENGTH_SHORT).show();

                    if (role.equals("Admin")) {
                        startActivity(new Intent(Login.this, AdminDashboard.class));
                    } else {
                        startActivity(new Intent(Login.this, StudentDashboard.class));
                    }
                    finish();
                } else {
                    Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSwapRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
