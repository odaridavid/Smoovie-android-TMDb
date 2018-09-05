package smoovie.apps.com.kayatech.smoovie.room_database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import smoovie.apps.com.kayatech.smoovie.model.Movie;

@Database(entities = {Movie.class},version = 2,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movieList";
    private static MovieDatabase movieDatabaseInstance;

    public static MovieDatabase getMovieDatabaseInstance(Context context) {
        if (movieDatabaseInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating DB Instance");
                movieDatabaseInstance = Room
                        .databaseBuilder(context.getApplicationContext(), MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration().build();
            }
        }
        Log.d(TAG,"Getting Database Instance");
        return movieDatabaseInstance;
    }

    public abstract IMovieDAO movieDAO();
}
