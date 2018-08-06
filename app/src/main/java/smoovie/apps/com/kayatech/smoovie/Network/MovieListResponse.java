package smoovie.apps.com.kayatech.smoovie.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;

public class MovieListResponse {

    //@expose serialises as variable name
    //@serialized name uses name params key
    //Retrieves movies page by page ,JSON Response TMDB

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total_results")
    @Expose
    private int totalResults;

    @SerializedName("results")
    @Expose
    private List<Movie> moviesResult;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    //Getters
    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }


    public List<Movie> getMoviesResult() {
        return moviesResult;
    }


    public int getTotalPages() {
        return totalPages;
    }

}
