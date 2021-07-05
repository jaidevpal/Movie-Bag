package com.example.moviebag.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Profile_FullModel;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieProfile_Repo {
    private int movieID;

    public MovieProfile_Repo(int movieID){
        this.movieID = movieID;
    }

    private MutableLiveData<Movie_Profile_FullModel> mutableLiveData_Repo = new MutableLiveData<>();

    public MutableLiveData<Movie_Profile_FullModel> getMutableLiveData_Repo(){
        API_Interface api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Profile_FullModel> call = api_interface.getMovie_Profile(movieID);
        call.enqueue(new Callback<Movie_Profile_FullModel>() {
            @Override
            public void onResponse(Call<Movie_Profile_FullModel> call, Response<Movie_Profile_FullModel> response) {
                mutableLiveData_Repo.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Movie_Profile_FullModel> call, Throwable t) {

            }
        });

        return mutableLiveData_Repo;
    }
}
