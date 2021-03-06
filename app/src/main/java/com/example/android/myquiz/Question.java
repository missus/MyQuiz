/*
 * Created by Karolin Fornet.
 * Copyright (c) 2017.  All rights reserved.
 */

package com.example.android.myquiz;

import java.util.List;

public class Question {
    public String Text;
    public List<Answer> Answers;
    public boolean IsEditable;

    public Question(String text, List<Answer> answers, boolean isEditable) {
        Text = text;
        Answers = answers;
        IsEditable = isEditable;
    }

    public int correctAnswerCount() {
        int count = 0;
        for (Answer answer : Answers) {
            if (answer.IsCorrect) {
                count++;
            }
        }
        return count;
    }

    public boolean isCorrect(String s) {
        boolean ret = false;
        for (Answer answer : Answers) {
            if (answer.IsCorrect && s.trim().equalsIgnoreCase(answer.Text.trim())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public String correctAnswerToast() {
        String toast = " ";
        for (Answer answer : Answers) {
            if (answer.IsCorrect) {
                toast += (toast.equals(" ") ? answer.Text : ", " + answer.Text);
            }
        }
        return toast;
    }
}