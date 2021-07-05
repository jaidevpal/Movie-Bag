package com.example.moviebag.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.moviebag.Adapters.MovieReview_Adapter;
import com.example.moviebag.MovieData_Models.Movie_Reviews;
import com.example.moviebag.ViewModel.MovieReview_VM;
import com.example.moviebag.databinding.ActivityMovieReviewsBinding;

import java.util.ArrayList;
import java.util.List;

public class Movie_Reviews_Activity extends AppCompatActivity {

    private ActivityMovieReviewsBinding binding;
    private LinearLayoutManager linearLayoutManager;

    private MovieReview_Adapter movieReviewAdapter;
    private MovieReview_VM movieReview_vm;

    private List<Movie_Reviews.Result> reviewList = new ArrayList<>();
    private int movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie_Reviews_Activity.this.finish();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewReviews.setLayoutManager(linearLayoutManager);

        movieID = getIntent().getIntExtra("MovieID", 0);

        movieReview_vm = new ViewModelProvider(this).get(MovieReview_VM.class);
        movieReview_vm.init(movieID);
        movieReview_vm.getMovieReviewLiveData().observe(this, new Observer<List<Movie_Reviews.Result>>() {
            @Override
            public void onChanged(List<Movie_Reviews.Result> results) {
                updateMovieReviewData(results);
            }
        });

    }

    private void updateMovieReviewData(List<Movie_Reviews.Result> results) {

        if (movieReviewAdapter == null) {
            reviewList.addAll(results);
            movieReviewAdapter = new MovieReview_Adapter(this, reviewList);
            binding.recyclerViewReviews.setAdapter(movieReviewAdapter);
            movieReviewAdapter.notifyDataSetChanged();
            binding.progressBar.setVisibility(View.GONE);
        } else if (movieReview_vm.getMovieReviewLiveData().getValue() != null) {
            reviewList = movieReview_vm.getMovieReviewLiveData().getValue();
            movieReviewAdapter = new MovieReview_Adapter(this, reviewList);
            binding.recyclerViewReviews.setAdapter(movieReviewAdapter);
            binding.progressBar.setVisibility(View.GONE);
        } else {
            movieReviewAdapter.notifyDataSetChanged();
        }
    }
}