package smoovie.apps.com.kayatech.smoovie.Model;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Movie {
    //model class to map json data to pojo
    //Movie GET Class objects from tmdb
    @SerializedName("original_title")
    String MovieTitle;

    @SerializedName("overview")
    String MovieOverview;

    @SerializedName("release_date")
    String MovieReleaseDate;

    @SerializedName("poster_path")
    String MoviePoster;

    @SerializedName("vote_average")
    float VoterAverage;

    @SerializedName("backdrop_path")
    String Backdrop;

    @SerializedName("id")
    int MovieId;

    public Movie() {
    }

    Movie(String movieTitle, String movieOverview, String movieReleaseDate, String moviePoster, float voterAverage) {
        MovieTitle = movieTitle;
        MovieOverview = movieOverview;
        MovieReleaseDate = movieReleaseDate;
        MoviePoster = moviePoster;
        VoterAverage = voterAverage;
    }


    public String getMovieTitle() {
        return MovieTitle;
    }


    public String getMovieOverview() {
        return MovieOverview;
    }


    public String getMovieReleaseDate() {
        return MovieReleaseDate;
    }


    public String getMoviePoster() {
        return MoviePoster;
    }


    public float getVoterAverage() {
        return VoterAverage;
    }


    public String getBackdrop() {
        return Backdrop;
    }


    public int getMovieId() {
        return MovieId;
    }

}
