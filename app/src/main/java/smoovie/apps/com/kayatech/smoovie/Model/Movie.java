package smoovie.apps.com.kayatech.smoovie.Model;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Movie implements IMovie {
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
