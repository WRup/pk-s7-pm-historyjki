package com.pm.historyjki;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pm.historyjki.api.adapter.StoryAdapter;
import com.pm.historyjki.api.service.StoryDTO;

import java.util.ArrayList;

public class StoriesListFragment extends Fragment {

    private ArrayList<StoryDTO> stories;
    private StoryAdapter storyAdapter;

    public static StoriesListFragment newInstance(ArrayList<StoryDTO> stories) {
        StoriesListFragment fragment = new StoriesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("stories", stories);
        fragment.setArguments(args);
        return fragment;
    }
    public StoriesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stories = getArguments().getParcelableArrayList("stories");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stories_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){

        ListView storylist = findViewById(R.id.storylist);
            storyAdapter = new StoryAdapter(this.getActivity(), stories);

        storylist.setAdapter(storyAdapter);
    }

    private <T extends View> T findViewById(@IdRes int id) {
        if (getView() == null) {
            throw new NullPointerException();
        } else {
            return getView().findViewById(id);
        }
    }
}
