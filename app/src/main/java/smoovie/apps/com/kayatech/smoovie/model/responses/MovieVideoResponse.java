package smoovie.apps.com.kayatech.smoovie.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Trailers;

public class MovieVideoResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private List<Trailers> moviesResult;

    public int getId() {
        return id;
    }

    public List<Trailers> getMoviesResult() {
        return moviesResult;
    }
}
