package com.example.moviebag.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Castings;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieCasts_Repo {
    private int movieID;

    public MovieCasts_Repo(int movieID) {
        this.movieID = movieID;
    }

    private MutableLiveData<List<Movie_Castings.Cast>> mutableLiveData = new MutableLiveData<>();;

    public MutableLiveData<List<Movie_Castings.Cast>> getmutableLiveData_Repo() {
        API_Interface api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Castings> call= api_interface.getMoviesCastings(movieID);
        call.enqueue(new Callback<Movie_Castings>() {
            @Override
            public void onResponse(Call<Movie_Castings> call, Response<Movie_Castings> response) {
                if (response.isSuccessful()){
                    mutableLiveData.setValue(response.body().getCast());
                    Log.e("MovieCasts: ", "Response Success!");
                }
            }

            @Override
            public void onFailure(Call<Movie_Castings> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    ;
}
