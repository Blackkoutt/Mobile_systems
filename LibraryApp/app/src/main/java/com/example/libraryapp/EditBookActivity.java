package com.example.libraryapp;

import static com.example.libraryapp.MainActivity.KEY_EXTRA_BOOK_AUTHOR;
import static com.example.libraryapp.MainActivity.KEY_EXTRA_BOOK_TITLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditBookActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_BOOK_TITLE ="EDIT_BOOK_TITLE";
    public static final String EXTRA_EDIT_BOOK_AUTHOR = "EDIT_BOOK_AUTHOR";

    private TextView AddEditBookTextView;
    private EditText editTitleEditText;
    private EditText editAuthorEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTitleEditText = findViewById(R.id.edit_book_title);
        editAuthorEditText = findViewById(R.id.edit_book_author);
        AddEditBookTextView = findViewById(R.id.edit_or_add_text_view);
        AddEditBookTextView.setText(R.string.add_book);

        // Jeśli przesłano dane to będzie dokonywana edycja
        // Do pól formularza przypisywane są poszczególne elementy
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            AddEditBookTextView.setText(R.string.edit_book);
            String title = extras.getString(KEY_EXTRA_BOOK_TITLE);
            editTitleEditText.setText(title);
            String author = extras.getString(KEY_EXTRA_BOOK_AUTHOR);
            editAuthorEditText.setText(author);
        }

        // OnClick listener dla przycisku save
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view->{
            Intent replyIntent = new Intent();

            // Jeśli nic nie wpisano
            if(TextUtils.isEmpty(editTitleEditText.getText())||TextUtils.isEmpty(editAuthorEditText.getText())){
                setResult(RESULT_CANCELED, replyIntent);
            }
            // Jeśli cokolwiek znajduje się w formularzu
            else{
                // Zwrócenie parametrów
                String title = editTitleEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_BOOK_TITLE, title);
                String author = editAuthorEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_BOOK_AUTHOR, author);
                setResult(RESULT_OK, replyIntent);
            }
            finish(); // Zakończenie aktywności
        });

    }
}