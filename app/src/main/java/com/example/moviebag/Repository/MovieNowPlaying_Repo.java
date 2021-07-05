package com.example.moviebag.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Now_Playing;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieNowPlaying_Repo {
    private API_Interface api_interface;
    private MutableLiveData<List<Movie_Now_Playing.Result>> liveDataRepo = new MutableLiveData<>();

    public MutableLiveData<List<Movie_Now_Playing.Result>> getLiveDataRepo(){
        api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Now_Playing> call = api_interface.getNow_Playing_Movies();
        call.enqueue(new Callback<Movie_Now_Playing>() {
            @Override
            public void onResponse(Call<Movie_Now_Playing> call, Response<Movie_Now_Playing> response) {
                liveDataRepo.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Movie_Now_Playing> call, Throwable t) {

                liveDataRepo.setValue(null);
            }
        });
        return liveDataRepo;
    }
}
