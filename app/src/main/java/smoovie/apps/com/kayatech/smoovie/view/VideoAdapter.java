package smoovie.apps.com.kayatech.smoovie.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.MovieVideos;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MoviesVideoViewHolder> {

    //Adapter Class
    private List<MovieVideos> mMovieVideoList;
    private static IVideoClickHandler mIVideoClickHandler;
    private static IShareClickHandler mIShareClickHandler;


    VideoAdapter(List<MovieVideos> mMovieVideoList, IVideoClickHandler iVideoClickHandler, IShareClickHandler iShareClickHandler) {
        this.mMovieVideoList = mMovieVideoList;
        mIVideoClickHandler = iVideoClickHandler;
        mIShareClickHandler = iShareClickHandler;
    }

    public void setMovieVideoList(List<MovieVideos> mMovieVideoList) {
        this.mMovieVideoList = mMovieVideoList;
        notifyDataSetChanged();
    }

    public void clearMovies() {
        mMovieVideoList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.video_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesVideoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MoviesVideoViewHolder holder, int position) {
        holder.bind(mMovieVideoList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mMovieVideoList == null) ? 0 : mMovieVideoList.size();
    }

    public static class MoviesVideoViewHolder extends RecyclerView.ViewHolder {

        private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
        MovieVideos movieVideos;
        private Context ctx;
        @BindView(R.id.iv_trailer)
        ImageView mMovieTrailerThumbnailImageView;
        @BindView(R.id.iv_icon_share)
        ImageView mMovieTrailerShareImageView;
        @BindView(R.id.iv_trailer_play)
        ImageView mMovieTrailerPlay;

        MoviesVideoViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            ButterKnife.bind(this, itemView);
            mMovieTrailerPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIVideoClickHandler.onClick(movieVideos);
                }
            });
            mMovieTrailerShareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIShareClickHandler.onClick(movieVideos);
                }
            });
        }

        private void bind(MovieVideos movieVideos) {
            //Set Trailer image view thumbnail
            this.movieVideos = movieVideos;
            ctx = itemView.getContext();
            Picasso.with(ctx)
                    .load(String.format(YOUTUBE_THUMBNAIL_URL, movieVideos.getKeyTrailer()))
                    .placeholder(R.color.colorAlternate)
                    .into(mMovieTrailerThumbnailImageView);
        }
    }

}
