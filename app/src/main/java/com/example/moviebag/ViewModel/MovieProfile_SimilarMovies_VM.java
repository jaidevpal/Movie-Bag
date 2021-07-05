package com.example.moviebag.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Similar_Model;
import com.example.moviebag.Repository.MovieProfile_SimilarMovies_Repo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieProfile_SimilarMovies_VM extends AndroidViewModel {

    private MovieProfile_SimilarMovies_Repo movieProfile_similarMovies_repo;
    public MovieProfile_SimilarMovies_VM(@NonNull @NotNull Application application) {
        super(application);
    }

    private MutableLiveData<List<Movie_Similar_Model.Result>> liveData;

    public void init(int movieID){
        if (liveData!=null){
            return;
        }
        movieProfile_similarMovies_repo = new MovieProfile_SimilarMovies_Repo(movieID);
        liveData = movieProfile_similarMovies_repo.getMutableLiveData_SimilarMovies_Repo();
    }

    public LiveData<List<Movie_Similar_Model.Result>> getLiveData_SimilarMovies(){
        return liveData;
    }
}
