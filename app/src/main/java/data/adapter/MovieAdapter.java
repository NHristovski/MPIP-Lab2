package data.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mpip_lab2.R;
import ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import data.movie.MovieItem;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieItem> movieItemList;
    private Context context;
    private MainActivity mainActivity;

    public MovieAdapter(Context context, List<MovieItem> movieItemList, MainActivity mainActivity){
        this.context = context;
        this.movieItemList = new ArrayList<>(movieItemList);
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final LayoutInflater layoutInflater =
                context.getSystemService(LayoutInflater.class);

        CardView cardView = (CardView)
                layoutInflater.inflate(R.layout.movie_card,viewGroup,false);


        MovieViewHolder viewHolder = new MovieViewHolder(cardView);
        viewHolder.setOnClickListener(mainActivity);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
            movieViewHolder.bind(movieItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return movieItemList.size();
    }

    public void addData(List<MovieItem> movieItems){
        this.movieItemList.addAll(movieItems);
    }

    public void clear(){
        this.movieItemList.clear();
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView moviePoster;
        private TextView movieTitle;
        private TextView movieYear;
        private View parent;

        private String imdbId;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            this.moviePoster = itemView.findViewById(R.id.iv_movie_poster);
            this.movieTitle = itemView.findViewById(R.id.tv_movie_title);
            this.movieYear = itemView.findViewById(R.id.tv_movie_year);
        }

        public void bind(MovieItem movieItem){
            Glide.with(itemView.getContext())
                    .load(movieItem.getPhotoUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image))
                    .into(moviePoster);

            movieTitle.setText(movieItem.getTitle());

            imdbId = movieItem.getImdbId();

            movieYear.setText(movieItem.getYear());

        }

        public void setOnClickListener(MainActivity mainActivity){
            itemView.setOnClickListener((view -> {
                Log.i("Nikola","From adapter sending imdbId: " + this.imdbId);
                mainActivity.onClick(this.imdbId);
            }));
        }
        public View getParent() {
            return parent;
        }

        public void setParent(View parent) {
            this.parent = parent;
        }
    }
}
