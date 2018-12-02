package ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mpip_lab2.R;

import java.util.ArrayList;
import java.util.List;

import data.adapter.MovieAdapter;
import data.movie.MovieItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.OMDbService;
import service.OMDbServiceSingleton;
import data.movie.SearchResult;

public class MainActivity extends AppCompatActivity {

    public static final String PAGE_KEY = "page";
    private static final String ALL_MOVIES_KEY = "all_movies";

    public static final String EXTRA_IMDB_ID = "imdbID";
    public static final String TAG = "Nikola";
    public static final int FIRST_PAGE = 1;
    public static final int DOWN = 1;
    public static final String QUERY_KEY = "queryKey";
    public static final String TOTAL_RESULTS_KEY = "total";
    public static final int SPAN_COUNT = 2;

    private RecyclerView mRecyclerView;
    private OMDbService mOMDbService;
    private MovieAdapter mMovieAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String query;
    private int page;
    private int totalResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        }else{
            layoutManager = new LinearLayoutManager(this);
        }

        if(savedInstanceState == null) {
            initRecyclerView(new ArrayList<>(),layoutManager);

            page = 1;
            query = "";
            totalResults = 0;

        }else{
            final ArrayList<String> movies =
                    savedInstanceState.getStringArrayList(ALL_MOVIES_KEY);

            List<MovieItem> movieItems = new ArrayList<>(movies.size());

            for(String movie : movies){
                movieItems.add(MovieItem.fromString(movie));
            }

            initRecyclerView(movieItems,layoutManager);

            page = savedInstanceState.getInt(PAGE_KEY);
            query = savedInstanceState.getString(QUERY_KEY);
            totalResults = savedInstanceState.getInt(TOTAL_RESULTS_KEY);
        }

        mOMDbService = OMDbServiceSingleton.getService();
    }

    private void initRecyclerView(List<MovieItem> movieItems, RecyclerView.LayoutManager layoutManager) {

        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, movieItems, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(DOWN)) {
                    addNewItemsToAdapter();
                }
            }
        });
    }

    private void addNewItemsToAdapter() {
        Log.i(TAG,"Adapter items: " + mMovieAdapter.getItemCount() + " and total results: " + totalResults);

        if (mMovieAdapter.getItemCount() < totalResults){
            page++;
            Log.i(TAG,"Page now: " + page);
            mOMDbService.getMovies(query, page).enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    if (response.isSuccessful()){
                        final SearchResult searchResult = response.body();

                        if (searchResult != null) {

                            final List<MovieItem> dataToAdd = searchResult.getMovieItemList();
                            if(dataToAdd != null) {
                                mMovieAdapter.addData(dataToAdd);
                                mMovieAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    else{
                        Log.e(TAG,"Response not successful");
                        showFailureMessage();
                    }
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    Log.e(TAG,t.toString());
                    Log.e(TAG,"onFailure called");
                    showFailureMessage();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.i(TAG,"SE POVIKA HANDLE INTENT");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);

            Log.i(TAG,searchQuery);

            mMovieAdapter.clear();
            mOMDbService.getMovies(searchQuery, FIRST_PAGE).enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    if (response.isSuccessful()){
                        final SearchResult searchResult = response.body();

                        if (searchResult != null) {
                            query = searchQuery;
                            page = FIRST_PAGE;
                            totalResults = searchResult.getTotalResults();

                            mMovieAdapter.addData(searchResult.getMovieItemList());
                            mMovieAdapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        query = searchQuery;
                        page = FIRST_PAGE;
                        Log.e(TAG,"Response not successful");
                        showFailureMessage();
                    }
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    Log.e(TAG,t.toString());
                    Log.e(TAG,"onFailure called");
                    showFailureMessage();
                }
            });
        }
    }

    private void showFailureMessage() {
        Toast.makeText(MainActivity.this,
                R.string.errorFetchingMoviesMessage
                ,Toast.LENGTH_SHORT).show();
    }


    public void onClick(String imdbId) {
        Intent intent = new Intent(this,DetailsActivity.class);
        Log.i(TAG,"Adding imdbId: " + imdbId);
        intent.putExtra(EXTRA_IMDB_ID,imdbId);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        final List<MovieItem> movieItemList = mMovieAdapter.getMovieItemList();
        ArrayList<String> movies = new ArrayList<>(movieItemList.size());

        for(MovieItem movieItem : movieItemList){
            movies.add(movieItem.toString());
        }

        outState.putStringArrayList(ALL_MOVIES_KEY,movies);

        outState.putString(QUERY_KEY,query);

        outState.putInt(TOTAL_RESULTS_KEY,totalResults);

        outState.putInt(PAGE_KEY,page);

        super.onSaveInstanceState(outState);
    }
}
