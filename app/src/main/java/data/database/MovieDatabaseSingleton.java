package data.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class MovieDatabaseSingleton {
    private static final String DATABASE_NAME = "movies";

    private static MovieDatabase movieDatabase;


    public static synchronized MovieDatabase getDatabase(Context context) {
        if (movieDatabase == null) {
             movieDatabase = Room
                     .databaseBuilder(context,MovieDatabase.class,DATABASE_NAME)
                     .build();
        }
        return movieDatabase;
    }
}
