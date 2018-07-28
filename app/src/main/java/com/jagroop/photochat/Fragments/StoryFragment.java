package com.jagroop.photochat.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jagroop.photochat.R;

public class StoryFragment extends Fragment {

    public static StoryFragment newInstance() {
        StoryFragment fragment = new StoryFragment();
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        return view;
    }
}
