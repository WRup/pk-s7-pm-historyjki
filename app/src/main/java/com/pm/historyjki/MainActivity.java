package com.pm.historyjki;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pm.historyjki.api.adapter.StoryAdapter;
import com.pm.historyjki.api.boundService.MyBoundService;
import com.pm.historyjki.api.service.StoryApiCallService;
import com.pm.historyjki.api.service.StoryDTO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StoryApiCallService storyApiCallService;
    private MyBoundService myBoundService;
    private List<Integer> likeArray = new ArrayList<>();
    private List<Integer> dislikeArray = new ArrayList<>();
    private List<StoryDTO> stories;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, MyBoundService.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, Context.BIND_ABOVE_CLIENT);

        storyApiCallService = new StoryApiCallService(this);
        initStories();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stories = getAllStories();
        initStoriesView();
    }

    private void initStories() {
        tabLayout = findViewById(R.id.toolbarfilter);
        tabLayout.addTab(tabLayout.newTab().setText("STORIES"));
        tabLayout.addTab(tabLayout.newTab().setText("MY STORIES"));
        storyApiCallService.getAllStories(new Consumer<List<StoryDTO>>() {
                                              @Override
                                              public void accept(List<StoryDTO> storyDTOS) {
                                                  stories = new ArrayList<>(storyDTOS);
                                                  createLists(stories);
                                                  initStoriesView();
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

    private void initStoriesView() {
        ListView storiesLv = findViewById(R.id.lv_stories);
        setListItemListener(storiesLv);

        StoryAdapter storyAdapter = new StoryAdapter(this, stories, likeArray, dislikeArray);
        storiesLv.setAdapter(storyAdapter);
    }

    private void setListItemListener(ListView storiesLv) {

        storiesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("Position =" + i);
                StoryDTO storyDTO = (StoryDTO) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, StoryActivity.class);
                intent.putExtra("storyId", storyDTO.getId());
                startActivity(intent);

            }
        });
    }

    private void createLists(List<StoryDTO> stories) {

        for (StoryDTO storyDTO : stories) {
            likeArray.add(storyDTO.getLikeNumber());
            dislikeArray.add(storyDTO.getDislikeNumber());
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBoundService.MyBinder binder = (MyBoundService.MyBinder) iBinder;
            myBoundService = binder.getMyService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myBoundService = null;
        }
    };

    private List<StoryDTO> getAllStories() {
        if (myBoundService != null) {
            return myBoundService.getAllStories();
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                initStories();
            }
        }
    }
}
