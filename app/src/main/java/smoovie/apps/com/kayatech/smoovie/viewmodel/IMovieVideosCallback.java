package smoovie.apps.com.kayatech.smoovie.viewmodel;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.MovieVideos;

public interface IMovieVideosCallback {
    void onSuccess(List<MovieVideos> movieVideos);

    void onError();
}
