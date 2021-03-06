package com.pm.historyjki;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.pm.historyjki.StoryDetailsFragment.OnStoryDetailsSaveListener;
import com.pm.historyjki.StoryFormFragment.OnNewStorySubmitListener;
import com.pm.historyjki.api.service.ConfigApiService;
import com.pm.historyjki.api.service.Configuration;
import com.pm.historyjki.api.service.StoryApiService;
import com.pm.historyjki.api.service.StoryDTO;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class StoryCreatorActivity
        extends AppCompatActivity
        implements OnNewStorySubmitListener, OnStoryDetailsSaveListener {

    private StoryApiService storyApiService;

    private ConfigApiService configApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creator);
        initApiServices();
        startStoryFormFragment();
    }

    private void initApiServices() {
        storyApiService = new StoryApiService(this);
        configApiService = new ConfigApiService(this);
    }

    @Override
    public void onSubmit(StoryDTO storyDTO) {
        startStoryDetailsFragment(storyDTO);
    }

    private void startStoryFormFragment() {
        final StoryFormFragment fragment = StoryFormFragment.newInstance(new StoryDTO());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.story_creator_fragment_container, fragment, StoryFormFragment.TAG)
                .commit();

        configApiService.getConfiguration(new Consumer<Configuration>() {
            @Override
            public void accept(Configuration configuration) {
                fragment.onConfigurationChange(configuration);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoryCreatorActivity.this, getText(R.string.somethingGoesWrongErrorMsg), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void startStoryDetailsFragment(StoryDTO storyDTO) {
        Fragment fragment = StoryDetailsFragment.newInstance(storyDTO, false);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.story_creator_fragment_container, fragment, StoryDetailsFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSave(StoryDTO storyDTO) {
        storyApiService.addStory(storyDTO, new Consumer<StoryDTO>() {
            @Override
            public void accept(StoryDTO storyDTO) {
                Intent intent = new Intent(StoryCreatorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoryCreatorActivity.this, R.string.somethingGoesWrongErrorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}