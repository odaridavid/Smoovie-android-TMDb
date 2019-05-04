package smoovie.apps.com.kayatech.smoovie.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Movie;

@Dao
public interface IFavouriteMovieDao {

    @Query("SELECT * FROM favourite_movie")
    LiveData<List<Movie>> loadAll();

    @Insert
    void addToFavourites(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void removeFromFavourites(Movie movie);

    @Query("SELECT * FROM favourite_movie WHERE movie_id = :id")
    LiveData<Movie> loadById(int id);

}
