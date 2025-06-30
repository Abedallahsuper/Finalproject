package com.example.publiclibrary.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.publiclibrary.R;
import com.example.publiclibrary.model.User;
import com.example.publiclibrary.helper.DatabaseHelper;

public class StudentProfileActivity extends AppCompatActivity {

    private TextView tvStudentName, tvStudentEmail, tvStudentPhone,tvStudentIdmember;
    private Button btnViewBorrowedBooks, btnLogout;

    private DatabaseHelper dbHelper;
    private int currentStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);

        tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentEmail = findViewById(R.id.tvStudentEmail);
        tvStudentPhone = findViewById(R.id.tvStudentPhone);
        tvStudentIdmember = findViewById(R.id.tvStudentIdmember);

        btnViewBorrowedBooks = findViewById(R.id.btnViewBorrowedBooks);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("LibraryAppSession", MODE_PRIVATE);
        String userEmail = prefs.getString("email", "");


        currentStudentId = dbHelper.getUserIdByEmail(userEmail);

        User user = dbHelper.getUserById(currentStudentId);
        if (user != null) {
            tvStudentName.setText("Name  "+user.getName());
            tvStudentEmail.setText("Email "+user.getEmail());
            tvStudentPhone.setText("Phone  "+user.getPhone());
            tvStudentIdmember.setText("MemberId  "+user.getMemberId());

        }

        btnViewBorrowedBooks.setOnClickListener(v -> {
            Intent intent = new Intent(StudentProfileActivity.this, BorrowedBooksActivity.class);
            intent.putExtra("student_id", currentStudentId);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(StudentProfileActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
