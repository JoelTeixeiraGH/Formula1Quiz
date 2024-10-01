package pt.ipp.estg.formula1q.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import net.glxn.qrgen.android.QRCode;

import java.util.Locale;

import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.database.Firebase;
import pt.ipp.estg.formula1q.database.question.QuestionRepository;
import pt.ipp.estg.formula1q.models.AnsweredQuestion;
import pt.ipp.estg.formula1q.models.Question;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{
    private static final long START_TIME_IN_MILLIS = 15*1000;
    private static final int SPECIAL_QUESTION_POINTS = 5;
    private final int DEFAULT_QUESTION_POINTS = 1;
    private TextView question_text, question_timer, choiceA, choiceB, choiceC;
    private LinearLayout btnQRCode;
    private int correctAnswer, userAnswer, questionPoints, questionId, sharedQuestionId;
    private boolean isSpecialQuestion, isSharedQuestion;
    private QuestionViewModel questionViewModel;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private String eventName, eventDate;
    private Question question;
    private boolean canceled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getSupportActionBar().hide();

        question_text = findViewById(R.id.question_title);
        question_timer = findViewById(R.id.question_timer);
        choiceA = findViewById(R.id.choice_A);
        choiceB = findViewById(R.id.choice_B);
        choiceC = findViewById(R.id.choice_C);
        btnQRCode = findViewById(R.id.btnQRCode);

        choiceA.setOnClickListener(this);
        choiceB.setOnClickListener(this);
        choiceC.setOnClickListener(this);

        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQuestion();
            }
        });

        isSpecialQuestion = getIntent().getBooleanExtra("isSpecialQuestion", false);
        isSharedQuestion = getIntent().getBooleanExtra("isSharedQuestion", false);
        sharedQuestionId = getIntent().getIntExtra("sharedQuestionId", -1);
        eventName = getIntent().getStringExtra("eventName");
        eventDate = getIntent().getStringExtra("eventDate");

        questionViewModel = new ViewModelProvider(this,
                new QuestionViewModel.QuestionViewModelFactory(getApplication(),isSharedQuestion, sharedQuestionId))
                .get(QuestionViewModel.class);

        questionViewModel.getQuestionObservable()
                    .observe(QuestionActivity.this, this::setUI);

        timeLeftInMillis = START_TIME_IN_MILLIS;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int minutes = (int) (timeLeftInMillis/1000)/60;
                int seconds = (int) (timeLeftInMillis/1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes, seconds);
                question_timer.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(QuestionActivity.this, MainActivity.class));
            }
        };

        canceled = false;
    }
    //
    private void setUI(Question question){
        this.question = question;
        if(question == null){
            canceled = true;
            QuestionRepository.getInstance(this).setHasMoreQuestions(false);
            launchMainActivity();
        } else {
            countDownTimer.start();
            questionId = question.getId();
            question_text.setText(question.getQuestion());
            if(question_text.length() >= 100) question_text.setTextSize(20);
            choiceA.setText(question.getChoiceA());
            if(choiceA.length() > 20)choiceA.setTextSize(15);
            choiceB.setText(question.getChoiceB());
            if(choiceB.length() > 20)choiceB.setTextSize(15);
            choiceC.setText(question.getChoiceC());
            if(choiceC.length() > 20)choiceC.setTextSize(15);
            correctAnswer = question.getAnswer();
            questionPoints = isSpecialQuestion ? SPECIAL_QUESTION_POINTS : DEFAULT_QUESTION_POINTS;
        }
    }

    private void setUIAfterAnswer(boolean answeredCorrectly){
        if(answeredCorrectly){
            Toast.makeText(this, "Good job!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Boo!", Toast.LENGTH_SHORT).show();
        }

        launchMainActivity();
    }

    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void shareQuestion(){
        countDownTimer.cancel();
        Bitmap qrCode = QRCode.from(
                String.valueOf(questionId))
                .withSize(750, 750)
                .withCharset("UTF-8")
                .bitmap();

        Dialog dialogQR = new Dialog(this);
        dialogQR.setContentView(R.layout.dialog_qr);
        Button close = dialogQR.findViewById(R.id.btn_close);
        ImageView qrImage = dialogQR.findViewById(R.id.imageView);
        qrImage.setImageBitmap(qrCode);
        close.setEnabled(true);
        dialogQR.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                canceled = true;
                launchMainActivity();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogQR.cancel();
            }
        });
        dialogQR.show();
    }

    @Override
    public void onClick(View v) {
        boolean answeredCorrectly = false;

        switch(v.getId()){
            case R.id.choice_A:
                if(correctAnswer == 0){
                    answeredCorrectly = true;
                    userAnswer = 0;
                }
                break;
            case R.id.choice_B:
                if(correctAnswer == 1){
                    answeredCorrectly = true;
                    userAnswer = 1;
                }
                break;
            case R.id.choice_C:
                if(correctAnswer == 2){
                    answeredCorrectly = true;
                    userAnswer = 2;
                }
                break;
            default:
                answeredCorrectly = false;
        }

        if(answeredCorrectly){
            new ViewModelProvider(this).get(MainActivityViewModel.class).addPoints(questionPoints);
        }

        QuestionRepository.getInstance(this).setAnswered(questionId);

        setUIAfterAnswer(answeredCorrectly);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("You won't be able to answer this question again.\n" +
                        "Are you sure you want to leave?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        canceled = true;
                        QuestionRepository.getInstance(QuestionActivity.this).setAnswered(questionId);
                        QuestionActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * Mark question as answered and add to list of user answered questions
     */
    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
        if(!canceled) {
            Firebase.addAnsweredQuestion(new AnsweredQuestion(question, userAnswer, questionPoints),
                    eventName, eventDate);
            QuestionRepository.getInstance(this).setAnswered(questionId);
        }
    }

}
