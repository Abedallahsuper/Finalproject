package com.example.publiclibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.publiclibrary.R;
import com.example.publiclibrary.model.User;
import com.example.publiclibrary.helper.DatabaseHelper;

public class Register extends AppCompatActivity {

    EditText txtName, txtEmail, txtPhone, txtMemberId, txtPassword, txtConfirmPassword;
    RadioGroup radioGroupRole;
    RadioButton radioStudent, radioAdmin;
    Button btnRegister;
    TextView btnSwapLogin;

    String selectedRole = "";
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtName = findViewById(R.id.txtname);
        txtEmail = findViewById(R.id.txtemail);
        txtPhone = findViewById(R.id.txtphone);
        txtMemberId = findViewById(R.id.txtmemberid);
        txtPassword = findViewById(R.id.txtpassword);
        txtConfirmPassword = findViewById(R.id.txtcomfirmpassword);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        radioStudent = findViewById(R.id.radioStudent);
        radioAdmin = findViewById(R.id.radioAdmin);
        btnRegister = findViewById(R.id.btnregister);
        btnSwapLogin = findViewById(R.id.btnswaplogin);
        db = new DatabaseHelper(this);

        radioGroupRole.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioStudent) {
                selectedRole = "Student";
            } else if (checkedId == R.id.radioAdmin) {
                selectedRole = "Admin";
            }
        });
        btnRegister.setOnClickListener(v -> {
            String name = txtName.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String phone = txtPhone.getText().toString().trim();
            String memberId = txtMemberId.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String confirmPassword = txtConfirmPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || memberId.isEmpty()
                    || password.isEmpty() || confirmPassword.isEmpty() || selectedRole.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(name, email, phone, memberId, password, selectedRole);
            long result = db.addUser(newUser);

            if (result > 0) {
                Toast.makeText(this, "Registered successfully as " + selectedRole, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this, Login.class));
                finish();
            } else {
                Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            }
        });

        btnSwapLogin.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }
}
