package data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

import data.movie.DetailedMovieItem;
import data.movie.MovieItem;

@Database(entities = {MovieItem.class,DetailedMovieItem.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public abstract MovieDao getMovieDao();
}
