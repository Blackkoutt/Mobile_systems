package com.example.zadanie_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptActivity extends AppCompatActivity {

    private Button answerButton;
    public static final String KEY_EXTRA_ANSWER_SHOWN = "answerShown";
    private TextView answerTextView;
    private boolean correctAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        correctAnswer = getIntent().getBooleanExtra(MainActivity.KEY_EXTRA_ANSWER, true);

        answerButton = findViewById(R.id.button_answer);
        answerTextView = findViewById(R.id.answer_text_view);

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int answer = correctAnswer ? R.string.button_true : R.string.button_false;
                int answer_color = correctAnswer ? R.color.green : R.color.red;
                answerTextView.setText(answer);
                answerTextView.setTextColor(ContextCompat.getColor(PromptActivity.this, answer_color));
                setAnswerShownResult(true);
                //finish();
            }
        });
    }
    private void setAnswerShownResult(boolean answerWasShown){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EXTRA_ANSWER_SHOWN, answerWasShown);
        setResult(RESULT_OK, resultIntent);
    }
}