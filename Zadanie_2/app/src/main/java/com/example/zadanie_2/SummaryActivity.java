package com.example.zadanie_2;

import static com.example.zadanie_2.MainActivity.KEY_EXTRA_CHECKED_ANSWERS;
import static com.example.zadanie_2.MainActivity.KEY_EXTRA_CORRECT_ANSWERS;
import static com.example.zadanie_2.MainActivity.KEY_EXTRA_INCORRECT_ANSWERS;
import static com.example.zadanie_2.MainActivity.KEY_EXTRA_QUESTIONS_LENGTH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SummaryActivity extends AppCompatActivity {

    private int correctAnswers;
    private int incorrectAnswers;
    private int checkedAnswers;
    private int ommitedQuestions;
    private int questionsLength;
    private Button playAgainButton;

    private TextView correctAnswersTextView;
    private TextView incorrectAnswersTextView;
    private TextView checkedAnswersTextView;
    private TextView ommitedQuestionsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            correctAnswers=extra.getInt(KEY_EXTRA_CORRECT_ANSWERS);
            incorrectAnswers = extra.getInt(KEY_EXTRA_INCORRECT_ANSWERS);
            checkedAnswers = extra.getInt(KEY_EXTRA_CHECKED_ANSWERS);
            questionsLength = extra.getInt(KEY_EXTRA_QUESTIONS_LENGTH);
        }

        correctAnswersTextView = findViewById(R.id.correct_answers_text_view);
        incorrectAnswersTextView = findViewById(R.id.incorrect_answers_text_view);
        checkedAnswersTextView = findViewById(R.id.checked_answers_text_view);
        ommitedQuestionsTextView = findViewById(R.id.ommited_questions_text_view);
        playAgainButton = findViewById(R.id.play_again_button);

        ommitedQuestions = questionsLength - correctAnswers - incorrectAnswers - checkedAnswers;

        correctAnswersTextView.setText(getString(R.string.correct_answers_info)+" "+correctAnswers);
        incorrectAnswersTextView.setText(getString(R.string.incorrect_answers_info)+" "+incorrectAnswers);
        checkedAnswersTextView.setText(getString(R.string.checked_answers_info)+" "+ checkedAnswers);
        ommitedQuestionsTextView.setText(getString(R.string.ommited_questions_info)+" "+ ommitedQuestions);

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}