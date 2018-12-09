package com.pm.historyjki;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.pm.historyjki.api.service.StoryApiCallService;
import com.pm.historyjki.api.service.StoryDTO;

import android.os.Bundle;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private StoryApiCallService storyApiCallService;

    private List<StoryDTO> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storyApiCallService = new StoryApiCallService(this);

        initStories();
    }

    private void initStories() {
        storyApiCallService.getAllStories(new Consumer<List<StoryDTO>>() {
            @Override
            public void accept(List<StoryDTO> storyDTOS) {
                stories = new ArrayList<>(storyDTOS);
                initStoriesView();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, getText(R.string.somethingGoesWrongErrorMsg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initStoriesView() {
        ListView storiesLv = findViewById(R.id.lv_stories);
        ArrayAdapter<StoryDTO> adapter = new ArrayAdapter<>(this, R.layout.lv_item, R.id.tv_lv_item);
        adapter.addAll(stories);
        storiesLv.setAdapter(adapter);
    }
}
