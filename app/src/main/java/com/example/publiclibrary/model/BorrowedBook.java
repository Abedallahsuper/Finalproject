package com.example.publiclibrary.model;

public class BorrowedBook {
    private int id;
    private String name;
    private String author;
    private String category;
    private String memberName;
    private int memberId;
    private String borrowDate;
    private String imageUri;
    public BorrowedBook(int id, String name, String author, String category, String memberName, int memberId, String borrowDate, String imageUri) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.category = category;
        this.memberName = memberName;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.imageUri = imageUri;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public int getMemberId() {
        return memberId;
    }
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    public String getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }
    public String getImageUri() {
        return imageUri;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
