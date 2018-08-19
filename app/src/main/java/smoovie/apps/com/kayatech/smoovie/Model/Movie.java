package smoovie.apps.com.kayatech.smoovie.Model;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel(Parcel.Serialization.BEAN)
public class Movie implements IMovie {
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
    private  float VoterAverage;

    @SerializedName("backdrop_path")
    private String Backdrop;

    @SerializedName("id")
    private int MovieId;

    public Movie() {
    }


    @Override
    public String getMovieTitle() {
        return MovieTitle;
    }

    @Override
    public String getMovieOverview() {
        return MovieOverview;
    }

    @Override
    public String getMovieReleaseDate() {
        return MovieReleaseDate;
    }

    @Override
    public String getMoviePoster() {
        return MoviePoster;
    }

    @Override
    public float getVoterAverage() {
        return VoterAverage;
    }

    @Override
    public String getBackdrop() {
        return Backdrop;
    }

    @Override
    public int getMovieId() {
        return MovieId;
    }
}
