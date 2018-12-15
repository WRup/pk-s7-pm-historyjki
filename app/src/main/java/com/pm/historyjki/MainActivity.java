package com.pm.historyjki;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pm.historyjki.api.adapter.StoryAdapter;
import com.pm.historyjki.api.service.StoryApiCallService;
import com.pm.historyjki.api.service.StoryDTO;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private StoryApiCallService storyApiCallService;
//    private StoriesUpdateService storiesUpdateService;
    private List<StoryDTO> stories = new ArrayList<>();
    private ArrayAdapter<StoryDTO> storyAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storyApiCallService = new StoryApiCallService(this);


//        Intent serviceIntent = new Intent(this, StoriesUpdateService.class);
//        startService(serviceIntent);
//        bindService(serviceIntent, serviceConnection, Context.BIND_ABOVE_CLIENT);

        init();
    }

    private void init() {
        initTabs();
        initStoriesView();
        updateCurrentStories();
        initCreateStoryBtn();
    }

    private void initCreateStoryBtn() {
        FloatingActionButton btn = findViewById(R.id.btn_create_story);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StoryCreatorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateCurrentStories() {
        storyApiCallService.getAllStories(new Consumer<List<StoryDTO>>() {
                                              @Override
                                              public void accept(List<StoryDTO> storyDTOS) {
                                                  stories.clear();
                                                  stories.addAll(storyDTOS);
                                                  storyAdapter.notifyDataSetChanged();
                                              }
                                          }, new Response.ErrorListener() {
                                              @Override
                                              public void onErrorResponse(VolleyError error) {
                                                  Toast.makeText(MainActivity.this, getText(R.string.somethingGoesWrongErrorMsg), Toast.LENGTH_SHORT)
                                                          .show();
                                              }
                                          }
        );
    }

    private void initTabs() {
        tabLayout = findViewById(R.id.toolbarfilter);
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.stories)));
        tabLayout.addTab(tabLayout.newTab().setText(getText(R.string.myStories)));
    }

    private void initStoriesView() {
        ListView storiesLv = findViewById(R.id.lv_stories);

        storyAdapter = new StoryAdapter(this, stories);
        storiesLv.setAdapter(storyAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateCurrentStories();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                updateCurrentStories();
            }
        }
    }


}
