package data.movie;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "detailed_movie")
public class DetailedMovieItem extends MovieItem {

    @ColumnInfo(name = "released")
    @SerializedName("Released")
    private String released;

    @ColumnInfo(name = "runtime")
    @SerializedName("Runtime")
    private String runtime;

    @ColumnInfo(name = "director")
    @SerializedName("Director")
    private String director;

    @ColumnInfo(name = "plot")
    @SerializedName("Plot")
    private String plot;

    @ColumnInfo(name = "actors")
    @SerializedName("Actors")
    private String actors;

    public DetailedMovieItem(){
        super();
    }

    public DetailedMovieItem(String photoUrl, String imdbId, String title, String year, String rated, String released, String runtime, String genre, String director, String plot, String imdbRating, String imdbVotes, String actors) {
        super(photoUrl, imdbId, title, year);
        this.released = released;
        this.runtime = runtime;
        this.director = director;
        this.plot = plot;
        this.actors = actors;
    }


    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "DetailedMovieItem{" +
                "released='" + released + '\'' +
                ", runtime='" + runtime + '\'' +
                ", director='" + director + '\'' +
                ", plot='" + plot + '\'' +
                ", actors='" + actors + '\'' +
                ", title='" + getTitle() + '\'' +
                ", photoUrl='" + getPhotoUrl() + '\'' +
                ", year='" + getYear() + '\'' +
                ", ID='" + getImdbId() + '\'' +
                '}';
    }
}
