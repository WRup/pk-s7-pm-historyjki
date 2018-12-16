package com.pm.historyjki;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.pm.historyjki.StoryDetailsFragment.OnStoryChangeListener;
import com.pm.historyjki.api.service.StoryApiService;
import com.pm.historyjki.api.service.StoryDTO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class StoryActivity extends AppCompatActivity
implements OnStoryChangeListener {

    private StoryDTO actualStory = new StoryDTO();
    private StoryApiService storyApiService;

    boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        storyApiService = new StoryApiService(this);
        initStory();
    }

    private void initStory() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String storyId = extras.getString("storyId");
        storyApiService.getStory(storyId, new Consumer<StoryDTO>() {
            @Override
            public void accept(StoryDTO storyDTO) {
                actualStory = storyDTO;
                startStoryDetailsFragment(actualStory);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoryActivity.this, getText(R.string.somethingGoesWrongErrorMsg), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void updateStory(final StoryDTO actualStory){
        storyApiService.updateStory(actualStory, new Consumer<StoryDTO>() {
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

    @Override
    public void onChange(StoryDTO storyDTO) {
        updateStory(storyDTO);
    }

    private void startStoryDetailsFragment(StoryDTO storyDTO) {
        Fragment fragment = StoryDetailsFragment.newInstance(storyDTO, true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.story_fragment_container, fragment, StoryDetailsFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }
}
