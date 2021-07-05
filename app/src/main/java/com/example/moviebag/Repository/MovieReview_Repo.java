package com.example.moviebag.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Reviews;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieReview_Repo {

    private final String TAG = this.getClass().getSimpleName();

    private int movieID;
    private MutableLiveData<List<Movie_Reviews.Result>> liveData = new MutableLiveData<>();

    public MovieReview_Repo(int movieID){
        this.movieID = movieID;
    }

    public MutableLiveData<List<Movie_Reviews.Result>> getMutableLiveData_Repo(){

        API_Interface api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Reviews> call = api_interface.getMoviesReviews(movieID);
        call.enqueue(new Callback<Movie_Reviews>() {
            @Override
            public void onResponse(Call<Movie_Reviews> call, Response<Movie_Reviews> response) {
                if (response.isSuccessful()){
                    liveData.setValue(response.body().getResults());
                    Log.e(TAG, " | Response Success: "+ response.body().getResults().size());
                }
            }

            @Override
            public void onFailure(Call<Movie_Reviews> call, Throwable t) {
                liveData.setValue(null);
                Log.e(TAG," | Response Failure: "+t);
            }
        });
        return liveData;
    };
}
