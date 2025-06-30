package com.example.publiclibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.publiclibrary.R;
import com.example.publiclibrary.model.Author;
import java.util.List;
public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private Context context;
    private List<Author> authorList;
    public AuthorAdapter(Context context, List<Author> authorList) {
        this.context = context;
        this.authorList = authorList;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = authorList.get(position);

        holder.tvAuthorName.setText(author.getName());
        holder.tvAuthorBooksCount.setText("Books: " + author.getBooksCount());
    }
    @Override
    public int getItemCount() {
        return authorList.size();
    }

    static class AuthorViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorName, tvAuthorBooksCount;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvAuthorBooksCount = itemView.findViewById(R.id.tvAuthorBooksCount);
        }
    }
}
