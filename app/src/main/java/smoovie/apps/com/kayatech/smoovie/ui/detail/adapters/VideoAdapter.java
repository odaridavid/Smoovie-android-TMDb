package smoovie.apps.com.kayatech.smoovie.ui.detail.adapters;

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
import smoovie.apps.com.kayatech.smoovie.model.Trailers;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MoviesVideoViewHolder> {

    //Adapter Class
    private List<Trailers> mMovieVideoList;
    private static ITrailerClickHandler sMITrailerClickHandler;
    private static IShareClickHandler mIShareClickHandler;


    VideoAdapter(List<Trailers> mMovieVideoList, ITrailerClickHandler iTrailerClickHandler, IShareClickHandler iShareClickHandler) {
        this.mMovieVideoList = mMovieVideoList;
        sMITrailerClickHandler = iTrailerClickHandler;
        mIShareClickHandler = iShareClickHandler;
    }

    public void setMovieVideoList(List<Trailers> mMovieVideoList) {
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
        int layoutIdForListItem = R.layout.trailer_layout;
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
        Trailers mTrailers;
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
                    sMITrailerClickHandler.onClick(mTrailers);
                }
            });
            mMovieTrailerShareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIShareClickHandler.onClick(mTrailers);
                }
            });
        }

        private void bind(Trailers trailers) {
            //Set Trailer image view thumbnail
            this.mTrailers = trailers;
            ctx = itemView.getContext();
            Picasso.with(ctx)
                    .load(String.format(YOUTUBE_THUMBNAIL_URL, trailers.getKey()))
                    .placeholder(R.color.colorAlternate)
                    .into(mMovieTrailerThumbnailImageView);
        }
    }

}
