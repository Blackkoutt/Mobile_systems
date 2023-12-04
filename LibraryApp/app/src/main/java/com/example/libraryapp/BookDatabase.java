package com.example.libraryapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Odpowiada za połączenie z bazą danych, jej utworzenie i inicjację
@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    private static BookDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract BookDao bookDao();

    // Pobranie bazy
    static BookDatabase getDatabase(final Context context){
        if(databaseInstance == null){
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    BookDatabase.class, "book_database")
                    .addCallback(roomDataBaseCallback)
                    .build();
        }
        return databaseInstance;
    }

    // Dodatkowy Callback do insertu przykładowych danych
    private static final RoomDatabase.Callback roomDataBaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(()->{
                BookDao dao =databaseInstance.bookDao();
                Book book = new Book("Clean code", "Robert C. Martin");
                dao.insert(book);
            });
        }
    };
}
