package com.example.publiclibrary.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private String description;
    private String imageUri;
    public Book() {}
    public Book(int id,
                String title,
                String author,
                String category,
                String description,
                String imageUri) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.imageUri = imageUri;
    }
    public Book(String title,
                String author,
                String category,
                String description,
                String imageUri) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.imageUri = imageUri;
    }
    public Book(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = null;
        this.imageUri = null;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
