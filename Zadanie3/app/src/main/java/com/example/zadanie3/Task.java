package com.example.zadanie3;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;    // Id zadania
    private String name;    // Nazwa zadania
    private Date date;  // Data wykonania zadania
    private boolean done;   // Czy zrobione

    public Task(){
        id=UUID.randomUUID();
        date= new Date();
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
    public boolean isDone(){
        return this.done;
    }
    public void setDone(boolean done){
        this.done=done;
    }
    public UUID getID(){
        return this.id;
    }

}
