package com.example.moviebag.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.moviebag.Adapters.MovieProfile_ProductionHouse_Adapter;
import com.example.moviebag.Adapters.MovieProfile_SimilarMovies_Adapter;
import com.example.moviebag.MovieData_Models.Movie_Profile_FullModel;
import com.example.moviebag.MovieData_Models.Movie_Similar_Model;
import com.example.moviebag.R;
import com.example.moviebag.Tools.Custom_Utilities;
import com.example.moviebag.Tools.Custom_ViewOnItemClickListener;
import com.example.moviebag.ViewModel.MovieProfile_SimilarMovies_VM;
import com.example.moviebag.ViewModel.MovieProfile_VM;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;
import com.example.moviebag.databinding.ActivityMovieProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Movie_Profile extends AppCompatActivity implements Custom_ViewOnItemClickListener {

    private ActivityMovieProfileBinding binding;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManagerSM;

    private API_Interface api_interface;
    private List<Movie_Profile_FullModel> movie_profile = new ArrayList<>();
    private List<Movie_Similar_Model.Result> movie_similar = new ArrayList<>();

    private MovieProfile_SimilarMovies_Adapter similarMoviesAdapter;

    private MovieProfile_VM movieProfile_vm;
    private MovieProfile_SimilarMovies_VM movieProfile_similarMovies_vm;

    private static int movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie_Profile.this.finish();
            }
        });


        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.viewpagerProductionHouse.setLayoutManager(linearLayoutManager);
        binding.viewpagerProductionHouse.setHorizontalScrollBarEnabled(true);

        linearLayoutManagerSM = new LinearLayoutManager(this);
        linearLayoutManagerSM.setOrientation(RecyclerView.HORIZONTAL);
        binding.similarMovieRV.setLayoutManager(linearLayoutManagerSM);

        api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);

