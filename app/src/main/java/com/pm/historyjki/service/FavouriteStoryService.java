package com.pm.historyjki.service;

import java.util.Collections;
import java.util.List;

import com.pm.historyjki.App;
import com.pm.historyjki.api.service.StoryDTO;
import com.pm.historyjki.db.dao.FavouriteStoryDao;
import com.pm.historyjki.db.model.FavouriteStory;

public class FavouriteStoryService implements IFavouriteStoryService {

    private FavouriteStoryDao dao;

    public FavouriteStoryService() {
        dao = App.get().getDb().favouriteStoryDao();
    }

    @Override
    public List<FavouriteStory> getAll() {
        return dao.getAll();
    }

    @Override
    public FavouriteStory findByStoryId(String storyId) {
        return dao.findByStoryId(storyId);
    }

    @Override
    public void insertAll(List<FavouriteStory> favouriteStories) {
        dao.insertAll(favouriteStories);
    }

    @Override
    public void update(FavouriteStory favouriteStory) {
        dao.update(favouriteStory);
    }

    @Override
    public void delete(FavouriteStory favouriteStory) {
        dao.delete(favouriteStory);
    }

    @Override
    public boolean isFavourite(String storyId) {
        return findByStoryId(storyId) != null;
    }

    @Override
    public void addStoryToFavourite(StoryDTO storyDTO) {
        FavouriteStory favouriteStory = new FavouriteStory();
        favouriteStory.setStoryId(storyDTO.getId());
        insertAll(Collections.singletonList(favouriteStory));
    }

    @Override
    public void removeStoryFromFavourite(StoryDTO storyDTO) {
        dao.delete(storyDTO.getId());
    }
}
