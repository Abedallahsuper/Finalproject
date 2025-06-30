package com.example.publiclibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publiclibrary.R;
import com.example.publiclibrary.helper.DatabaseHelper;
import com.example.publiclibrary.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnUserActionListener {
        void onEdit(User user);
        void onDelete(User user);
    }

    private Context context;
    private List<User> userList;
    private OnUserActionListener listener;
    private DatabaseHelper dbHelper;

    public UserAdapter(Context context, List<User> userList,
                       OnUserActionListener listener, DatabaseHelper dbHelper) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.nameTextView.setText("Name : " + user.getName());
        holder.emailTextView.setText("Email : " + user.getEmail());
        holder.phoneTextView.setText("Phone : " + user.getPhone());
        holder.memberIdTextView.setText("MemberId : " + user.getMemberId());
        holder.roleTextView.setText("Role : " + user.getRole());

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(user);
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(user);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, phoneTextView, memberIdTextView, roleTextView;
        Button editButton, deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView     = itemView.findViewById(R.id.tvUserName);
            emailTextView    = itemView.findViewById(R.id.tvUserEmail);
            phoneTextView    = itemView.findViewById(R.id.tvUserPhone);
            memberIdTextView = itemView.findViewById(R.id.tvUserMemberId);
            roleTextView     = itemView.findViewById(R.id.tvUserRole);
            editButton       = itemView.findViewById(R.id.btnEditUser);
            deleteButton     = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}