//        Getting Particular Movie ID from HomePage(MainActivity)
        Intent intent = getIntent();
        movieID = intent.getIntExtra("MovieID", 0);

        /**
         * ViewModel for Movie Profile Data From here
         */
        movieProfile_vm = new ViewModelProvider(this).get(MovieProfile_VM.class);
        movieProfile_vm.init(movieID);
        movieProfile_vm.getMovieProfileLiveData().observe(this, new Observer<Movie_Profile_FullModel>() {
            @Override
            public void onChanged(Movie_Profile_FullModel movie_profile_fullModel) {

                setMovieDataToProfileViewSetup(movie_profile_fullModel);
            }
        });

        /**
         *  View Model for Movie Profile Similar Movies from here
         */

        movieProfile_similarMovies_vm = new ViewModelProvider(this).get(MovieProfile_SimilarMovies_VM.class);
        movieProfile_similarMovies_vm.init(movieID);
        movieProfile_similarMovies_vm.getLiveData_SimilarMovies().observe(this, new Observer<List<Movie_Similar_Model.Result>>() {
            @Override
            public void onChanged(List<Movie_Similar_Model.Result> results) {
                updateSimilarMoviesData(results);
            }
        });
        
        buttonHandlingClicks();
    }

    private void updateSimilarMoviesData(List<Movie_Similar_Model.Result> results) {
        if (similarMoviesAdapter == null) {
            movie_similar.addAll(results);
            similarMoviesAdapter = new MovieProfile_SimilarMovies_Adapter(this, results, this);
            binding.similarMovieRV.setAdapter(similarMoviesAdapter);
            similarMoviesAdapter.notifyDataSetChanged();
        } else if (movieProfile_similarMovies_vm.getLiveData_SimilarMovies().getValue() != null) {
            List<Movie_Similar_Model.Result> data = movieProfile_similarMovies_vm.getLiveData_SimilarMovies().getValue();
            movie_similar = data;
            similarMoviesAdapter = new MovieProfile_SimilarMovies_Adapter(this, data, this);
            binding.similarMovieRV.setAdapter(similarMoviesAdapter);
        } else {
            similarMoviesAdapter.notifyDataSetChanged();
        }
    }

    private void setMovieDataToProfileViewSetup(Movie_Profile_FullModel newData) {

        binding.shimmer.setVisibility(View.GONE);
        binding.shimmer.stopShimmer();
        binding.appBarLayout.setVisibility(View.VISIBLE);
        binding.profileLayoutMain.setVisibility(View.VISIBLE);

        if (movieProfile_vm.getMovieProfileLiveData().getValue() == null) {

            String backDoprPoster_imageURL = "https://image.tmdb.org/t/p/w500" + newData.getBackdropPath();

            Picasso.get()
                    .load(backDoprPoster_imageURL)
                    .placeholder(R.drawable.noimage_preview)
                    .fit()
                    .into(binding.backDropPosterProfile);
            binding.tvMovieTitle.setText(newData.getTitle());
            try {
                Custom_Utilities custom_utilities = Custom_Utilities.class.newInstance();
                binding.tvMovieLanguage.setText("Language: " + custom_utilities.getLanguageName(newData.getOriginalLanguage()));
                binding.tvMoveReleaseDate.setText("Release Date: " + custom_utilities.getFormattedDate(newData.getReleaseDate()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            if (newData.getStatus() == null || newData.getStatus() == "") {
                binding.tvMoveReleaseStatus.setText("Release Status: Not Available");
            } else {
                binding.tvMoveReleaseStatus.setText("Release Status: " + newData.getStatus());
            }
            binding.ratingBarMovieRatingsStars.setRating((float) newData.getVoteAverage().doubleValue() / 2);
            binding.ratingBarMovieRatingsNumber.setText(String.valueOf(newData.getVoteAverage() / 2));
            binding.tvMovieOverView.setText(newData.getOverview());
            binding.viewpagerProductionHouse.setAdapter(new MovieProfile_ProductionHouse_Adapter(Movie_Profile.this, newData.getProductionCompanies()));
        } else if (movieProfile_vm.getMovieProfileLiveData().getValue() != null) {
            Movie_Profile_FullModel data = movieProfile_vm.getMovieProfileLiveData().getValue();
            String backDoprPoster_imageURL = "https://image.tmdb.org/t/p/w500" + data.getBackdropPath();

            Picasso.get()
                    .load(backDoprPoster_imageURL)
                    .placeholder(R.drawable.noimage_preview)
                    .fit()
                    .into(binding.backDropPosterProfile);
            binding.tvMovieTitle.setText(data.getTitle());
            try {
                Custom_Utilities custom_utilities = Custom_Utilities.class.newInstance();
                binding.tvMovieLanguage.setText("Language: " + custom_utilities.getLanguageName(data.getOriginalLanguage()));
                binding.tvMoveReleaseDate.setText("Release Date: " + custom_utilities.getFormattedDate(data.getReleaseDate()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            if (data.getStatus() == null || data.getStatus() == "") {
                binding.tvMoveReleaseStatus.setText("Release Status: Not Available");
            } else {
                binding.tvMoveReleaseStatus.setText("Release Status: " + data.getStatus());
            }
            binding.ratingBarMovieRatingsStars.setRating((float) data.getVoteAverage().doubleValue() / 2);
            binding.ratingBarMovieRatingsNumber.setText(String.valueOf(data.getVoteAverage() / 2));
            binding.tvMovieOverView.setText(data.getOverview());

            binding.viewpagerProductionHouse.setAdapter(new MovieProfile_ProductionHouse_Adapter(Movie_Profile.this, data.getProductionCompanies()));
        }


    }

    //    Handling all the button click events inside this method
    private void buttonHandlingClicks() {
        binding.buttonCasts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Movie_Profile.this, Movie_casts_detail.class);
                intent.putExtra("MovieID", movieID);
                startActivity(intent);

            }
        });

        binding.buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Movie_Profile.this, Movie_Reviews_Activity.class);
                intent.putExtra("MovieID", movieID);
                startActivity(intent);

//                Toast.makeText(Movie_Profile.this, n + " Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    This is on item click listener for recyclerview
    @Override
    public void ViewOnItemClick(int position) {
        int newMovieID = movie_similar.get(position).getId();
        Intent intent = new Intent(getApplicationContext(), Movie_Profile.class);
        intent.putExtra("MovieID", newMovieID);
        startActivity(intent);
    }

    @Override
    public void ViewOnItemClickVP(int position) {

    }

    @Override
    public void ViewOnItemLongClick(int position) {

    }
}