package smoovie.apps.com.kayatech.smoovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    //Adapter Class
    private Context context;
    private List<Movie> MovieList;


    MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.MovieList = movies;


    }

    public void setMovieList(List<Movie> movieList) {
        this.MovieList = movieList;
        //gets change in movie list

    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_grid_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.bind(MovieList.get(position));
    }

    @Override
    public int getItemCount() {
        //chek for null
        return (MovieList == null) ? 0 : MovieList.size();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private Context ctx;
        private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
        TextView mMovieTitle;
        ImageView mPosterImage;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            mMovieTitle = itemView.findViewById(R.id.movie_title_text_view);
            mPosterImage = itemView.findViewById(R.id.poster_image_view);
        }

        public void bind(Movie movie) {
            ctx = itemView.getContext();
            mMovieTitle.setText(movie.getMovieTitle());
            Picasso.with(ctx).load(IMAGE_BASE_URL + movie.getMoviePoster()).error(R.drawable.test).into(mPosterImage);

        }
    }

}
