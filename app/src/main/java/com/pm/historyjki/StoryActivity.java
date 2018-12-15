package com.pm.historyjki;

import java.util.ArrayList;
import java.util.Iterator;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.pm.historyjki.api.service.StoryApiCallService;
import com.pm.historyjki.api.service.StoryDTO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StoryActivity extends AppCompatActivity {

    private StoryDTO actualStory = new StoryDTO();
    private StoryApiCallService storyApiCallService;

    private Button likeButton;
    private Button dislikeButton;

    boolean checked = false;

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
                actualStory = storyDTO;
                initStoriesView(actualStory);
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
        likeButton = findViewById(R.id.like);
        dislikeButton = findViewById(R.id.dislike);
        setButtonListener(likeButton,actualStory);
        setButtonListener(dislikeButton,actualStory);

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


    private void setButtonListener(Button button, final StoryDTO storyDTO) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.like:
                        if (!checked) {
                            dislikeButton.setClickable(false);
                            dislikeButton.setTextColor(getResources().getColor(android.R.color.darker_gray));
                            checked = true;
                        } else {
                            dislikeButton.setClickable(true);
                            dislikeButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            checked = false;
                        }
                        likeStory(storyDTO, checked);
                        break;
                    case R.id.dislike:
                        if (!checked) {
                            likeButton.setClickable(false);
                            likeButton.setTextColor(getResources().getColor(android.R.color.darker_gray));
                            checked = true;
                        } else {
                            likeButton.setClickable(true);
                            likeButton.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                            checked = false;
                        }
                        dislikeStory(storyDTO, checked);
                        break;
                }
                updateStory(actualStory);
            }
        });
    }

    private void likeStory(StoryDTO storyDTO, boolean checked) {
        if (checked)
            storyDTO.setLikeNumber(storyDTO.getLikeNumber() + 1);
        else
            storyDTO.setLikeNumber(storyDTO.getLikeNumber() - 1);
    }

    private void dislikeStory(StoryDTO storyDTO, boolean checked) {
        if(checked)
          storyDTO.setDislikeNumber(storyDTO.getDislikeNumber() + 1);
        else
            storyDTO.setDislikeNumber(storyDTO.getDislikeNumber() - 1);


    }

    private void updateStory(final StoryDTO actualStory){
        storyApiCallService.updateStory(actualStory, new Consumer<StoryDTO>() {
                    @Override
                    public void accept(StoryDTO storyDTO) {
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StoryActivity.this, getText(R.string.somethingGoesWrongErrorMsg), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
