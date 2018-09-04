package smoovie.apps.com.kayatech.smoovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Movie;

public class MovieListResponse {

    //@expose serialises as variable name
    //@serialized name uses name params key
    //Retrieves movies page by page ,JSON Response TMDB

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private List<Movie> moviesResult;

    //Getters
    public int getPage() {
        return page;
    }


    public List<Movie> getMoviesResult() {
        return moviesResult;
    }


}
