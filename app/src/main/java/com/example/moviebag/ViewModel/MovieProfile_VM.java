package com.example.moviebag.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Profile_FullModel;
import com.example.moviebag.Repository.MovieProfile_Repo;

import org.jetbrains.annotations.NotNull;

public class MovieProfile_VM extends AndroidViewModel {

    private MovieProfile_Repo movieProfile_repo;
    private MutableLiveData<Movie_Profile_FullModel> liveData;

    public MovieProfile_VM(@NonNull @NotNull Application application) {
        super(application);
    }

    public void init(int movieID){
        if (liveData!=null){
            return;
        }
        movieProfile_repo = new MovieProfile_Repo(movieID);
        liveData = movieProfile_repo.getMutableLiveData_Repo();
    }

    public LiveData<Movie_Profile_FullModel> getMovieProfileLiveData(){
        return liveData;
    }
}
