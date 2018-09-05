package smoovie.apps.com.kayatech.smoovie.model;

interface IMovie {
    String getMovieTitle();


    String getMovieOverview();


    String getMovieReleaseDate();


    String getMoviePoster();


    float getVoterAverage();


    String getBackdrop();

    int getMovieId();
    void setMovieId(int movieId);
}
