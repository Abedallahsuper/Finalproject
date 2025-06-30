package com.example.publiclibrary.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.publiclibrary.R;

public class StudentDashboard extends AppCompatActivity {

        Button btnProfile, btnAllBooks, btnHome, btnNotifications, btnSearch;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_student_dashboard);

            btnProfile = findViewById(R.id.btnProfile);
            btnAllBooks = findViewById(R.id.btnAllBooks);
            btnHome = findViewById(R.id.btnHome);
            btnNotifications = findViewById(R.id.btnNotifications);
            btnSearch = findViewById(R.id.btnSearch);


            btnProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StudentDashboard.this, StudentProfileActivity.class);
                    startActivity(intent);
                }
            });

            btnAllBooks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StudentDashboard.this, StudentBooksActivity.class);
                    startActivity(intent);
                }
            });

            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StudentDashboard.this, HomeActivity.class);
                    startActivity(intent);
                }
            });

            btnNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(StudentDashboard.this, BorrowedBooksActivity.class));
                }
            });
            btnSearch.setOnClickListener(v -> {
                startActivity(new Intent(StudentDashboard.this, StudentBooksActivity.class));
            });
        }

    }


