package com.example.zadanie9;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookContainer {
    // Potrzebna ponieważ odpowiedź z serwera w formacie JSON należy odpowiednio dostosować

    // Lista książek
    @SerializedName("docs")
    private List<Book> bookList;

    public List<Book> getBookList(){
        return this.bookList;
    }
    public void setBookList(List<Book> bookList){
        this.bookList = bookList;
    }
}
