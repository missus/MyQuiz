package com.example.android.myquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditTextFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.answer_edittext_fragment, container, false);
        return view;
    }

    public String getAnswer() {
        EditText et = (EditText) view.findViewById(R.id.editText);
        et.setClickable(false);
        return et.getText().toString();
    }
}
