package smoovie.apps.com.kayatech.smoovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideoResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private List<MovieVideos> moviesResult;

    public int getId() {
        return id;
    }

    public List<MovieVideos> getMoviesResult() {
        return moviesResult;
    }
}
