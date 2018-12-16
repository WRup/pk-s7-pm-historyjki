package com.pm.historyjki.service;

import java.util.List;

import com.pm.historyjki.api.service.StoryDTO;
import com.pm.historyjki.db.model.FavouriteStory;

public interface IFavouriteStoryService {

    List<FavouriteStory> getAll();

    FavouriteStory findByStoryId(String storyId);

    void insertAll(List<FavouriteStory> favouriteStories);

    void update(FavouriteStory favouriteStory);

    void delete(FavouriteStory favouriteStory);

    boolean isFavourite(String storyId);

    void addStoryToFavourite(StoryDTO storyDTO);

    void removeStoryFromFavourite(StoryDTO storyDTO);
}
