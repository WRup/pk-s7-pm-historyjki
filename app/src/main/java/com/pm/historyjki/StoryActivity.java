package com.pm.historyjki;

import android.os.Bundle;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.pm.historyjki.api.service.StoryApiCallService;
import com.pm.historyjki.api.service.StoryDTO;

import java.util.ArrayList;
import java.util.Iterator;

public class StoryActivity extends AppCompatActivity {

    private StoryApiCallService storyApiCallService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyApiCallService = new StoryApiCallService(this);

        initStory();
    }

    private void initStory() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String storyId = extras.getString("storyId");
        storyApiCallService.getStory(storyId, new Consumer<StoryDTO>() {
            @Override
            public void accept(StoryDTO storyDTO) {
                initStoriesView(storyDTO);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoryActivity.this, getText(R.string.somethingGoesWrongErrorMsg), Toast.LENGTH_SHORT)
                     .show();
            }
        });
    }

    private void initStoriesView(StoryDTO storyDTO) {
        TextView storiesTv = findViewById(R.id.tv_story);
        storiesTv.setText(storyDTO.getContent().getContent());
        ArrayList<TextView> continuationsTv = new ArrayList<>();

        continuationsTv.add((TextView) findViewById(R.id.tv_continuation_1));
        continuationsTv.add((TextView) findViewById(R.id.tv_continuation_2));
        continuationsTv.add((TextView) findViewById(R.id.tv_continuation_3));

        Iterator<TextView> it1 = continuationsTv.iterator();
        Iterator<String> it2 = storyDTO.getContent().getContinuations().iterator();

        while (it1.hasNext() && it2.hasNext()) {
            it1.next().setText(it2.next());
        }
    }
}
