package com.example.moviebag.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebag.MovieData_Models.Movie_Popular;
import com.example.moviebag.Repository.MoviePop_Repo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoviePop_VM extends AndroidViewModel {

    private int pageNo;

    private MutableLiveData<Movie_Popular> listMutableLiveData;
    private MoviePop_Repo moviePop_repo;

    public MoviePop_VM(@NonNull @NotNull Application application) {
        super(application);
    }

    public LiveData<Movie_Popular> getListLiveData(int pageNo){
        this.pageNo = pageNo;

        moviePop_repo = new MoviePop_Repo(pageNo);
        listMutableLiveData = moviePop_repo.getListMutableLiveData();
        return listMutableLiveData;
    };
}
