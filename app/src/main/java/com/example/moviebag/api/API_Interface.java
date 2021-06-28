package com.example.moviebag.api;

import com.example.moviebag.MovieData_Models.Movie_Castings;
import com.example.moviebag.MovieData_Models.Movie_Now_Playing;
import com.example.moviebag.MovieData_Models.Movie_Popular;
import com.example.moviebag.MovieData_Models.Movie_Profile_FullModel;
import com.example.moviebag.MovieData_Models.Movie_Reviews;
import com.example.moviebag.MovieData_Models.Movie_Similar_Model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API_Interface {

    //    Key from API's main access
    String API_KEY = "?api_key=c96bb61aaeba0a7910d8e13b3a7b2575";

    //    Accessing directories
    @GET("now_playing" + API_KEY + "&region=in")
    Call<Movie_Now_Playing> getNow_Playing_Movies();

    @GET("popular" + API_KEY)
    Call<Movie_Popular> getPopular_Movies(@Query("page") int pageNumber);

    @GET("{id}" + API_KEY)
    Call<Movie_Profile_FullModel> getMovie_Profile(@Path("id") int movieID);

    @GET("{id}/similar" + API_KEY)
    Call<Movie_Similar_Model> getSimilar_Movies(@Path("id") int movieID);

    @GET("{id}/reviews" + API_KEY)
    Call<Movie_Reviews> getMoviesReviews(@Path("id") int movieID);

    @GET("{id}/casts" + API_KEY)
    Call<Movie_Castings> getMoviesCastings(@Path("id") int movieID);


}
