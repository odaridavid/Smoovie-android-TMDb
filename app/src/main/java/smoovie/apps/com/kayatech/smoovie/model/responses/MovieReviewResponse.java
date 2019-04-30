package smoovie.apps.com.kayatech.smoovie.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Reviews;

public class MovieReviewResponse {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("results")
    @Expose
    private List<Reviews> mReviews;


    public int getPage() {
        return page;
    }

    public List<Reviews> getMoviesReviews() {
        return mReviews;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
