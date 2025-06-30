package com.example.publiclibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.publiclibrary.R;

public class AdminDashboard extends AppCompatActivity {

    Button btnManageBooks, btnManageUsers, btnManageAuthors, btnBorrowedList;
    Button btnHome;
    Button btnSearch;
    Button btnNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnManageBooks = findViewById(R.id.btnManageBooks);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageAuthors = findViewById(R.id.btnManageAuthors);
        btnBorrowedList = findViewById(R.id.btnBorrowedList);
        btnHome = findViewById(R.id.btnHome);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnSearch = findViewById(R.id.btnSearch);


        btnManageBooks.setOnClickListener(new View.OnClickListener() {//هذة الاسطر تنتقل من الادمن الى صفحة اداة الكتب
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, ManageBooksActivity.class));
            }
        });

        btnManageUsers.setOnClickListener(new View.OnClickListener() {//هذة الاسطر تنتقل من الادمن الى صفحة اداة المستخدم
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, ManageUsersActivity.class));
            }
        });

        btnManageAuthors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, ManageAuthorsActivity.class));
            }
        });

        btnBorrowedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, BorrowedBooksActivity.class));
            }
        });

        btnHome.setOnClickListener(v -> {//ازرار الشريط السفلي و هي زر ينقلني الي الصفحة الرئيسة و زر بحث  و زر الاشعارات
            startActivity(new Intent(AdminDashboard.this, HomeActivity.class));

        });
        btnNotifications.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboard.this, BorrowedBooksActivity.class));
        });
        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboard.this, StudentBooksActivity.class));
        });
    }

}
