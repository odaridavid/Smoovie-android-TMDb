package smoovie.apps.com.kayatech.smoovie.Model;


import com.google.gson.annotations.SerializedName;

public class Movie {
    //model class to map json data to pojo
    //Movie GET Class objects from tmdb
    @SerializedName("original_title")
    private String MovieTitle;

    @SerializedName("overview")
    private String MovieOverview;

    @SerializedName("release_date")
    private String MovieReleaseDate;

    @SerializedName("poster_path")
    private String MoviePoster;

    @SerializedName("vote_average")
    private  float  VoterAverage;

    @SerializedName("backdrop_path")
    private String Backdrop;

    @SerializedName("id")
    private int MovieId;

    Movie() {
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
