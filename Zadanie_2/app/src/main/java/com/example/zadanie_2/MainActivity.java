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
    private static final String QUIZ_TAG = "TAG";
    private static boolean answerWasShown;
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "correctAnswer";
    public static final String KEY_EXTRA_CORRECT_ANSWERS = "correctAnswers";
    public static final String KEY_EXTRA_INCORRECT_ANSWERS = "incorrectAnswers";
    public static final String KEY_EXTRA_CHECKED_ANSWERS = "checkedAnswers";
    public static final String KEY_EXTRA_QUESTIONS_LENGTH = "questionsLength";
    private static final int REQUEST_CODE_PROMPT = 0;
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private TextView questionNrTextView;
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
    private int currentIndex=0;
    private int correctAnswers=0;
    private int incorrectAnswers=0;
    private int checkedAnswers=0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_PROMPT){
            if(data == null){
                return;
            }
            answerWasShown =  data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(QUIZ_TAG,getString(R.string.on_save_instance_state));
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(QUIZ_TAG,getString(R.string.on_start));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(QUIZ_TAG,getString(R.string.on_stop));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(QUIZ_TAG,getString(R.string.on_destroy));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(QUIZ_TAG,getString(R.string.on_pause));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(QUIZ_TAG,getString(R.string.on_resume));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(QUIZ_TAG, getString(R.string.on_create));
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }
        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        promptButton=findViewById(R.id.prompt_button);
        nextButton=findViewById(R.id.next_button);
        questionTextView=findViewById(R.id.question_text_view);
        questionNrTextView=findViewById(R.id.question_nr_text_view);

        promptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].isTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrectness(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex = (currentIndex+1);
                if(currentIndex == questions.length){
                    Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                    Bundle extra = new Bundle();
                    extra.putInt(KEY_EXTRA_CORRECT_ANSWERS,correctAnswers);
                    extra.putInt(KEY_EXTRA_INCORRECT_ANSWERS, incorrectAnswers);
                    extra.putInt(KEY_EXTRA_CHECKED_ANSWERS, checkedAnswers);
                    extra.putInt(KEY_EXTRA_QUESTIONS_LENGTH, questions.length);

                    intent.putExtras(extra);

                    startActivity(intent);

                    currentIndex=0;
                    correctAnswers=0;
                    incorrectAnswers=0;
                    checkedAnswers=0;
                }
                answerWasShown=false;
                setNextQuestion();
            }
        });
        setNextQuestion();
    }
    private void setNextQuestion(){
        trueButton.setEnabled(true);
        falseButton.setEnabled(true);
        promptButton.setEnabled(true);
        questionTextView.setText(questions[currentIndex].getQuestionId());
        questionNrTextView.setText("Pytanie "+(currentIndex+1)+"/"+questions.length+":");
    }
    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if(answerWasShown){
            resultMessageId = R.string.answer_was_shown;
            checkedAnswers++;
            //trueButton.setEnabled(false);
            //falseButton.setEnabled(false);
        }
        else {
            if (userAnswer == correctAnswer) {
                correctAnswers++;
                resultMessageId = R.string.correct_answer;
            } else {
                incorrectAnswers++;
                resultMessageId = R.string.incorrect_answer;
            }

        }
        trueButton.setEnabled(false);
        falseButton.setEnabled(false);
        promptButton.setEnabled(false);
        Toast.makeText(this,resultMessageId, Toast.LENGTH_SHORT).show();

    }
}