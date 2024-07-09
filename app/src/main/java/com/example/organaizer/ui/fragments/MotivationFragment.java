package com.example.organaizer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import java.util.Random;
import com.example.organaizer.R;

public class MotivationFragment extends Fragment {
    private TextView motivationTextView;
    private String[] motivationStories;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motivation, container, false);
        motivationTextView = view.findViewById(R.id.motivation_text);
        motivationStories = getResources().getStringArray(R.array.motivationsstory_array);
        motivationTextView.setText(displayRandomMotivationStory());

        return view;
    }

    private CharSequence displayRandomMotivationStory() {
        Random random = new Random();
        return motivationStories[random.nextInt(motivationStories.length)];
    }

}