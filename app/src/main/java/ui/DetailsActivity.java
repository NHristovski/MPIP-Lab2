package ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mpip_lab2.R;

import data.DetailedMovieItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.OMDbService;

import service.OMDbServiceSingleton;
import ui.MainActivity;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "Nikola";
    private OMDbService mOMDbSerivce;
    private String imdbId;

    private ImageView moviePoster;
    private TextView title;
    private TextView released;
    private TextView runtime;
    private TextView director;
    private TextView actors;
    private TextView plot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imdbId = getIntent().getStringExtra(MainActivity.EXTRA_IMDB_ID);

        mOMDbSerivce = OMDbServiceSingleton.getService();

        findViews();
        loadData();
    }

    private void findViews() {
        actors = findViewById(R.id.tv_details_actors_value);
        plot = findViewById(R.id.tv_details_plot_value);
        runtime = findViewById(R.id.tv_details_runtime_value);
        director = findViewById(R.id.tv_details_director_value);
        released = findViewById(R.id.tv_details_released_value);
        title = findViewById(R.id.tv_details_title_value);
        moviePoster = findViewById(R.id.iv_details_poster);
    }

    private void populateViews(DetailedMovieItem detailedMovieItem){
        Log.i(TAG,detailedMovieItem.toString());

        actors.setText(detailedMovieItem.getActors());

        plot.setText(detailedMovieItem.getPlot());

        runtime.setText(detailedMovieItem.getRuntime());

        director.setText(detailedMovieItem.getDirector());

        released.setText(detailedMovieItem.getReleased());

        title.setText(detailedMovieItem.getTitle());

        Glide.with(this)
                .load(detailedMovieItem.getPhotoUrl())
                .apply(new RequestOptions().placeholder(R.drawable.no_image))
                .into(moviePoster);
    }

    private void loadData() {
        if (false)//database contains imdb load from database){
        {
        }
        else{
            Log.i(TAG,"imcbId: " + imdbId);
            mOMDbSerivce.getDetails(imdbId).enqueue(new Callback<DetailedMovieItem>() {
                @Override
                public void onResponse(Call<DetailedMovieItem> call, Response<DetailedMovieItem> response) {
                    if (response.isSuccessful()){
                        final DetailedMovieItem detailedMovieItem = response.body();

                        Log.i(TAG,"Got details: " + detailedMovieItem.toString());
                        populateViews(detailedMovieItem);
                        //TODO put in database;
                    }
                }

                @Override
                public void onFailure(Call<DetailedMovieItem> call, Throwable t) {

                }
            });
        }
    }
}
