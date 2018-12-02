package data.movie;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.movie.MovieItem;

public class SearchResult {

    @SerializedName("Search")
    private List<MovieItem> movieItemList;

    private int totalResults;

    public SearchResult(List<MovieItem> movieItemList, int totalResults) {
        this.movieItemList = new ArrayList<>(movieItemList);
        this.totalResults = totalResults;
    }

    public List<MovieItem> getMovieItemList() {
        return Collections.unmodifiableList(movieItemList);
    }

    public void setMovieItemList(List<MovieItem> movieItemList) {
        this.movieItemList = new ArrayList<>(movieItemList);
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
