package com.example.publiclibrary.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publiclibrary.helper.DatabaseHelper;
import com.example.publiclibrary.R;
import com.example.publiclibrary.adapter.BorrowedBookAdapter;
import com.example.publiclibrary.model.BorrowedBook;

import java.util.List;

public class BorrowedBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BorrowedBookAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<BorrowedBook> borrowedBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_borrowed);

        recyclerView = findViewById(R.id.recyclerViewReservedBook);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);


        SharedPreferences prefs = getSharedPreferences("LibraryAppSession", MODE_PRIVATE);  // تم استخدام  SharedPreferences
        String userRole = prefs.getString("role", "Student");
        String userEmail = prefs.getString("email", "");
        int studentId = prefs.getInt("user_id", -1);

        if (userRole.equalsIgnoreCase("Admin")) {
            borrowedBooks = dbHelper.getAllBorrowedBooks();
            adapter = new BorrowedBookAdapter(this, borrowedBooks, BorrowedBookAdapter.UserType.ADMIN, -1);
        } else {
            borrowedBooks = dbHelper.getBorrowedBooksByStudent(studentId);
            adapter = new BorrowedBookAdapter(this, borrowedBooks, BorrowedBookAdapter.UserType.STUDENT, studentId);
        }

        recyclerView.setAdapter(adapter);
    }

}
