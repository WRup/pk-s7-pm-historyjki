package com.pm.historyjki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pm.historyjki.api.adapter.StoryAdapter;
import com.pm.historyjki.api.service.StoryApiService;
import com.pm.historyjki.api.service.StoryDTO;
import com.pm.historyjki.db.model.FavouriteStory;
import com.pm.historyjki.service.FavouriteStoryService;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private StoryApiService storyApiService;
    private FavouriteStoryService favouriteStoryService;
//    private StoriesUpdateService storiesUpdateService;
    private List<StoryDTO> allStories = new ArrayList<>();
    private ArrayAdapter<StoryDTO> storyAdapter;
    private TabLayout tabLayout;

    private Map<String, Predicate<StoryDTO>> storiesFilters = new HashMap<>();

    private List<String> favouritesIds = new ArrayList<>();

    private List<StoryDTO> displayedStories = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storyApiService = new StoryApiService(this);
        favouriteStoryService = new FavouriteStoryService();


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
        initFavourites();
    }

    private void initFavourites() {
        favouritesIds.clear();
        for (FavouriteStory fav : favouriteStoryService.getAll()) {
            favouritesIds.add(fav.getStoryId());
        }
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
        storyApiService.getAllStories(new Consumer<List<StoryDTO>>() {
                                              @Override
                                              public void accept(List<StoryDTO> storyDTOS) {
                                                  allStories.clear();
                                                  allStories.addAll(storyDTOS);
                                                  updateDisplayedStories();
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

    private void updateDisplayedStories() {
        displayedStories.clear();

        for (StoryDTO story : allStories) {

            boolean accepted = true;
            for (Predicate<StoryDTO> predicate : storiesFilters.values()) {
                if (!predicate.test(story)) {
                    accepted = false;
                }
            }
            if (accepted) {
                displayedStories.add(story);
            }
        }
        storyAdapter.notifyDataSetChanged();
    }

    private void initTabs() {
        tabLayout = findViewById(R.id.toolbarfilter);

        TabLayout.Tab storiesTab = tabLayout.newTab();
        storiesTab.setText(R.string.stories);
        storiesTab.setTag("stories");

        TabLayout.Tab myStoriesTab = tabLayout.newTab();
        myStoriesTab.setText(R.string.myStories);
        myStoriesTab.setTag("myStories");

        tabLayout.addTab(storiesTab, true);
        tabLayout.addTab(myStoriesTab);

        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                if (tab.getTag() == "stories") {
                    storiesFilters.remove("myStories");
                } else {
                    storiesFilters.put("myStories", new Predicate<StoryDTO>() {
                        @Override
                        public boolean test(StoryDTO arg) {
                            return favouritesIds.contains(arg.getId());
                        }
                    });
                }
                updateDisplayedStories();
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });

    }

    private void initStoriesView() {
        ListView storiesLv = findViewById(R.id.lv_stories);

        storyAdapter = new StoryAdapter(this, displayedStories);
        storiesLv.setAdapter(storyAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateCurrentStories();
        initFavourites();
    }

    private interface Predicate<T> {
        boolean test(T arg);
    }
}
