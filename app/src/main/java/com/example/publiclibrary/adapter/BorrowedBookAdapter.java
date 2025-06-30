package com.example.publiclibrary.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.publiclibrary.helper.DatabaseHelper;
import com.example.publiclibrary.R;
import com.example.publiclibrary.model.BorrowedBook;

import java.util.List;

public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.ViewHolder> {

    private List<BorrowedBook> borrowedBooks;
    private Context context;
    private UserType userType;
    private DatabaseHelper dbHelper;
    private int currentStudentId;

    public BorrowedBookAdapter(Context context, List<BorrowedBook> borrowedBooks, UserType userType, int currentStudentId) {
        this.context = context;
        this.borrowedBooks = borrowedBooks;
        this.userType = userType;
        this.currentStudentId = currentStudentId;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_borrowed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowedBook book = borrowedBooks.get(position);

        holder.txtReservedBookName.setText("Name: " +book.getName());
        holder.txtReservedBookAuthor.setText("Author: " +book.getAuthor());
        holder.txtReservedBookPublisher.setText("Category: " + book.getCategory());
        holder.txtBorrowDate.setText("BorrowDate: "+book.getBorrowDate());

        String imageUri = book.getImageUri();
        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(imageUri))
                    .placeholder(R.drawable.book3)
                    .error(R.drawable.book3)
                    .into(holder.imgBorrowedBook);
        } else {
            holder.imgBorrowedBook.setImageResource(R.drawable.book3);
        }
        if (userType == UserType.ADMIN) {
            holder.txtReservedBy.setText("By: " + book.getMemberName());
            holder.txtReservedBy.setVisibility(View.VISIBLE);
            holder.btnReturn.setVisibility(View.GONE);
        } else {
            holder.txtReservedBy.setText("You");
            holder.txtReservedBy.setVisibility(View.VISIBLE);
            if (book.getMemberId() == currentStudentId) {
                holder.btnReturn.setVisibility(View.VISIBLE);
                holder.btnReturn.setOnClickListener(v -> {
                    showReturnConfirmationDialog(book.getId(), position);
                });
            } else {
                holder.btnReturn.setVisibility(View.GONE);
            }
        }
    }

    private void showReturnConfirmationDialog(int borrowId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Return Book")
                .setMessage("Are you sure you want to return this book?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean deleted = dbHelper.deleteBorrowedBook(borrowId);
                    if (deleted) {
                        borrowedBooks.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Book returned successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to return book", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return borrowedBooks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtReservedBookName, txtReservedBookAuthor, txtReservedBookPublisher, txtReservedBy, txtBorrowDate;
        ImageView imgBorrowedBook;
        Button btnReturn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReservedBookName = itemView.findViewById(R.id.txtReservedBookName);
            txtReservedBookAuthor = itemView.findViewById(R.id.txtReservedBookAuthor);
            txtReservedBookPublisher = itemView.findViewById(R.id.txtReservedBookPublisher);
            txtReservedBy = itemView.findViewById(R.id.txtReservedBy);
            txtBorrowDate = itemView.findViewById(R.id.txtBorrowDate);
            btnReturn = itemView.findViewById(R.id.btnReturnBook);
            imgBorrowedBook = itemView.findViewById(R.id.imgBorrowedBook);
        }
    }

    public enum UserType {
        ADMIN,
        STUDENT
    }
}
