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
import com.pm.historyjki.service.FavouriteStoryService;

import java.util.ArrayList;

public class FavouriteStoriesListFragment extends Fragment {

    private ArrayList<StoryDTO> stories;
    private ArrayList<StoryDTO> favouritesStories;
    private StoryAdapter storyAdapter;
    private FavouriteStoryService favouriteStoryService;

    public static FavouriteStoriesListFragment newInstance(ArrayList<StoryDTO> stories) {
        FavouriteStoriesListFragment fragment = new FavouriteStoriesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("stories", stories);
        fragment.setArguments(args);
        return fragment;
    }

    public FavouriteStoriesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stories = getArguments().getParcelableArrayList("stories");
            favouritesStories = new ArrayList<>();
            favouriteStoryService = new FavouriteStoryService();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFavourites();
        initView();
    }

    private void initView(){
        ListView storylist = findViewById(R.id.storylist);

        storyAdapter = new StoryAdapter(getActivity(), favouritesStories);
        storylist.setAdapter(storyAdapter);
    }



    private void getFavourites(){
        for(StoryDTO storyList: stories){
            if(favouriteStoryService.isFavourite(storyList.getId()))
                favouritesStories.add(storyList);
        }
        storyAdapter.notifyDataSetChanged();

    }

    private <T extends View> T findViewById(@IdRes int id) {
        if (getView() == null) {
            throw new NullPointerException();
        } else {
            return getView().findViewById(id);
        }
    }
}
