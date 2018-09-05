package smoovie.apps.com.kayatech.smoovie.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Entity(tableName = "favourite_movie")
@Parcel(Parcel.Serialization.BEAN)
public class Movie implements IMovie {
    //model class to map json data to pojo
    //Movie GET Class objects from tmdb
    @ColumnInfo(name = "movie_title")
    @SerializedName("original_title")
    private String movieTitle;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String movieOverview;

    @ColumnInfo(name = "release")
    @SerializedName("release_date")
    private String movieReleaseDate;

    @ColumnInfo(name = "poster")
    @SerializedName("poster_path")
    private String moviePoster;

    @ColumnInfo(name = "voter_average")
    @SerializedName("vote_average")
    private  float voterAverage;

    @ColumnInfo(name = "backdrop")
    @SerializedName("backdrop_path")
    private String backdrop;

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int movieId;

    @Ignore
    public Movie() {
    }

    public Movie(int movieId, String movieTitle, String movieOverview, String movieReleaseDate, String moviePoster, String backdrop, float voterAverage) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePoster = moviePoster;
        this.backdrop = backdrop;
        this.voterAverage = voterAverage;
    }

    @Ignore
    public Movie(String movieTitle, String movieOverview, String movieReleaseDate, String moviePoster, String backdrop, float voterAverage) {
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePoster = moviePoster;
        this.backdrop = backdrop;
        this.voterAverage = voterAverage;
    }


    @Override
    public String getMovieTitle() {
        return movieTitle;
    }

    @Override
    public String getMovieOverview() {
        return movieOverview;
    }

    @Override
    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    @Override
    public String getMoviePoster() {
        return moviePoster;
    }

    @Override
    public float getVoterAverage() {
        return voterAverage;
    }

    @Override
    public String getBackdrop() {
        return backdrop;
    }

    @Override
    public int getMovieId() {
        return movieId;
    }

    @Override
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
