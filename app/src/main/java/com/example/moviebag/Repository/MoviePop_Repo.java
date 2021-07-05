package com.example.moviebag.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Popular;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviePop_Repo {
    private int pageNo;
    private API_Interface api_interface;
    private static MoviePop_Repo instance;

    private MutableLiveData<Movie_Popular> listMutableLiveData = new MutableLiveData<>();

    private List<Movie_Popular.Result> movie_populars = new ArrayList<>();

    public MoviePop_Repo(int pageNo) {
        this.pageNo = pageNo;
    }

    public MutableLiveData<Movie_Popular> getListMutableLiveData() {
        api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Popular> call = api_interface.getPopular_Movies(pageNo);
        call.enqueue(new Callback<Movie_Popular>() {
            @Override
            public void onResponse(Call<Movie_Popular> call, Response<Movie_Popular> response) {
                listMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Movie_Popular> call, Throwable t) {

                Log.e(this.getClass().getSimpleName(), "Response Failure: " + t);
            }
        });

        return listMutableLiveData;
    }
}
