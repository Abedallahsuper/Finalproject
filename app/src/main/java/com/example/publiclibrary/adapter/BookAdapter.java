package com.example.publiclibrary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide; // أضف هذا في الأعلى
import com.example.publiclibrary.helper.DatabaseHelper;
import com.example.publiclibrary.R;
import com.example.publiclibrary.model.Book;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public interface OnBookActionListener {
        void onEdit(Book book);
        void onDelete(Book book);
    }
    private Context context;
    private List<Book> bookList;
    private OnBookActionListener listener;
    private DatabaseHelper dbHelper;

    public BookAdapter(Context context,
                       List<Book> bookList,
                       OnBookActionListener listener,
                       DatabaseHelper dbHelper) {
        this.context   = context;
        this.bookList  = bookList;
        this.listener  = listener;
        this.dbHelper  = dbHelper;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bookNameTextView.setText( " Title "+ book.getTitle());
        holder.authorTextView.setText( " Author "+ book.getAuthor());
        holder.categoryTextView.setText( " Category "+book.getCategory());
        holder.descriptionTextView.setText(
                book.getDescription() == null ? "" : book.getDescription()
        );
        if (book.getImageUri() != null && !book.getImageUri().isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(book.getImageUri()))
                    .placeholder(R.drawable.book3)
                    .error(R.drawable.book3)
                    .into(holder.bookImageView);
        } else {
            holder.bookImageView.setImageResource(R.drawable.book3);
        }
        holder.editBookButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(book);
            }
        });
        holder.deleteBookButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        TextView  bookNameTextView;
        TextView  authorTextView;
        TextView  categoryTextView;
        TextView  descriptionTextView;
        Button    editBookButton;
        Button    deleteBookButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImageView       = itemView.findViewById(R.id.bookImageView);
            bookNameTextView    = itemView.findViewById(R.id.bookNameTextView);
            authorTextView      = itemView.findViewById(R.id.authorTextView);
            categoryTextView    = itemView.findViewById(R.id.categoryTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            editBookButton      = itemView.findViewById(R.id.editBookButton);
            deleteBookButton    = itemView.findViewById(R.id.deleteBookButton);
        }
    }
}
