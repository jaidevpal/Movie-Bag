package com.example.moviebag.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Similar_Model;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieProfile_SimilarMovies_Repo {

    private int movieID;

    public MovieProfile_SimilarMovies_Repo(int movieID){
        this.movieID = movieID;
    }

    private MutableLiveData<List<Movie_Similar_Model.Result>> mutableLiveData_Repo = new MutableLiveData<>();

    public MutableLiveData<List<Movie_Similar_Model.Result>> getMutableLiveData_SimilarMovies_Repo(){
        API_Interface api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Similar_Model> call = api_interface.getSimilar_Movies(movieID);
        call.enqueue(new Callback<Movie_Similar_Model>() {
            @Override
            public void onResponse(Call<Movie_Similar_Model> call, Response<Movie_Similar_Model> response) {
                mutableLiveData_Repo.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Movie_Similar_Model> call, Throwable t) {

                mutableLiveData_Repo.setValue(null);
            }
        });

        return mutableLiveData_Repo;
    }
}
