package com.example.moviebag.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Now_Playing;
import com.example.moviebag.Repository.MovieNowPlaying_Repo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieNow_VM extends AndroidViewModel {

    private MutableLiveData<List<Movie_Now_Playing.Result>> mutableLiveData;
    private MovieNowPlaying_Repo movieNowPlaying_repo;

    public MovieNow_VM(@NonNull @NotNull Application application) {
        super(application);
    }

    public LiveData<List<Movie_Now_Playing.Result>> getListLiveDataMovieNow(){

        movieNowPlaying_repo = new MovieNowPlaying_Repo();
        mutableLiveData = movieNowPlaying_repo.getLiveDataRepo();
        return mutableLiveData;
    }
}
