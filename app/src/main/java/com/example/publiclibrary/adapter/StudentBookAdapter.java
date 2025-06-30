package com.example.publiclibrary.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide; // أضف هذا في الأعلى
import com.example.publiclibrary.R;
import com.example.publiclibrary.model.Book;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentBookAdapter extends RecyclerView.Adapter<StudentBookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;
    private OnBorrowClickListener borrowClickListener;

    public interface OnBorrowClickListener {
        void onBorrowClick(Book book);
    }
    public StudentBookAdapter(Context context, List<Book> bookList, OnBorrowClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.borrowClickListener = listener;
    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.titleTextView.setText("Name: " + book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.categoryTextView.setText(book.getCategory());
        holder.descriptionTextView.setText(book.getDescription() != null ? book.getDescription() : "");
        String imageUri = book.getImageUri();

        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                Uri uri = Uri.parse(imageUri);
                Glide.with(context)
                        .load(uri)
                        .placeholder(R.drawable.book3)
                        .error(R.drawable.book3)
                        .into(holder.bookImageView);
            } catch (Exception e) {
                Glide.with(context)
                        .load(imageUri)
                        .placeholder(R.drawable.book3)
                        .error(R.drawable.book3)
                        .into(holder.bookImageView);
            }
        } else {
            holder.bookImageView.setImageResource(R.drawable.book3);
        }



        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "student");

        if (role.equals("admin")) {
            holder.borrowButton.setVisibility(View.GONE);
        } else {
            holder.borrowButton.setVisibility(View.VISIBLE);
        }

        holder.borrowButton.setOnClickListener(v -> {
            if (borrowClickListener != null) {
                borrowClickListener.onBorrowClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        TextView titleTextView, authorTextView, categoryTextView, descriptionTextView;
        Button borrowButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.imageView2);
            titleTextView = itemView.findViewById(R.id.txtallbookcardtitle);
            authorTextView = itemView.findViewById(R.id.txtallbookcardauthor);
            categoryTextView = itemView.findViewById(R.id.txtallbookcardcategory);
            descriptionTextView = itemView.findViewById(R.id.txtalldescription);
            borrowButton = itemView.findViewById(R.id.btnBorrow);
        }
    }
}
