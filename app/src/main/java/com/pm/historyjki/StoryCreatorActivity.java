package com.pm.historyjki;

import com.pm.historyjki.StoryFormFragment.OnSubmitListener;
import com.pm.historyjki.api.service.StoryDTO;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StoryCreatorActivity
        extends AppCompatActivity
        implements OnSubmitListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creator);

    }

    @Override
    public void onSubmit(StoryDTO storyDTO) {
        System.out.println("submit");
    }

    private void startStoryDetailsFragment(StoryDTO storyDTO) {

    }
}