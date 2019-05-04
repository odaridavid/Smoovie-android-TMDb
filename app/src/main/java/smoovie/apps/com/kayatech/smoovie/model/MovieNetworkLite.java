package smoovie.apps.com.kayatech.smoovie.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Objects;

/**
 * Created By blackcoder
 * On 02/05/19
 **/
@Parcel(Parcel.Serialization.BEAN)
public final class MovieNetworkLite implements IMovie {

    @SerializedName("vote_average")
    private float voterAverage;

    @SerializedName("poster_path")
    private String moviePoster;

    @SerializedName("id")
    private int movieId;

    MovieNetworkLite() {
    }

    public MovieNetworkLite(float voterAverage, String moviePoster, int movieId) {
        this.voterAverage = voterAverage;
        this.moviePoster = moviePoster;
        this.movieId = movieId;
    }

    @Override
    public float getVoterAverage() {
        return voterAverage;
    }

    @Override
    public String getMoviePoster() {
        return moviePoster;
    }

    @Override
    public int getMovieId() {
        return movieId;
    }

    @Override
    public String toString() {
        return "MovieNetworkLite{" +
                "voterAverage=" + voterAverage +
                ", moviePoster='" + moviePoster + '\'' +
                ", movieId=" + movieId +
                '}';
    }

    @Override
    public boolean equals(Object vo) {
        if (this == vo) return true;
        if (vo == null || getClass() != vo.getClass()) return false;
        MovieNetworkLite vthat = (MovieNetworkLite) vo;
        return movieId == vthat.movieId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId);
    }
}
