package com.example.publiclibrary.model;
public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String memberId;
    private String password;
    private String role;

    public User(int id, String name, String email, String phone, String memberId, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberId = memberId;
        this.password = password;
        this.role = role;
    }

    public User(String name, String email, String phone, String memberId, String password, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberId = memberId;
        this.password = password;
        this.role = role;
    }

    public User() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
