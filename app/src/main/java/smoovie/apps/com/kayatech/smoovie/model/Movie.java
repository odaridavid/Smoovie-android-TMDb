package smoovie.apps.com.kayatech.smoovie.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Entity(tableName = "favourite_movie", primaryKeys = {"movie_id"})
@Parcel(Parcel.Serialization.BEAN)
public class Movie implements IMovie {

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

    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    private int movieId;

    @ColumnInfo(name = "favourite")
    private boolean isFavourite;

    @Ignore
    private List<Reviews> mReviews;

    @Ignore
    private List<Trailers> mTrailers;

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

    public List<Reviews> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Reviews> reviews) {
        mReviews = reviews;
    }

    public List<Trailers> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<Trailers> trailers) {
        mTrailers = trailers;
    }

    public boolean isFavourite() {
        return isFavourite;
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

    @Override
    public String getMoviePoster() {
        return moviePoster;
    }

    @Override
    public float getVoterAverage() {
        return voterAverage;
    }

    public String getBackdrop() {
        return backdrop;
    }

    @Override
    public int getMovieId() {
        return movieId;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "movieTitle='" + movieTitle + '\'' +
                ", movieReleaseDate='" + movieReleaseDate + '\'' +
                ", voterAverage=" + voterAverage +
                ", movieId=" + movieId +
                '}';
    }

    @Override
    public boolean equals(Object vo) {
        if (this == vo) return true;
        if (vo == null || getClass() != vo.getClass()) return false;
        Movie vvMovie = (Movie) vo;
        return getMovieId() == vvMovie.getMovieId() &&
                getMovieTitle().equals(vvMovie.getMovieTitle());
    }

    @Override
    public int hashCode() {
        int result = String.valueOf(getMovieId()).hashCode();
        result = 31 * result + getMovieTitle().hashCode();
        return result;
    }
}
