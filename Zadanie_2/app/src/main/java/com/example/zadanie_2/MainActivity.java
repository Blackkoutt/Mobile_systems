package com.example.zadanie_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String QUIZ_TAG = "TAG"; // TAG dla logów
    private static boolean answerWasShown;  // Flaga informująca o tym czy użytkownik użył podpowiedzi

    // Klucze do przekazywanych parametrów między aktywnościami
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "correctAnswer";
    public static final String KEY_EXTRA_CORRECT_ANSWERS = "correctAnswers";
    public static final String KEY_EXTRA_INCORRECT_ANSWERS = "incorrectAnswers";
    public static final String KEY_EXTRA_CHECKED_ANSWERS = "checkedAnswers";
    public static final String KEY_EXTRA_QUESTIONS_LENGTH = "questionsLength";
    private static final int REQUEST_CODE_PROMPT = 0;

    // Deklracja elementów widoku
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private TextView questionNrTextView;

    // Tabela pytań wraz z odpowiedziami
    private Question[] questions = new Question[]{
            new Question(R.string.q_language, true),
            new Question(R.string.q_process, false),
            new Question(R.string.q_components, false),
            new Question(R.string.q_resumed_state, true),
            new Question(R.string.q_on_pause, true),
            new Question(R.string.q_fragment, false),
            new Question(R.string.q_service, true),
            new Question(R.string.q_bound_service, false),
            new Question(R.string.q_content_provider, false),
            new Question(R.string.q_broadcast_reciever, false)
    };

    // Liczniki
    private int currentIndex=0;
    private int correctAnswers=0;
    private int incorrectAnswers=0;
    private int checkedAnswers=0;

    // Metoda wywoływana w momencie zakończenia PromptActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Jeśli otrzymany kod rezulatatu wykonania aktywności nie jest poprawny zakończ metodę
        if(resultCode != RESULT_OK){
            return;
        }

        // Jeśli jest to aktywność PromptActivity
        if(requestCode == REQUEST_CODE_PROMPT){
            // Jeśli PromptActivity nie zwróciło danych zakończ metodę
            if(data == null){
                return;
            }
            answerWasShown =  data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);   // Pobranie otrzymanej wartości
        }
    }

    // Metoda służaca do zapisania stanu aplikacji
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState); // Wywołanie metody klasy nadrzędnej
        Log.d(QUIZ_TAG,getString(R.string.on_save_instance_state)); // Zapis informacji do logów w trybie debug
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);   // Zapis stanu aplikacji (indeksu aktualnego pytania)
    }

    // Metody cyklu życia aktywności
    @Override
    protected void onStart() {
        super.onStart();    // Wywołanie metody klasy nadrzędnej
        Log.d(QUIZ_TAG,getString(R.string.on_start)); // Zapis informacji do logów w trybie debug
    }

    @Override
    protected void onStop() {
        super.onStop(); // Wywołanie metody klasy nadrzędnej
        Log.d(QUIZ_TAG,getString(R.string.on_stop)); // Zapis informacji do logów w trybie debug
    }

    @Override
    protected void onDestroy() {
        super.onDestroy(); // Wywołanie metody klasy nadrzędnej
        Log.d(QUIZ_TAG,getString(R.string.on_destroy)); // Zapis informacji do logów w trybie debug
    }

    @Override
    protected void onPause() {
        super.onPause(); // Wywołanie metody klasy nadrzędnej
        Log.d(QUIZ_TAG,getString(R.string.on_pause)); // Zapis informacji do logów w trybie debug
    }

    @Override
    protected void onResume() {
        super.onResume(); // Wywołanie metody klasy nadrzędnej
        Log.d(QUIZ_TAG,getString(R.string.on_resume)); // Zapis informacji do logów w trybie debug
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Wywołanie metody klasy nadrzędnej
        Log.d(QUIZ_TAG, getString(R.string.on_create)); // Zapis informacji do logów w trybie debug
        setContentView(R.layout.activity_main); // Powiązanie widoku

        // Jeśli wystąpiła zmiana stanu aplikacji
        if(savedInstanceState != null){
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX); // Przywrócenie wcześniejszego (zapisanego) stanu aplikacji (pytania)
        }

        // Pobranie elementów widoku
        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        promptButton=findViewById(R.id.prompt_button);
        nextButton=findViewById(R.id.next_button);
        questionTextView=findViewById(R.id.question_text_view);
        questionNrTextView=findViewById(R.id.question_nr_text_view);

        // Metody nasłuchujące wciśnięcie przycisku

        // Przycisk podpowiedź
        promptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jeśli został wciśnięty przycisk podpowiedź deklarujemy nową aktywność PromptActivity
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].isTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);   // Przekazanie dodatkowych informacji do uruchamianej aktywności
                startActivityForResult(intent, REQUEST_CODE_PROMPT);    // Uruchomienie nowej aktywności PromptActivity
            }
        });

        // Przycisk Prawda
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrectness(true);   // Sprawdzenie poprawności odpowiedzi
            }
        });

        // Przycisk Fałsz
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkAnswerCorrectness(false); // Sprawdzenie poprawności odpowiedzi
            }
        });

        // Przycisk Następne pytanie
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex = (currentIndex+1);    // Zwiększenie indexu pytania

                // Jeśli index pytania jest równy ilości pytań (użytkownik wcisnął przycisk "nastepne" przy ostatnim pytaniu)
                if(currentIndex == questions.length){
                    Intent intent = new Intent(MainActivity.this, SummaryActivity.class); // Deklaracja nowej aktywności SummaryActivity

                    // Przekazanie dodatkowych informacji dotyczących ilości odpowiedzi
                    Bundle extra = new Bundle();
                    extra.putInt(KEY_EXTRA_CORRECT_ANSWERS,correctAnswers);
                    extra.putInt(KEY_EXTRA_INCORRECT_ANSWERS, incorrectAnswers);
                    extra.putInt(KEY_EXTRA_CHECKED_ANSWERS, checkedAnswers);
                    extra.putInt(KEY_EXTRA_QUESTIONS_LENGTH, questions.length);

                    intent.putExtras(extra);

                    startActivity(intent);  // Uruchomienie nowej aktywności SummaryActivity

                    // Po zakończeniu aktywności (podsumowania) - wyzerowanie liczników
                    currentIndex=0;
                    correctAnswers=0;
                    incorrectAnswers=0;
                    checkedAnswers=0;
                }
                answerWasShown=false;   // Ustawienie flagi informującej o braku wyświetlenia podpowiedzi
                setNextQuestion();  // Ustawienie nowego pytania
            }
        });
        setNextQuestion(); // Ustawienie nowego pytania
    }

    // Metoda ustawiająca kolejne pytanie
    private void setNextQuestion(){
        // Ustawienie przycisków jako aktywne
        trueButton.setEnabled(true);
        falseButton.setEnabled(true);
        promptButton.setEnabled(true);
        questionTextView.setText(questions[currentIndex].getQuestionId());  // Ustawienie kolejnego pytania
        questionNrTextView.setText("Pytanie "+(currentIndex+1)+"/"+questions.length+":");   // Ustawienie numeru pytania
    }

    // Metoda sprawdzająca poprawność odpowiedzi
    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrueAnswer(); // Pobranie poprawnej odpowiedzi
        int resultMessageId = 0;

        // Jeśli użytkownik wyświetlił podpowiedź
        if(answerWasShown){
            resultMessageId = R.string.answer_was_shown; // Przypisujemy do rezultatu wiadomość o wyświeleniu odpowiedzi
            checkedAnswers++;   // Zwiększenie licznika użytych podpowiedzi
        }
        else {
            if (userAnswer == correctAnswer) {
                correctAnswers++; // Zwiększenie licznika poprawnych podpowiedzi
                resultMessageId = R.string.correct_answer;  // Przypisujemy do rezultatu wiadomość o poprawnej odpowiedzi
            } else {
                incorrectAnswers++; // Zwiększenie licznika niepoprawnych podpowiedzi
                resultMessageId = R.string.incorrect_answer; // Przypisujemy do rezultatu wiadomość o niepoprawnej odpowiedzi
            }

        }

        // Po udzieleniu odpowiedzi blokowane jest użycie przycisków aby nie doprowadzić do sytuacji w której "spamowanie" w jeden przycisk wyświetla wiele komunikatów
        trueButton.setEnabled(false);
        falseButton.setEnabled(false);
        promptButton.setEnabled(false);
        Toast.makeText(this,resultMessageId, Toast.LENGTH_SHORT).show();

    }
}