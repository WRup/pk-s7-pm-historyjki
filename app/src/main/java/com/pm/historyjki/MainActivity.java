package com.pm.historyjki;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pm.historyjki.api.adapter.StoryAdapter;
import com.pm.historyjki.api.service.StoryApiService;
import com.pm.historyjki.api.service.StoryDTO;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Consumer;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;

    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;
    private StoryApiService storyApiService;
//    private StoriesUpdateService storiesUpdateService;
    private ArrayList<StoryDTO> stories = new ArrayList<>();
    private ArrayAdapter<StoryDTO> storyAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storyApiService = new StoryApiService(this);


//        Intent serviceIntent = new Intent(this, StoriesUpdateService.class);
//        startService(serviceIntent);
//        bindService(serviceIntent, serviceConnection, Context.BIND_ABOVE_CLIENT);

        init();
    }

    private void init() {
        initNavigationPanel();
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
        storyApiService.getAllStories(new Consumer<List<StoryDTO>>() {
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
        onTabSelectedListener(tabLayout);
    }

    private void initStoriesView() {
        Fragment fragment = StoriesListFragment.newInstance(stories);
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
        /*ListView storiesLv = findViewById(R.id.lv_stories);

        storyAdapter = new StoryAdapter(this, stories);
        storiesLv.setAdapter(storyAdapter);*/
    }

    private void initNavigationPanel(){
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        mDrawer.addDrawerListener(drawerToggle);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
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

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

    }

    private void onTabSelectedListener(final TabLayout tabLayout){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(Objects.requireNonNull(tab.getText()).toString().equals("Stories")){
                    Fragment fragment = StoriesListFragment.newInstance(stories);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
                }
                else if(Objects.requireNonNull(tab.getText()).toString().equals("My stories")){
                    Fragment fragment = FavouriteStoriesListFragment.newInstance(stories);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }




}
