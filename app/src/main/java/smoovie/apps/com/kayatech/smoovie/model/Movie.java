package smoovie.apps.com.kayatech.smoovie.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Entity(tableName = "favourite_movie")
@Parcel(Parcel.Serialization.BEAN)
public class Movie {
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
    private float voterAverage;

    @ColumnInfo(name = "backdrop")
    @SerializedName("backdrop_path")
    private String backdrop;

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int movieId;


    @ColumnInfo(name = "favourite")
    private boolean isFavourite;

    @Ignore
    public Movie() {
    }

    public Movie(int movieId, String movieTitle, String movieOverview, String movieReleaseDate, String moviePoster, String backdrop, float voterAverage, boolean isFavourite) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePoster = moviePoster;
        this.backdrop = backdrop;
        this.voterAverage = voterAverage;
        this.isFavourite = isFavourite;
    }

    @Ignore
    public Movie(String movieTitle, String movieOverview, String movieReleaseDate, String moviePoster, String backdrop, float voterAverage, boolean isFavourite) {
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePoster = moviePoster;
        this.backdrop = backdrop;
        this.voterAverage = voterAverage;
        this.isFavourite = isFavourite;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void isFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public float getVoterAverage() {
        return voterAverage;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
