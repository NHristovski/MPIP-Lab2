package ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mpip_lab2.R;

import java.util.ArrayList;

import data.adapter.MovieAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.OMDbService;
import service.OMDbServiceSingleton;
import data.movie.SearchResult;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_IMDB_ID = "imdbID";
    public static final String TAG = "Nikola";
    public static final int FIRST_PAGE = 1;
    public static final int DOWN = 1;
    private RecyclerView mRecyclerView;
    private OMDbService mOMDbService;
    private MovieAdapter mMovieAdapter;

    private String query;
    private int page;
    private int totalResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager linerLayoutMenager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linerLayoutMenager);

        mMovieAdapter = new MovieAdapter(this, new ArrayList<>(),this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(DOWN)){
                    addNewItemsToAdapter();
                }
            }
        });

        mOMDbService = OMDbServiceSingleton.getService();

        page = 1;
        query = "";
        totalResults = 0;
    }

    private void addNewItemsToAdapter() {
        Log.i(TAG,"SE POVIKA ADD NEW ITEMS");
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

                            mMovieAdapter.addData(searchResult.getMovieItemList());
                            mMovieAdapter.notifyDataSetChanged();
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

}
