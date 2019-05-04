package smoovie.apps.com.kayatech.smoovie.ui.main.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.IMovie;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieNetworkLite;
import smoovie.apps.com.kayatech.smoovie.util.SmooviePosterImageView;

import static smoovie.apps.com.kayatech.smoovie.util.Constants.POSTER_BASE_URL;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<MovieNetworkLite> mMovieList;
    private IMovieClickHandler mIMovieClickHandler;
    private List<Movie> favouriteMovie;

    public MoviesAdapter(List<MovieNetworkLite> movies,
                         List<Movie> favMovies,
                         IMovieClickHandler IMovieClickHandler) {

        this.mMovieList = movies;
        mIMovieClickHandler = IMovieClickHandler;
        favouriteMovie = favMovies;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        if (mMovieList != null)
            holder.bind(mMovieList.get(position));
        if (favouriteMovie != null)
            holder.bind(favouriteMovie.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMovieList != null)
            return mMovieList.size();
        else
            return favouriteMovie.size();
    }


    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        IMovie movie;
        @BindView(R.id.iv_poster_image)
        SmooviePosterImageView mPosterImage;
        @BindView(R.id.tv_rating_cardlabel)
        TextView mMovieRatings;

        MoviesViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mMovieList != null)
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movie = mMovieList.get(getAdapterPosition());
                        mIMovieClickHandler.viewMovieDetails(movie, mPosterImage, false);
                    }
                });
            else
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movie = favouriteMovie.get(getAdapterPosition());
                        mIMovieClickHandler.viewMovieDetails(movie, mPosterImage, true);
                    }
                });

        }

        private void bind(IMovie movie) {
            Context ctx = itemView.getContext();
            Picasso.with(ctx)
                    .load(POSTER_BASE_URL + movie.getMoviePoster())
                    .error(R.drawable.test)
                    .placeholder(R.drawable.test)
                    .into(mPosterImage);
            String rating = " " + movie.getVoterAverage() + " ";
            mMovieRatings.setText(rating);
        }
    }

    public interface IMovieClickHandler {
        void viewMovieDetails(IMovie movie, SmooviePosterImageView view, boolean isFavourite);
    }

}
