package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        nextButton=findViewById(R.id.next_button);
        questionTextView=findViewById(R.id.question_text_view);
    }
    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if(userAnswer == correctAnswer){
            resultMessageId=R.string.correct_answer;
        }
        else{
            resultMessageId=R.string.incorrect_answer;
        }
        Toast.makeText(this,resultMessageId, Toast.LENGTH_SHORT).show();

    }
}