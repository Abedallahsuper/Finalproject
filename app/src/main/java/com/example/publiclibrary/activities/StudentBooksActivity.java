package com.example.publiclibrary.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.SearchView;  // <-- هنا التعديل
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publiclibrary.R;
import com.example.publiclibrary.adapter.StudentBookAdapter;
import com.example.publiclibrary.helper.DatabaseHelper;
import com.example.publiclibrary.model.Book;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner spinnerCategory, spinnerAuthor;
    private SearchView searchView;
    private StudentBookAdapter adapter;
    private DatabaseHelper dbHelper;
    private int currentUserId;

    private List<Book> allBooks;
    private List<Book> filteredBooks;
    private List<String> categories;
    private List<String> authors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allbook);

        recyclerView = findViewById(R.id.recyclerBooks);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerAuthor = findViewById(R.id.spinnerAuthor);
        searchView = findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        // الحصول على user_id من SharedPreferences
        SharedPreferences prefs = getSharedPreferences("LibraryAppSession", Context.MODE_PRIVATE);
        currentUserId = prefs.getInt("user_id", -1);

        loadData();
        setupSpinners();
        setupSearch();
        setupRecycler();
    }

    private void loadData() {
        allBooks = dbHelper.getAllBooks();
        filteredBooks = new ArrayList<>(allBooks);

        categories = new ArrayList<>();
        categories.add("All Categories");
        for (Book b : allBooks)
            if (!categories.contains(b.getCategory()))
                categories.add(b.getCategory());

        authors = new ArrayList<>();
        authors.add("All Authors");
        for (Book b : allBooks)
            if (!authors.contains(b.getAuthor()))
                authors.add(b.getAuthor());
    }


    private void setupSpinners() {
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        ArrayAdapter<String> authAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, authors);
        authAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAuthor.setAdapter(authAdapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int pos, long id) {
                applyFilters();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        };
        spinnerCategory.setOnItemSelectedListener(listener);
        spinnerAuthor.setOnItemSelectedListener(listener);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilters();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilters();
                return true;
            }
        });
    }

    private void setupRecycler() {
        dbHelper = new DatabaseHelper(this);
        adapter = new StudentBookAdapter(this, filteredBooks, book -> {
            String date = getCurrentDate();
            long borrowId = dbHelper.addBorrowRecord(book.getId(), currentUserId, date);

            if (borrowId != -1) {
                Toast.makeText(this,
                        "Borrowed \"" + book.getTitle() + "\"Date " + date,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void applyFilters() {
        String selCat = spinnerCategory.getSelectedItem().toString();
        String selAuth = spinnerAuthor.getSelectedItem().toString();
        String search = searchView.getQuery().toString().trim().toLowerCase();

        filteredBooks.clear();

        for (Book b : allBooks) {
            boolean matchCat = selCat.equals("All Categories") || b.getCategory().equals(selCat);
            boolean matchAuth = selAuth.equals("All Authors") || b.getAuthor().equals(selAuth);
            boolean matchSearch = b.getTitle().toLowerCase().contains(search);

            if (matchCat && matchAuth && matchSearch) {
                filteredBooks.add(b);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
