/*
 * Created by Karolin Fornet.
 * Copyright (c) 2017.  All rights reserved.
 */

package com.example.android.myquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckBoxFragment extends Fragment {

    private View view;
    private static Question question;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.answer_checkbox_fragment, container, false);
        this.displayAnswers();
        return view;
    }

    public void setQuestion(Question q) {
        if (question == null || !question.Text.equalsIgnoreCase(q.Text)) {
            question = q;
            Collections.shuffle(question.Answers);
        }
    }

    private void displayAnswers() {
        if (question != null && view != null) {
            CheckBox cb0 = view.findViewById(R.id.checkBox0);
            cb0.setText(question.Answers.get(0).Text);
            cb0.setChecked(false);
            CheckBox cb1 = view.findViewById(R.id.checkBox1);
            cb1.setText(question.Answers.get(1).Text);
            cb1.setChecked(false);
            CheckBox cb2 = view.findViewById(R.id.checkBox2);
            cb2.setText(question.Answers.get(2).Text);
            cb2.setChecked(false);
            CheckBox cb3 = view.findViewById(R.id.checkBox3);
            cb3.setText(question.Answers.get(3).Text);
            cb3.setChecked(false);
            CheckBox cb4 = view.findViewById(R.id.checkBox4);
            cb4.setText(question.Answers.get(4).Text);
            cb4.setChecked(false);
            CheckBox cb5 = view.findViewById(R.id.checkBox5);
            cb5.setText(question.Answers.get(5).Text);
            cb5.setChecked(false);
        }
    }

    public List<String> getSelectedAnswers() {
        List<String> selectedAnswers = new ArrayList<>();
        CheckBox cb0 = view.findViewById(R.id.checkBox0);
        if (cb0.isChecked()) {
            selectedAnswers.add(cb0.getText().toString());
        }
        CheckBox cb1 = view.findViewById(R.id.checkBox1);
        if (cb1.isChecked()) {
            selectedAnswers.add(cb1.getText().toString());
        }
        CheckBox cb2 = view.findViewById(R.id.checkBox2);
        if (cb2.isChecked()) {
            selectedAnswers.add(cb2.getText().toString());
        }
        CheckBox cb3 = view.findViewById(R.id.checkBox3);
        if (cb3.isChecked()) {
            selectedAnswers.add(cb3.getText().toString());
        }
        CheckBox cb4 = view.findViewById(R.id.checkBox4);
        if (cb4.isChecked()) {
            selectedAnswers.add(cb4.getText().toString());
        }
        CheckBox cb5 = view.findViewById(R.id.checkBox5);
        if (cb5.isChecked()) {
            selectedAnswers.add(cb5.getText().toString());
        }
        cb0.setClickable(false);
        cb1.setClickable(false);
        cb2.setClickable(false);
        cb3.setClickable(false);
        cb4.setClickable(false);
        cb5.setClickable(false);
        return selectedAnswers;
    }
}
