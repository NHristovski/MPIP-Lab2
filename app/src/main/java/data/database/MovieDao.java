package data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import data.movie.DetailedMovieItem;

@Dao
public interface MovieDao {
    @Insert
    void insert(DetailedMovieItem detailedMovieItem);

    @Insert
    void insertAll(DetailedMovieItem detailedMovieItem);


    @Query("SELECT * FROM detailed_movie WHERE imdbId LIKE :imdbId")
    DetailedMovieItem findById(String imdbId);

    @Delete
    void delete(DetailedMovieItem detailedMovieItem);

    @Update
    void update(DetailedMovieItem detailedMovieItem);

}
