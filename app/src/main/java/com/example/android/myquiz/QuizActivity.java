package com.example.android.myquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.myquiz.R.id.question;

public class QuizActivity extends AppCompatActivity {
    private int score = 0;
    private int total = 0;
    private int progress = 0;
    private String buttonTxt;
    public static final String SCORE = "score";
    public static final String TOTAL = "total";
    public static final String PROGRESS = "progress";
    public static final String BUTTON = "buttonTxt";
    private static List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.questions == null) {
            this.questions = getQuestions();
        }
        setContentView(R.layout.activity_quiz);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState == null) {
                EditTextFragment firstFragment = new EditTextFragment();
                firstFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
                firstFragment.onCreateView(getLayoutInflater(), null, null);
                displayQuestion(questions.get(progress));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE, score);
        outState.putInt(TOTAL, total);
        outState.putInt(PROGRESS, progress);
        Button button = (Button) findViewById(R.id.submit);
        outState.putString(BUTTON, button.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        score = savedInstanceState.getInt(SCORE);
        total = savedInstanceState.getInt(TOTAL);
        progress = savedInstanceState.getInt(PROGRESS);
        buttonTxt = savedInstanceState.getString(BUTTON);
        TextView questionView = (TextView) findViewById(question);
        questionView.setText(questions.get(progress).Text);
        displayProgress(progress);
        Button button = (Button) findViewById(R.id.submit);
        button.setText(buttonTxt);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        QuizActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onSubmitClick(View view) {
        Button button = (Button) findViewById(R.id.submit);
        if (button.getText().toString().equalsIgnoreCase(getString(R.string.submit))) {
            Question currentQuestion = questions.get(progress);
            boolean correct = false;
            if (currentQuestion.IsEditable) {
                total++;
                EditTextFragment f = (EditTextFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentQuestion.isCorrect(f.getAnswer())) {
                    score++;
                    correct = true;
                }
            } else if (currentQuestion.correctAnswerCount() > 1) {
                total += currentQuestion.correctAnswerCount();
                CheckBoxFragment f = (CheckBoxFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                int correctNum = 0;
                for (String answer : f.getSelectedAnswers()) {
                    if (currentQuestion.isCorrect(answer)) {
                        score++;
                        correctNum++;
                    }
                }
                correct = correctNum == currentQuestion.correctAnswerCount();
            } else {
                total++;
                RadioButtonFragment f = (RadioButtonFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentQuestion.isCorrect(f.getSelectedAnswer())) {
                    score++;
                    correct = true;
                }
            }
            Toast.makeText(this, correct ? getString(R.string.correct_toast) : getString(R.string.toast) + currentQuestion.correctAnswerToast(), Toast.LENGTH_SHORT).show();
            if (progress + 1 == questions.size()) {
                button.setText(getString(R.string.finish));
            } else {
                button.setText(getString(R.string.next));
            }
        } else {
            progress++;
            displayProgress(progress);
            if (progress == questions.size()) {
                startSummary(view);
            } else {
                displayQuestion(questions.get(progress));
                button.setText(getString(R.string.submit));
            }
        }
    }

    public void displayProgress(int progress) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setProgress(progress + 1);
    }

    public void displayQuestion(Question question) {
        TextView questionView = (TextView) findViewById(R.id.question);
        questionView.setText(question.Text);

        if (question.IsEditable) {
            EditTextFragment newFragment = new EditTextFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (question.correctAnswerCount() > 1) {
            CheckBoxFragment newFragment = new CheckBoxFragment();
            newFragment.setQuestion(question);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            RadioButtonFragment newFragment = new RadioButtonFragment();
            newFragment.setQuestion(question);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void startSummary(View view) {
        this.questions = null;
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", total);
        startActivity(intent);
        finish();
    }

    private List<Question> getQuestions() {
        List<Question> questions = new ArrayList<Question>();

        // Q0
        List<Answer> answers0 = new ArrayList<Answer>();
        answers0.add(new Answer(getString(R.string.answer0_0), true));
        questions.add(new Question(getString(R.string.question0), answers0, true));
        // Q1
        List<Answer> answers1 = new ArrayList<Answer>();
        answers1.add(new Answer(getString(R.string.answer1_0), true));
        questions.add(new Question(getString(R.string.question1), answers1, true));
        // Q2
        List<Answer> answers2 = new ArrayList<Answer>();
        answers2.add(new Answer(getString(R.string.answer2_0), true));
        questions.add(new Question(getString(R.string.question2), answers2, true));
        // Q3
        List<Answer> answers3 = new ArrayList<Answer>();
        answers3.add(new Answer(getString(R.string.answer3_0), true));
        questions.add(new Question(getString(R.string.question3), answers3, true));
        // Q4
        List<Answer> answers4 = new ArrayList<Answer>();
        answers4.add(new Answer(getString(R.string.answer4_0), true));
        questions.add(new Question(getString(R.string.question4), answers4, true));

        // Q5
        List<Answer> answers5 = new ArrayList<Answer>();
        answers5.add(new Answer(getString(R.string.answer5_0), true));
        answers5.add(new Answer(getString(R.string.answer5_1), true));
        answers5.add(new Answer(getString(R.string.answer5_2), true));
        answers5.add(new Answer(getString(R.string.answer5_3), false));
        answers5.add(new Answer(getString(R.string.answer5_4), false));
        answers5.add(new Answer(getString(R.string.answer5_5), false));
        questions.add(new Question(getString(R.string.question5), answers5, false));
        // Q6
        List<Answer> answers6 = new ArrayList<Answer>();
        answers6.add(new Answer(getString(R.string.answer6_0), true));
        answers6.add(new Answer(getString(R.string.answer6_1), true));
        answers6.add(new Answer(getString(R.string.answer6_2), false));
        answers6.add(new Answer(getString(R.string.answer6_3), false));
        answers6.add(new Answer(getString(R.string.answer6_4), false));
        answers6.add(new Answer(getString(R.string.answer6_5), false));
        questions.add(new Question(getString(R.string.question6), answers6, false));
        // Q7
        List<Answer> answers7 = new ArrayList<Answer>();
        answers7.add(new Answer(getString(R.string.answer7_0), true));
        answers7.add(new Answer(getString(R.string.answer7_1), true));
        answers7.add(new Answer(getString(R.string.answer7_2), true));
        answers7.add(new Answer(getString(R.string.answer7_3), false));
        answers7.add(new Answer(getString(R.string.answer7_4), false));
        answers7.add(new Answer(getString(R.string.answer7_5), false));
        questions.add(new Question(getString(R.string.question7), answers7, false));
        // Q8
        List<Answer> answers8 = new ArrayList<Answer>();
        answers8.add(new Answer(getString(R.string.answer8_0), true));
        answers8.add(new Answer(getString(R.string.answer8_1), true));
        answers8.add(new Answer(getString(R.string.answer8_2), true));
        answers8.add(new Answer(getString(R.string.answer8_3), false));
        answers8.add(new Answer(getString(R.string.answer8_4), false));
        answers8.add(new Answer(getString(R.string.answer8_5), false));
        questions.add(new Question(getString(R.string.question8), answers8, false));
        // Q9
        List<Answer> answers9 = new ArrayList<Answer>();
        answers9.add(new Answer(getString(R.string.answer9_0), true));
        answers9.add(new Answer(getString(R.string.answer9_1), true));
        answers9.add(new Answer(getString(R.string.answer9_2), false));
        answers9.add(new Answer(getString(R.string.answer9_3), false));
        answers9.add(new Answer(getString(R.string.answer9_4), false));
        answers9.add(new Answer(getString(R.string.answer9_5), false));
        questions.add(new Question(getString(R.string.question9), answers9, false));
        // Q10
        List<Answer> answers10 = new ArrayList<Answer>();
        answers10.add(new Answer(getString(R.string.answer10_0), true));
        answers10.add(new Answer(getString(R.string.answer10_1), true));
        answers10.add(new Answer(getString(R.string.answer10_2), true));
        answers10.add(new Answer(getString(R.string.answer10_3), true));
        answers10.add(new Answer(getString(R.string.answer10_4), false));
        answers10.add(new Answer(getString(R.string.answer10_5), false));
        questions.add(new Question(getString(R.string.question10), answers10, false));
        // Q11
        List<Answer> answers11 = new ArrayList<Answer>();
        answers11.add(new Answer(getString(R.string.answer11_0), true));
        answers11.add(new Answer(getString(R.string.answer11_1), true));
        answers11.add(new Answer(getString(R.string.answer11_2), true));
        answers11.add(new Answer(getString(R.string.answer11_3), false));
        answers11.add(new Answer(getString(R.string.answer11_4), false));
        answers11.add(new Answer(getString(R.string.answer11_5), false));
        questions.add(new Question(getString(R.string.question11), answers11, false));
        // Q12
        List<Answer> answers12 = new ArrayList<Answer>();
        answers12.add(new Answer(getString(R.string.answer12_0), true));
        answers12.add(new Answer(getString(R.string.answer12_1), true));
        answers12.add(new Answer(getString(R.string.answer12_2), true));
        answers12.add(new Answer(getString(R.string.answer12_3), false));
        answers12.add(new Answer(getString(R.string.answer12_4), false));
        answers12.add(new Answer(getString(R.string.answer12_5), false));
        questions.add(new Question(getString(R.string.question12), answers12, false));
        // Q13
        List<Answer> answers13 = new ArrayList<Answer>();
        answers13.add(new Answer(getString(R.string.answer13_0), true));
        answers13.add(new Answer(getString(R.string.answer13_1), true));
        answers13.add(new Answer(getString(R.string.answer13_2), true));
        answers13.add(new Answer(getString(R.string.answer13_3), true));
        answers13.add(new Answer(getString(R.string.answer13_4), false));
        answers13.add(new Answer(getString(R.string.answer13_5), false));
        questions.add(new Question(getString(R.string.question13), answers13, false));
        // Q14
        List<Answer> answers14 = new ArrayList<Answer>();
        answers14.add(new Answer(getString(R.string.answer14_0), true));
        answers14.add(new Answer(getString(R.string.answer14_1), true));
        answers14.add(new Answer(getString(R.string.answer14_2), true));
        answers14.add(new Answer(getString(R.string.answer14_3), false));
        answers14.add(new Answer(getString(R.string.answer14_4), false));
        answers14.add(new Answer(getString(R.string.answer14_5), false));
        questions.add(new Question(getString(R.string.question14), answers14, false));
        // Q15
        List<Answer> answers15 = new ArrayList<Answer>();
        answers15.add(new Answer(getString(R.string.answer15_0), true));
        answers15.add(new Answer(getString(R.string.answer15_1), false));
        answers15.add(new Answer(getString(R.string.answer15_2), false));
        answers15.add(new Answer(getString(R.string.answer15_3), false));
        questions.add(new Question(getString(R.string.question15), answers15, false));
        // Q16
        List<Answer> answers16 = new ArrayList<Answer>();
        answers16.add(new Answer(getString(R.string.answer16_0), true));
        answers16.add(new Answer(getString(R.string.answer16_1), false));
        answers16.add(new Answer(getString(R.string.answer16_2), false));
        answers16.add(new Answer(getString(R.string.answer16_3), false));
        questions.add(new Question(getString(R.string.question16), answers16, false));
        // Q17
        List<Answer> answers17 = new ArrayList<Answer>();
        answers17.add(new Answer(getString(R.string.answer17_0), true));
        answers17.add(new Answer(getString(R.string.answer17_1), false));
        answers17.add(new Answer(getString(R.string.answer17_2), false));
        answers17.add(new Answer(getString(R.string.answer17_3), false));
        questions.add(new Question(getString(R.string.question17), answers17, false));
        // Q18
        List<Answer> answers18 = new ArrayList<Answer>();
        answers18.add(new Answer(getString(R.string.answer18_0), true));
        answers18.add(new Answer(getString(R.string.answer18_1), false));
        answers18.add(new Answer(getString(R.string.answer18_2), false));
        answers18.add(new Answer(getString(R.string.answer18_3), false));
        questions.add(new Question(getString(R.string.question18), answers18, false));
        // Q19
        List<Answer> answers19 = new ArrayList<Answer>();
        answers19.add(new Answer(getString(R.string.answer19_0), true));
        answers19.add(new Answer(getString(R.string.answer19_1), false));
        answers19.add(new Answer(getString(R.string.answer19_2), false));
        answers19.add(new Answer(getString(R.string.answer19_3), false));
        questions.add(new Question(getString(R.string.question19), answers19, false));
        // Q20
        List<Answer> answers20 = new ArrayList<Answer>();
        answers20.add(new Answer(getString(R.string.answer20_0), true));
        answers20.add(new Answer(getString(R.string.answer20_1), false));
        answers20.add(new Answer(getString(R.string.answer20_2), false));
        answers20.add(new Answer(getString(R.string.answer20_3), false));
        questions.add(new Question(getString(R.string.question20), answers20, false));
        // Q21
        List<Answer> answers21 = new ArrayList<Answer>();
        answers21.add(new Answer(getString(R.string.answer21_0), true));
        answers21.add(new Answer(getString(R.string.answer21_1), false));
        answers21.add(new Answer(getString(R.string.answer21_2), false));
        answers21.add(new Answer(getString(R.string.answer21_3), false));
        questions.add(new Question(getString(R.string.question21), answers21, false));
        // Q22
        List<Answer> answers22 = new ArrayList<Answer>();
        answers22.add(new Answer(getString(R.string.answer22_0), true));
        answers22.add(new Answer(getString(R.string.answer22_1), false));
        answers22.add(new Answer(getString(R.string.answer22_2), false));
        answers22.add(new Answer(getString(R.string.answer22_3), false));
        questions.add(new Question(getString(R.string.question22), answers22, false));
        // Q23
        List<Answer> answers23 = new ArrayList<Answer>();
        answers23.add(new Answer(getString(R.string.answer23_0), true));
        answers23.add(new Answer(getString(R.string.answer23_1), false));
        answers23.add(new Answer(getString(R.string.answer23_2), false));
        answers23.add(new Answer(getString(R.string.answer23_3), false));
        questions.add(new Question(getString(R.string.question23), answers23, false));
        // Q24
        List<Answer> answers24 = new ArrayList<Answer>();
        answers24.add(new Answer(getString(R.string.answer24_0), true));
        answers24.add(new Answer(getString(R.string.answer24_1), false));
        answers24.add(new Answer(getString(R.string.answer24_2), false));
        answers24.add(new Answer(getString(R.string.answer24_3), false));
        questions.add(new Question(getString(R.string.question24), answers24, false));
        // Q25
        List<Answer> answers25 = new ArrayList<Answer>();
        answers25.add(new Answer(getString(R.string.answer25_0), true));
        answers25.add(new Answer(getString(R.string.answer25_1), false));
        answers25.add(new Answer(getString(R.string.answer25_2), false));
        answers25.add(new Answer(getString(R.string.answer25_3), false));
        questions.add(new Question(getString(R.string.question25), answers25, false));
        // Q26
        List<Answer> answers26 = new ArrayList<Answer>();
        answers26.add(new Answer(getString(R.string.answer26_0), true));
        answers26.add(new Answer(getString(R.string.answer26_1), false));
        answers26.add(new Answer(getString(R.string.answer26_2), false));
        answers26.add(new Answer(getString(R.string.answer26_3), false));
        questions.add(new Question(getString(R.string.question26), answers26, false));
        // Q27
        List<Answer> answers27 = new ArrayList<Answer>();
        answers27.add(new Answer(getString(R.string.answer27_0), true));
        answers27.add(new Answer(getString(R.string.answer27_1), false));
        answers27.add(new Answer(getString(R.string.answer27_2), false));
        answers27.add(new Answer(getString(R.string.answer27_3), false));
        questions.add(new Question(getString(R.string.question27), answers27, false));
        // Q28
        List<Answer> answers28 = new ArrayList<Answer>();
        answers28.add(new Answer(getString(R.string.answer28_0), true));
        answers28.add(new Answer(getString(R.string.answer28_1), false));
        answers28.add(new Answer(getString(R.string.answer28_2), false));
        answers28.add(new Answer(getString(R.string.answer28_3), false));
        questions.add(new Question(getString(R.string.question28), answers28, false));
        // Q29
        List<Answer> answers29 = new ArrayList<Answer>();
        answers29.add(new Answer(getString(R.string.answer29_0), true));
        answers29.add(new Answer(getString(R.string.answer29_1), false));
        answers29.add(new Answer(getString(R.string.answer29_2), false));
        answers29.add(new Answer(getString(R.string.answer29_3), false));
        questions.add(new Question(getString(R.string.question29), answers29, false));

        Collections.shuffle(questions);
        questions = questions.subList(0, 10);
        return questions;
    }
}
