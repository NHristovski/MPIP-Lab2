package ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mpip_lab2.R;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import data.database.MovieDao;
import data.database.MovieDatabase;
import data.database.MovieDatabaseSingleton;
import data.movie.DetailedMovieItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.OMDbService;

import service.OMDbServiceSingleton;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "Nikola";
    public static final int MAX_THREADS = 2;
    private OMDbService mOMDbSerivce;
    private String imdbId;

    private ImageView moviePoster;
    private TextView title;
    private TextView released;
    private TextView runtime;
    private TextView director;
    private TextView actors;
    private TextView plot;

    private ExecutorService threadPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imdbId = getIntent().getStringExtra(MainActivity.EXTRA_IMDB_ID);

        mOMDbSerivce = OMDbServiceSingleton.getService();

        threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        findViews();
        loadData();

    }

    private void findViews() {
        actors = findViewById(R.id.tv_details_actors_value);

        plot = findViewById(R.id.tv_details_plot_value);
        plot.setMovementMethod(new ScrollingMovementMethod());

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
        final MovieDatabase database = MovieDatabaseSingleton.getDatabase(this);
        final MovieDao movieDao = database.getMovieDao();

        final Future<DetailedMovieItem> detailedMovieItemFuture =
                threadPool.submit(() -> movieDao.findById(imdbId));

        try {
            final DetailedMovieItem detailedMovieItem =
                    detailedMovieItemFuture.get(500, TimeUnit.MILLISECONDS);

            if (detailedMovieItem != null){
                Log.i(TAG,"Returning from database: " + detailedMovieItem.toString());
                populateViews(detailedMovieItem);
            }else{

                Log.i(TAG,"API CALL imcbId: " + imdbId);
                populateFromAPI(movieDao);
            }
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void populateFromAPI(MovieDao movieDao) {
        mOMDbSerivce.getDetails(imdbId).enqueue(new Callback<DetailedMovieItem>() {
            @Override
            public void onResponse(Call<DetailedMovieItem> call, Response<DetailedMovieItem> response) {
                if (response.isSuccessful()){
                    final DetailedMovieItem detailedMovieItem = response.body();

                    Log.i(TAG,"Got details: " + detailedMovieItem);
                    if (detailedMovieItem != null) {
                        populateViews(detailedMovieItem);

                        threadPool.submit(() -> movieDao.insert(detailedMovieItem));
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailedMovieItem> call, Throwable t) {
                Log.e(TAG,"Failure to get details");
                Toast.makeText(DetailsActivity.this,
                        "Something went wrong. Please try again later"
                        ,Toast.LENGTH_LONG).show();
            }
        });
    }
}
