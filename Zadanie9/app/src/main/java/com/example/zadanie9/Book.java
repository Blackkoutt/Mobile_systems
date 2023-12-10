package com.example.zadanie9;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// Model książki
public class Book implements Serializable {
    // SerializedName to nazwa właściwości w otrzymanym pliku .json
    @SerializedName("title")
    private String title;
    @SerializedName("author_name")
    private List<String> authors;
    @SerializedName("cover_i")
    private String cover;
    @SerializedName("publisher")
    private List<String> publisher;

    @SerializedName("language")
    private List<String> language;

    @SerializedName("ratings_average")
    private String rating;
    @SerializedName("number_of_pages_median")
    private String numberOfPages;

   public void setTitle(String value){
       this.title=value;
   }
   public String getTitle(){
       return this.title;
   }
   public List<String> getAuthors(){
       return this.authors;
   }
   public String getCover(){
       return this.cover;
   }
   public void setCover(String value){
       this.cover=value;
   }
   public String getNumberOfPages(){
       return this.numberOfPages;
   }
   public void setNumberOfPages(String value){
       this.numberOfPages=value;
   }
    public List<String> getPublishers(){
        return this.publisher;
    }
    public List<String> getLanguages(){
        return this.language;
    }
    public String getRating(){
        return this.rating;
    }

}
