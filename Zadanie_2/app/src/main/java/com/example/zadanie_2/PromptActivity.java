package com.example.zadanie_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptActivity extends AppCompatActivity {

    // Deklracja elementów widoku
    private Button answerButton;
    private TextView answerTextView;

    // Klucz do przekazywania parametrów między aktywnościami
    public static final String KEY_EXTRA_ANSWER_SHOWN = "answerShown";
    private boolean correctAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Wywołanie metody klasy nadrzędnej
        setContentView(R.layout.activity_prompt);   // Powiązanie z widokiem aktywności

        // Pobranie danych otrzymanych z MainActivity
        correctAnswer = getIntent().getBooleanExtra(MainActivity.KEY_EXTRA_ANSWER, true);

        // Pobranie elementów widoku
        answerButton = findViewById(R.id.button_answer);
        answerTextView = findViewById(R.id.answer_text_view);


        // Metoda nasłuchująca klikniecie przycisku "Pokaż Odpowiedz"
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int answer = correctAnswer ? R.string.button_true : R.string.button_false;  // Jeśli correctAnswer ma wartość True przypisz Prawda jeśli nie przypisz Fałsz
                int answer_color = correctAnswer ? R.color.green : R.color.red; // Analogicznie z kolorem
                answerTextView.setText(answer); // Wyświetl odpowiedź
                answerTextView.setTextColor(ContextCompat.getColor(PromptActivity.this, answer_color)); // Ustaw kolor odpowiedzi
                setAnswerShownResult(true); // Ustawienie rezultatu aktywności wraz z flagą informującą o wyświetleniu odpowiedzi
            }
        });
    }

    // Metoda ustawiająca rezulat aktywności PromptActivity
    private void setAnswerShownResult(boolean answerWasShown){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EXTRA_ANSWER_SHOWN, answerWasShown);  // Dodanie flagi informującej o wyświetleniu odpowiedzi
        setResult(RESULT_OK, resultIntent); // Ustawienie rezultatu aktywności
    }
}