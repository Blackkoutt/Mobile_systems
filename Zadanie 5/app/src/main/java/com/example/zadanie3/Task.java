package com.example.zadanie3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Task {
    private UUID id;    // Id zadania
    private String name;    // Nazwa zadania
    private Date date;  // Data wykonania zadania
    private Category category; // Kategoria zadania
    private boolean done;   // Czy zrobione

    public Task(){
        id=UUID.randomUUID();
        date= new Date();
        category = Category.HOME; // Ustawienie domyślnej kategorii
    }
    // Metody dostępowe
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Date getDate(){
        return this.date;
    }
    public void setDate(Date date){
        this.date=date;
    }
    public boolean isDone(){
        return this.done;
    }
    public void setDone(boolean done){
        this.done=done;
    }
    public UUID getID(){
        return this.id;
    }
    public Category getCategory(){
        return category;
    }
    public void setCategory(Category category){
        this.category=category;
    }

}
