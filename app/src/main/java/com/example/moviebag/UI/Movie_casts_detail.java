package com.example.moviebag.UI;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.moviebag.Adapters.MovieCasting_Adapter;
import com.example.moviebag.MovieData_Models.Movie_Castings;
import com.example.moviebag.ViewModel.MovieCasts_VM;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;
import com.example.moviebag.databinding.ActivityMovieCastsDetailBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Movie_casts_detail extends AppCompatActivity {

    private ActivityMovieCastsDetailBinding binding;
    private GridLayoutManager gridLayoutManager;

    private static int movieID;

    private MovieCasting_Adapter movieCastingAdapter;
    private MovieCasts_VM movieCasts_vm;
    private List<Movie_Castings.Cast> castList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieCastsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie_casts_detail.this.finish();
            }
        });

        gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerViewCasts.setLayoutManager(gridLayoutManager);
        binding.recyclerViewCasts.setHasFixedSize(true);

        movieID = getIntent().getIntExtra("MovieID", 0);

        movieCasts_vm = new ViewModelProvider(this).get(MovieCasts_VM.class);
        movieCasts_vm.init(movieID);
        movieCasts_vm.getMovieCastingsLiveData().observe(this, new Observer<List<Movie_Castings.Cast>>() {
            @Override
            public void onChanged(List<Movie_Castings.Cast> casts) {
                if (casts!=null){
                    updateMovieCastsData(casts);

                    Log.e("CastsActivity: ", "Response: "+casts.size());
                }else {
                    Log.e("CastsActivity: ", "Response Null");
                }
            }
        });

    }

    private void updateMovieCastsData(List<Movie_Castings.Cast> casts) {
        if (movieCastingAdapter == null) {
            castList.addAll(casts);
            movieCastingAdapter = new MovieCasting_Adapter(this, castList);
            binding.recyclerViewCasts.setAdapter(movieCastingAdapter);
            movieCastingAdapter.notifyDataSetChanged();
            binding.progressBar.setVisibility(View.GONE);

        } else if (movieCasts_vm.getMovieCastingsLiveData().getValue() != null) {
            castList = movieCasts_vm.getMovieCastingsLiveData().getValue();
            movieCastingAdapter = new MovieCasting_Adapter(this, castList);
            binding.recyclerViewCasts.setAdapter(movieCastingAdapter);
            movieCastingAdapter.notifyDataSetChanged();
            binding.progressBar.setVisibility(View.GONE);
        } else {
            movieCastingAdapter.notifyDataSetChanged();
        }
    }

}