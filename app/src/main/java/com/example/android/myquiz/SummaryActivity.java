package com.example.android.myquiz;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        displaySummary(score, total);
    }

    public void displaySummary(int score, int total) {
        TextView summaryView = (TextView) findViewById(R.id.summary);
        boolean top = score > (total * 2 / 3);
        boolean good = score > (total / 3);
        String summary = "";
        if (top) {
            summary += getString(R.string.top) + "\n";
        } else if (good) {
            summary += getString(R.string.good) + "\n";
        } else {
            summary += getString(R.string.notgood) + "\n";
        }
        summary += getString(R.string.scored, score, total);
        summary += (good) ? "\n" + getString(R.string.well) : "";
        summaryView.setText(summary);
        ProgressBar summaryProgress = (ProgressBar) findViewById(R.id.summary_progress);
        summaryProgress.setProgress(score);
        summaryProgress.setMax(total);
        if (!top && good) {
            summaryProgress.getProgressDrawable().setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorSummaryGood, null), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (!good) {
            summaryProgress.getProgressDrawable().setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorSummaryNotGood, null), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    public void resetQuiz(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
        finish();
    }
}
