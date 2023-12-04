package com.example.libraryapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Klasa modelu książki - tabela książka
@Entity(tableName="book")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;

    public Book (String title, String author){
        this.title=title;
        this.author=author;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    // gettery i settery

    public String getTitle() {
        return title;
    }
    public void setTitle(String value){
        this.title = value;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String value){
        this.author = value;
    }
}
