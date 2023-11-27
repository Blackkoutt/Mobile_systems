package com.example.libraryapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="book")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;

    // gettery i settery
}
