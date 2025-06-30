package com.example.publiclibrary.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publiclibrary.R;
import com.example.publiclibrary.adapter.AuthorAdapter;
import com.example.publiclibrary.helper.DatabaseHelper;
import com.example.publiclibrary.model.Author;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManageAuthorsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddAuthor;
    private AuthorAdapter adapter;
    private DatabaseHelper db;
    private ArrayList<Author> authorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_authors);

        recyclerView = findViewById(R.id.rvAuthors);
        fabAddAuthor = findViewById(R.id.fabAddAuthor);
        db = new DatabaseHelper(this);

        authorList = new ArrayList<>(db.getAllAuthors());

        adapter = new AuthorAdapter(this, authorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAddAuthor.setOnClickListener(v -> showAddAuthorDialog());
    }

    private void showAddAuthorDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_author, null);
        EditText etAuthorName = view.findViewById(R.id.etAddAuthorName);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        view.findViewById(R.id.btnSaveAuthor).setOnClickListener(v -> {
            String name = etAuthorName.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please enter author name", Toast.LENGTH_SHORT).show();
                return;
            }

            Author author = new Author(name);
            long id = db.addAuthor(author);
            if (id != -1) {
                authorList.clear();
                authorList.addAll(db.getAllAuthors());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Author added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to add author", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btnCancelAuthor).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
