package com.example.moviebag.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviebag.MovieData_Models.Movie_Reviews;
import com.example.moviebag.Repository.MovieReview_Repo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieReview_VM extends AndroidViewModel {

    private MovieReview_Repo movieReviewRepo;
    private MutableLiveData<List<Movie_Reviews.Result>> mutableLiveData;
    public MovieReview_VM(@NonNull @NotNull Application application) {
        super(application);
    }

    public void init(int movieID){
        if (mutableLiveData!=null){
            return;
        }
        movieReviewRepo = new MovieReview_Repo(movieID);
        mutableLiveData = movieReviewRepo.getMutableLiveData_Repo();
    }

    public LiveData<List<Movie_Reviews.Result>> getMovieReviewLiveData(){
        return mutableLiveData;
    }
}
