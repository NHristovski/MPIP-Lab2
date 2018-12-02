package service;


import data.movie.DetailedMovieItem;
import data.movie.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface OMDbService {

    @GET("?type=movie")
    Call<SearchResult> getMovies(@Query("s") String title, @Query("page") int page);

    @GET("?plot=short")
    Call<DetailedMovieItem> getDetails(@Query("i") String ImdbId);

}

