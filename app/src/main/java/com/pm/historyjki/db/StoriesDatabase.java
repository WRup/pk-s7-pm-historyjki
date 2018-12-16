package com.pm.historyjki.db;

import com.pm.historyjki.db.dao.FavouriteStoryDao;
import com.pm.historyjki.db.model.FavouriteStory;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {FavouriteStory.class}, version = 1, exportSchema = false)
public abstract class StoriesDatabase extends RoomDatabase {

    public abstract FavouriteStoryDao favouriteStoryDao();
}
