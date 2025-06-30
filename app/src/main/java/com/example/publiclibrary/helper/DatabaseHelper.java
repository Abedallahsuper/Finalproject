package com.example.publiclibrary.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.publiclibrary.model.User;
import com.example.publiclibrary.model.BorrowedBook;
import com.example.publiclibrary.model.Author;
import com.example.publiclibrary.model.Book;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LibraryApp.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    private static final String COL_USER_MEMBER_ID = "member_id";
    public static final String COL_ROLE = "role";
    public static final String COL_PHONE = "phone";
    private static final String TABLE_BOOKS = "books";
    private static final String COLUMN_BOOK_ID = "id";
    private static final String COLUMN_BOOK_TITLE = "title";
    private static final String COLUMN_BOOK_AUTHOR = "author";
    private static final String COLUMN_BOOK_CATEGORY = "category";
    private static final String COLUMN_BOOK_DESCRIPTION = "description";
    private static final String COLUMN_BOOK_IMAGE_URI   = "imageUri";
    private static final String TABLE_BORROWED = "borrowed_books";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {//
        String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT, " +
                COL_PHONE+ " TEXT, " +
                COL_USER_MEMBER_ID + " TEXT)";
        db.execSQL(CREATE_TABLE_USERS);

        String CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOK_TITLE + " TEXT, " +
                COLUMN_BOOK_AUTHOR + " TEXT, " +
                COLUMN_BOOK_CATEGORY + " TEXT, " +
                COLUMN_BOOK_DESCRIPTION + " TEXT, " +
                COLUMN_BOOK_IMAGE_URI + " TEXT)";

        db.execSQL(CREATE_BOOKS_TABLE);
        String CREATE_BORROWED_BOOKS = "CREATE TABLE IF NOT EXISTS "+TABLE_BORROWED+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "book_id INTEGER, " +
                "user_id INTEGER, " +
                "date TEXT, " +
                "FOREIGN KEY(book_id) REFERENCES books(id), " +
                "FOREIGN KEY(user_id) REFERENCES users(id))";
        db.execSQL(CREATE_BORROWED_BOOKS);
        db.execSQL("CREATE TABLE IF NOT EXISTS authors (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "books_count INTEGER DEFAULT 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS borrowed_books");
        onCreate(db);
    }


    public boolean registerUser(String name, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=?", new String[]{email});
        if (c.getCount() > 0) {
            c.close();
            return false;
        }

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_EMAIL, email);
        cv.put(COL_PASSWORD, password);
        cv.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password}
        );

        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_ROLE + " FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        } else {
            cursor.close();
            return null;
        }

    }
    public long addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_NAME, user.getName());
        v.put(COL_EMAIL, user.getEmail());
        v.put("phone", user.getPhone());
        v.put(COL_USER_MEMBER_ID, user.getMemberId());
        v.put(COL_PASSWORD, user.getPassword());
        v.put(COL_ROLE, user.getRole());
        long id = db.insert(TABLE_USERS, null, v);
        db.close();
        return id;
    }
    public int updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_NAME,      user.getName());
        v.put(COL_EMAIL,     user.getEmail());
        v.put(COL_PHONE, user.getPhone());
        v.put(COL_USER_MEMBER_ID, user.getMemberId());
        v.put(COL_PASSWORD,  user.getPassword());
        v.put(COL_ROLE,      user.getRole());


        return db.update(
                TABLE_USERS, v,
                COL_ID + " = ?",
                new String[]{ String.valueOf(user.getId()) }
        );
    }
    public void deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                TABLE_USERS,
                COL_ID + " = ?",
                new String[]{ String.valueOf(id) }
        );
        db.close();
    }
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_USERS, null, null, null, null, null,
                COL_ID + " DESC"
        );

        if (c.moveToFirst()) {
            do {
                User u = new User();
                u.setId(         c.getInt(   c.getColumnIndexOrThrow(COL_ID) ) );
                u.setName(       c.getString(c.getColumnIndexOrThrow(COL_NAME)) );
                u.setEmail(      c.getString(c.getColumnIndexOrThrow(COL_EMAIL)) );
                u.setPhone(c.getString(c.getColumnIndexOrThrow("phone")));
                u.setMemberId(   c.getString(c.getColumnIndexOrThrow(COL_USER_MEMBER_ID)) );
                u.setPassword(   c.getString(c.getColumnIndexOrThrow(COL_PASSWORD)) );
                u.setRole(       c.getString(c.getColumnIndexOrThrow(COL_ROLE)) );
                list.add(u);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }
    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE, book.getTitle());
        values.put(COLUMN_BOOK_AUTHOR, book.getAuthor());
        values.put(COLUMN_BOOK_CATEGORY, book.getCategory());
        values.put(COLUMN_BOOK_DESCRIPTION, book.getDescription());
        values.put(COLUMN_BOOK_IMAGE_URI, book.getImageUri());
        long id = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return id;
    }
    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE, book.getTitle());
        values.put(COLUMN_BOOK_AUTHOR, book.getAuthor());
        values.put(COLUMN_BOOK_CATEGORY, book.getCategory());
        values.put(COLUMN_BOOK_DESCRIPTION, book.getDescription());
        values.put(COLUMN_BOOK_IMAGE_URI, book.getImageUri());
        return db.update(TABLE_BOOKS, values,
                COLUMN_BOOK_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
    }
    public void deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, COLUMN_BOOK_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, null, null, null, null, COLUMN_BOOK_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)));
                book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE)));
                book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR)));
                book.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY)));
                book.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                book.setImageUri(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_IMAGE_URI)));
                books.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return books;
    }
    public List<BorrowedBook> getAllBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT b.id, bk.title, bk.author, bk.category, u.name, u.id, b.date, bk.imageUri " +
                "FROM borrowed_books b " +
                "JOIN books bk ON b.book_id = bk.id " +
                "JOIN users u ON b.user_id = u.id";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int borrowId = cursor.getInt(0);
                String bookTitle = cursor.getString(1);
                String bookAuthor = cursor.getString(2);
                String bookCategory = cursor.getString(3);
                String userName = cursor.getString(4);
                int userId = cursor.getInt(5);
                String borrowDate = cursor.getString(6);
                String imageUri = cursor.getString(7);
                BorrowedBook borrowedBook = new BorrowedBook(
                        borrowId, bookTitle, bookAuthor, bookCategory,
                        userName, userId, borrowDate, imageUri
                );
                borrowedBooks.add(borrowedBook);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return borrowedBooks;
    }

    public List<BorrowedBook> getBorrowedBooksByStudent(int studentId) {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT b.id, bk.title, bk.author, bk.category, u.name, u.id, b.date,  bk.imageUri " +
                "FROM borrowed_books b " +
                "JOIN " + TABLE_BOOKS + " bk ON b.book_id = bk.id " +
                "JOIN " + TABLE_USERS + " u ON b.user_id = u.id " +
                "WHERE u.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});
        if (cursor.moveToFirst()) {
            do {
                int borrowId = cursor.getInt(0);
                String bookTitle = cursor.getString(1);
                String bookAuthor = cursor.getString(2);
                String bookCategory = cursor.getString(3);
                String userName = cursor.getString(4);
                int userId = cursor.getInt(5);
                String borrowDate = cursor.getString(6);
                String imageUri = cursor.getString(7);
                BorrowedBook borrowedBook = new BorrowedBook(
                        borrowId, bookTitle, bookAuthor, bookCategory,
                        userName, userId, borrowDate, imageUri
                );
                borrowedBooks.add(borrowedBook);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return borrowedBooks;
    }

    public int getUserIdByEmail(String email) {
        int userId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_ID + " FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=?", new String[]{email});
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }
    public boolean deleteBorrowedBook(int borrowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("borrowed_books", "id = ?", new String[]{String.valueOf(borrowId)});
        db.close();
        return result > 0;
    }
    public long addBorrowRecord(int bookId, int userId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("book_id", bookId);
        values.put("user_id", userId);
        values.put("date", date);

        long result = db.insert("borrowed_books", null, values);
        db.close();
        return result;
    }
    public long addAuthor(Author author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", author.getName());
        values.put("books_count", 0);
        return db.insert("authors", null, values);
    }
    public int getBooksCountForAuthor(String authorName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM books WHERE author = ?", new String[]{authorName});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public List<Author> getAllAuthors() {
        List<Author> authorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM authors", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int booksCount = getBooksCountForAuthor(name);
                Author author = new Author(id, name, booksCount);
                authorList.add(author);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return authorList;
    }

    public User getUserById(int userId) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id, name, email, phone, member_id, password, role FROM " + TABLE_USERS + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String phone = cursor.getString(3);
            String memberId = cursor.getString(4);
            String password = cursor.getString(5);
            String role = cursor.getString(6);

            user = new User(id, name, email, phone, memberId, password, role);
        }
        cursor.close();
        db.close();

        return user;
    }



}




