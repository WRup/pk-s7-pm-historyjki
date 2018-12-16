package com.pm.historyjki.db.dao;

import java.util.List;

import com.pm.historyjki.db.model.FavouriteStory;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface FavouriteStoryDao {

    @Query("SELECT * FROM FavouriteStory")
    List<FavouriteStory> getAll();

    @Query("SELECT * FROM FavouriteStory s WHERE s.storyId LIKE :storyId LIMIT 1")
    FavouriteStory findByStoryId(String storyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FavouriteStory> favouriteStories);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(FavouriteStory favouriteStory);

    @Delete
    void delete(FavouriteStory favouriteStory);

    @Query("DELETE FROM FavouriteStory WHERE storyId LIKE :storyId")
    void delete(String storyId);
}
