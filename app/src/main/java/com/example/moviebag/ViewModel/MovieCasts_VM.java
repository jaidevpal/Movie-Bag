package com.example.moviebag.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Castings;
import com.example.moviebag.Repository.MovieCasts_Repo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieCasts_VM extends AndroidViewModel {

    private MovieCasts_Repo movieCasts_repo;

    public MovieCasts_VM(@NonNull @NotNull Application application) {
        super(application);
    }

    private MutableLiveData<List<Movie_Castings.Cast>> liveData;

    public void init(int movieID){
        if (liveData!=null){
            return;
        }
        movieCasts_repo = new MovieCasts_Repo(movieID);
        liveData = movieCasts_repo.getmutableLiveData_Repo();

    }
    public LiveData<List<Movie_Castings.Cast>> getMovieCastingsLiveData(){
        return liveData;
    }
}
