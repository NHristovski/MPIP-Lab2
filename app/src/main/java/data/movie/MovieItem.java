package data.movie;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie")
public class MovieItem {


    @ColumnInfo(name = "photoUrl")
    @SerializedName("Poster")
    private String photoUrl;

    @PrimaryKey
    @NonNull
    @SerializedName("imdbID")
    private String imdbId;

    @ColumnInfo(name = "title")
    @SerializedName("Title")
    private String title;

    @ColumnInfo(name = "year")
    @SerializedName("Year")
    private String year;

    @Ignore
    public MovieItem(){}

    public MovieItem(String photoUrl, @NonNull String imdbId, String title, String year) {
        this.photoUrl = photoUrl;
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
