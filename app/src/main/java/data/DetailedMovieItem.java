package data;

import com.google.gson.annotations.SerializedName;

public class DetailedMovieItem extends MovieItem {

//    @SerializedName("Poster")
//    private String photoUrl;
//
//    private String imdbId;
//
//    @SerializedName("Title")
//    private String title;
//
//    @SerializedName("Year")
//    private String year;


    @SerializedName("Released")
    private String released;

    @SerializedName("Runtime")
    private String runtime;

    @SerializedName("Director")
    private String director;

    @SerializedName("Plot")
    private String plot;

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

//    @Override
//    public String getPhotoUrl() {
//        return photoUrl;
//    }
//
//    @Override
//    public void setPhotoUrl(String photoUrl) {
//        this.photoUrl = photoUrl;
//    }
//
//    @Override
//    public String getImdbId() {
//        return imdbId;
//    }
//
//    @Override
//    public void setImdbId(String imdbId) {
//        this.imdbId = imdbId;
//    }
//
//    @Override
//    public String getTitle() {
//        return title;
//    }
//
//    @Override
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    @Override
//    public String getYear() {
//        return year;
//    }
//
//    @Override
//    public void setYear(String year) {
//        this.year = year;
//    }

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
