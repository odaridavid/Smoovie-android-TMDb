package smoovie.apps.com.kayatech.smoovie.ui.main.callbacks;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.model.Movie;

/**
 * Created By blackcoder
 * On 01/05/19
 **/
public interface MovieListCallBack {

    void inProgress();

    void onFinished(List<Movie> movies, Category category);
}
