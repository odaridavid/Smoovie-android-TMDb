package smoovie.apps.com.kayatech.smoovie.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import smoovie.apps.com.kayatech.smoovie.db.dao.IFavouriteMovieDao;
import smoovie.apps.com.kayatech.smoovie.model.Movie;

@Database(entities = {Movie.class}, version = 3, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movie_database";
    private static MovieDatabase movieDatabaseInstance;

    public static MovieDatabase getInstance(Context context) {
        if (movieDatabaseInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating DB Instance");
                movieDatabaseInstance = Room
                        .databaseBuilder(context.getApplicationContext(), MovieDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(TAG, "Getting Database Instance");
        return movieDatabaseInstance;
    }

    public abstract IFavouriteMovieDao movieDAO();
}
