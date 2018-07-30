package smoovie.apps.com.kayatech.smoovie.Model;


import com.google.gson.annotations.SerializedName;

public class Movie {
    //model class to map json data to pojo
    @SerializedName("original_title")
    private String MovieTitle;

    @SerializedName("overview")
    private String MovieOverview;

    @SerializedName("release_date")
    private String MovieReleaseDate;

    @SerializedName("poster_path")
    private String MoviePoster;

    @SerializedName("vote_average")
    private float VoterAverage;

    @SerializedName("backdrop_path")
    private String Backdrop;

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

    public void setMovieTitle(String movieTitle) {
        MovieTitle = movieTitle;
    }

    public String getMovieOverview() {
        return MovieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        MovieOverview = movieOverview;
    }

    public String getMovieReleaseDate() {
        return MovieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        MovieReleaseDate = movieReleaseDate;
    }

    public String getMoviePoster() {
        return MoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        MoviePoster = moviePoster;
    }

    public float getVoterAverage() {
        return VoterAverage;
    }

    public void setVoterAverage(float voterAverage) {
        VoterAverage = voterAverage;
    }

    public String getBackdrop() {
        return Backdrop;
    }

    public void setBackdrop(String backdrop) {
        Backdrop = backdrop;
    }
}
