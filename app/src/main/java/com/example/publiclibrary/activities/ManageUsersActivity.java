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
import com.example.publiclibrary.model.User;
import com.example.publiclibrary.adapter.UserAdapter;
import com.example.publiclibrary.helper.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity
        implements UserAdapter.OnUserActionListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddUser;
    private UserAdapter adapter;
    private DatabaseHelper db;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerView = findViewById(R.id.recyclerViewviewmember);
        fabAddUser = findViewById(R.id.fabAddUser);
        db = new DatabaseHelper(this);
        userList = new ArrayList<>(db.getAllUsers());

        adapter = new UserAdapter(this, userList, this, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAddUser.setOnClickListener(v -> showAddUserDialog());
    }

    private void showAddUserDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null);

        EditText etName = view.findViewById(R.id.etAddUserName);
        EditText etEmail = view.findViewById(R.id.etAddUserEmail);
        EditText etMemberId = view.findViewById(R.id.etAddUserMemberId);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        view.findViewById(R.id.btnSaveUser).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String memberId = etMemberId.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(memberId)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = "123456";
            String role = "student";
            String phone = "0594681210";

            User user = new User(name, email, phone, memberId, password, role);
            long id = db.addUser(user);
            if (id != -1) {
                userList.clear();
                userList.addAll(db.getAllUsers());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Member added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to add member", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onEdit(User user) {
        showEditUserDialog(user);
    }

    @Override
    public void onDelete(User user) {
        confirmDeleteUser(user);
    }

    private void showEditUserDialog(User user) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null);

        EditText etName = view.findViewById(R.id.etAddUserName);
        EditText etEmail = view.findViewById(R.id.etAddUserEmail);
        EditText etMemberId = view.findViewById(R.id.etAddUserMemberId);

        etName.setText(user.getName());
        etEmail.setText(user.getEmail());
        etMemberId.setText(user.getMemberId());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        view.findViewById(R.id.btnSaveUser).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String memberId = etMemberId.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(memberId)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setName(name);
            user.setEmail(email);
            user.setMemberId(memberId);

            int rows = db.updateUser(user);
            if (rows > 0) {
                userList.clear();
                userList.addAll(db.getAllUsers());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Member updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to update member", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void confirmDeleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Member")
                .setMessage("Are you sure you want to delete \"" + user.getName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteUser(user.getId());
                    userList.clear();
                    userList.addAll(db.getAllUsers());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Member deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
