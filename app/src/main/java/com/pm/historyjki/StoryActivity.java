package com.pm.historyjki;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.pm.historyjki.StoryContinuationFragment.OnContinuationSaveListener;
import com.pm.historyjki.StoryDetailsFragment.OnStoryChangeListener;
import com.pm.historyjki.StoryDetailsFragment.OnStoryContinuationOpenListener;
import com.pm.historyjki.api.service.StoryApiService;
import com.pm.historyjki.api.service.StoryContinuation;
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
implements OnStoryChangeListener, OnStoryContinuationOpenListener, OnContinuationSaveListener {

    private StoryDTO actualStory = new StoryDTO();

    private StoryApiService storyApiService;

    private Deque<StoryContinuation> continuationsStack = new ArrayDeque<>();

    boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        storyApiService = new StoryApiService(this);
        initStory();

//        getSupportFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                System.out.println("backStackChange");
////                continuationsStack.pop();
//            }
//        });
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
        if (continuationsStack != null && !continuationsStack.isEmpty()) {
            continuationsStack.pop();
        }
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
                .commit();
    }

    @Override
    public void onStoryContinuationOpen(StoryContinuation continuation) {
        continuationsStack.push(continuation);
        startContinuationFragment(continuation);
    }

    private void startContinuationFragment(StoryContinuation continuation) {
        Fragment fragment = StoryContinuationFragment.newInstance(continuation);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.story_fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onContinuationSaveLister(StoryContinuation continuation) {
        continuationsStack.pop();
        if (continuationsStack.isEmpty()) {
            continuationsStack.push(continuation);
        } else {
            continuationsStack.peek().getContinuations().add(continuation);
        }
        saveContinuationsStack();

        storyApiService.updateStory(actualStory, new Consumer<StoryDTO>() {
            @Override
            public void accept(StoryDTO storyDTO) {

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoryActivity.this, getText(R.string.somethingGoesWrongErrorMsg), Toast.LENGTH_SHORT)
                        .show();
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveContinuationsStack() {
        Iterator<StoryContinuation> it = continuationsStack.iterator();

        StoryContinuation root = null;
        while (it.hasNext()) {
            StoryContinuation child = it.next();
            if (root == null) {
                int rootIndex = findIndexOfContinuation(actualStory.getContent().getContinuations(), child);
                if (rootIndex != -1) {
                    root = actualStory.getContent().getContinuations().get(rootIndex);
                } else {
                    actualStory.getContent().getContinuations().add(child);
                    root = child;
                }
            } else {
                int childIndex = findIndexOfContinuation(root.getContinuations(), child);
                if (childIndex != -1) {
                    root = root.getContinuations().get(childIndex);
                } else {
                    root.getContinuations().add(child);
                    root = child;
                }
            }
        }
    }

    private int findIndexOfContinuation(List<StoryContinuation> continuations, StoryContinuation continuation) {
        for (int i = 0; i < continuations.size(); i++) {
            if(Objects.equals(continuations.get(i).getContent(), continuation.getContent())) {
                return i;
            }
        }
        return -1;
    }
}
