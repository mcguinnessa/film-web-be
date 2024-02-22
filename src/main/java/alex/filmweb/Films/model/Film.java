package alex.filmweb.Films.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// This overrides the collection name
@Document(collection = "films")
public class Film {

    @Id
    private String id;

    private String imdbid;

    private String title;
    private Short year;
    private Short runtime;
    private Float imdb_rating;
    private String classification;
    private String media_type;
    private Boolean watched;

    public Film(String title, String imdbid, Short year, Short runtime, Float imdb_rating, String classification, String media_type, Boolean watched){
        this.title = title;
        this.imdbid = imdbid;
        this.year = year;
        this.runtime = runtime;
        this.imdb_rating = imdb_rating;
        this.classification = classification;
        this.media_type = media_type;
        this.watched = watched;
    }

    public Film(){
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public Short getYear(){
        return year;
    }

    public void setYear(Short year){
        this.year = year;
    }

    public Short getRuntime(){
        return runtime;
    }

    public void setRuntime(Short runtime){
        this.runtime = runtime;
    }


    public Float getImdb_rating() {
        return imdb_rating;
    }

    public void setImdb_rating(Float imdb_rating) {
        this.imdb_rating = imdb_rating;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public Boolean getWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id='" + id + '\'' +
                ", imdbid='" + imdbid + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", runtime=" + runtime +
                ", imdb_rating=" + imdb_rating +
                ", classification='" + classification + '\'' +
                ", media_type='" + media_type + '\'' +
                ", watched=" + watched +
                '}';
    }

    public String getImdbid() {
        return imdbid;
    }

    public void setImdbid(String imdbid) {
        this.imdbid = imdbid;
    }
}
