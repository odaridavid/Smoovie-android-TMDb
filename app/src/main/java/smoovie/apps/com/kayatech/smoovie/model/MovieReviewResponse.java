package smoovie.apps.com.kayatech.smoovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewResponse {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private List<MovieReviews> moviesResult;

    MovieReviewResponse() {
    }

    public int getPage() {
        return page;
    }

    public List<MovieReviews> getMoviesResult() {
        return moviesResult;
    }
}
