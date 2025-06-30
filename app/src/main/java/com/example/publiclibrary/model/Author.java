package com.example.publiclibrary.model;
public class Author {
    private int id;
    private String name;
    private int booksCount;
    public Author() {}
    public Author(String name) {
        this.name = name;
        this.booksCount = 0;
    }

    public Author(int id, String name, int booksCount) {
        this.id = id;
        this.name = name;
        this.booksCount = booksCount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getBooksCount() { return booksCount; }
    public void setBooksCount(int booksCount) { this.booksCount = booksCount; }
}
