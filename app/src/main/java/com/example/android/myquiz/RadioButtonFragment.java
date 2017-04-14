package com.example.android.myquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.Collections;

public class RadioButtonFragment extends Fragment {

    private View view;
    private static Question question;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.answer_radiobutton_fragment, container, false);
        this.displayAnswers(this.question);
        return view;
    }

    public void setQuestion(Question q) {
        if (this.question == null || !this.question.Text.equalsIgnoreCase(q.Text)) {
            this.question = q;
            Collections.shuffle(this.question.Answers);
        }
    }

    private void displayAnswers(Question question) {
        if (question != null && view != null) {
            RadioButton rb0 = (RadioButton) view.findViewById(R.id.radioButton0);
            rb0.setText(question.Answers.get(0).Text);
            RadioButton rb1 = (RadioButton) view.findViewById(R.id.radioButton1);
            rb1.setText(question.Answers.get(1).Text);
            RadioButton rb2 = (RadioButton) view.findViewById(R.id.radioButton2);
            rb2.setText(question.Answers.get(2).Text);
            RadioButton rb3 = (RadioButton) view.findViewById(R.id.radioButton3);
            rb3.setText(question.Answers.get(3).Text);
        }
    }

    public String getSelectedAnswer() {
        String ret = "";
        RadioButton rb0 = (RadioButton) view.findViewById(R.id.radioButton0);
        if (rb0.isChecked()) {
            ret = rb0.getText().toString();
        }
        RadioButton rb1 = (RadioButton) view.findViewById(R.id.radioButton1);
        if (rb1.isChecked()) {
            ret = rb1.getText().toString();
        }
        RadioButton rb2 = (RadioButton) view.findViewById(R.id.radioButton2);
        if (rb2.isChecked()) {
            ret = rb2.getText().toString();
        }
        RadioButton rb3 = (RadioButton) view.findViewById(R.id.radioButton3);
        if (rb3.isChecked()) {
            ret = rb3.getText().toString();
        }
        rb0.setClickable(false);
        rb1.setClickable(false);
        rb2.setClickable(false);
        rb3.setClickable(false);
        return ret;
    }
}
