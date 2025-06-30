package com.example.publiclibrary.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.publiclibrary.R;
import com.example.publiclibrary.adapter.BookAdapter;
import com.example.publiclibrary.helper.DatabaseHelper;
import com.example.publiclibrary.model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManageBooksActivity extends AppCompatActivity
        implements BookAdapter.OnBookActionListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddBook;
    private BookAdapter adapter;
    private DatabaseHelper db;
    private ArrayList<Book> bookList;

    private Uri selectedImageUri;
    private ImageView dialogImageView;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books);

        recyclerView = findViewById(R.id.recyclerBooks);
        fabAddBook = findViewById(R.id.fabAddBook);
        db = new DatabaseHelper(this);
        bookList = new ArrayList<>(db.getAllBooks());

        adapter = new BookAdapter(this, bookList, this, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAddBook.setOnClickListener(v -> showAddBookDialog());

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            getContentResolver().takePersistableUriPermission(
                                    selectedImageUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                        }
                        if (dialogImageView != null) {
                            Glide.with(this)
                                    .load(selectedImageUri)
                                    .into(dialogImageView);
                        }
                    }
                });
    }

    private void showAddBookDialog() {
        selectedImageUri = null;
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_book, null);

        EditText edtTitle = view.findViewById(R.id.etAddBookTitle);
        EditText edtAuthor = view.findViewById(R.id.etAddBookAuthor);
        EditText edtCategory = view.findViewById(R.id.etAddBookCategory);
        EditText edtDescription = view.findViewById(R.id.etAddBookDescription);
        dialogImageView = view.findViewById(R.id.imgBookCover);
        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        Button btnAdd = view.findViewById(R.id.btnadd);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            imagePickerLauncher.launch(intent);
        });

        btnAdd.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String author = edtAuthor.getText().toString().trim();
            String category = edtCategory.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(category)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Book book = new Book(title, author, category);
            book.setDescription(description.isEmpty() ? null : description);
            if (selectedImageUri != null) {
                book.setImageUri(selectedImageUri.toString());
            }

            long id = db.addBook(book);
            if (id != -1) {
                bookList.clear();
                bookList.addAll(db.getAllBooks());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onEdit(Book book) {
        showEditBookDialog(book);
    }

    @Override
    public void onDelete(Book book) {
        confirmDeleteBook(book);
    }

    private void showEditBookDialog(Book book) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_book, null);

        EditText edtTitle = view.findViewById(R.id.etAddBookTitle);
        EditText edtAuthor = view.findViewById(R.id.etAddBookAuthor);
        EditText edtCategory = view.findViewById(R.id.etAddBookCategory);
        EditText edtDescription = view.findViewById(R.id.etAddBookDescription);
        View btnUpdate = view.findViewById(R.id.btnupdate);

        edtTitle.setText(book.getTitle());
        edtAuthor.setText(book.getAuthor());
        edtCategory.setText(book.getCategory());
        edtDescription.setText(book.getDescription() == null ? "" : book.getDescription());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        btnUpdate.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String author = edtAuthor.getText().toString().trim();
            String category = edtCategory.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(category)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            book.setTitle(title);
            book.setAuthor(author);
            book.setCategory(category);
            book.setDescription(description.isEmpty() ? null : description);

            int rowsAffected = db.updateBook(book);
            if (rowsAffected > 0) {
                bookList.clear();
                bookList.addAll(db.getAllBooks());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Book updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to update book", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void confirmDeleteBook(Book book) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete \"" + book.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteBook(book.getId());
                    bookList.clear();
                    bookList.addAll(db.getAllBooks());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
