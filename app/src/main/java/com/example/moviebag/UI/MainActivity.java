package com.example.moviebag.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.moviebag.Adapters.MainActivityRV_Adapter;
import com.example.moviebag.Adapters.MainActivityVP_Adapter;
import com.example.moviebag.MovieData_Models.Movie_Now_Playing;
import com.example.moviebag.MovieData_Models.Movie_Popular;
import com.example.moviebag.R;
import com.example.moviebag.Tools.Custom_Utilities;
import com.example.moviebag.Tools.Custom_ViewOnItemClickListener;
import com.example.moviebag.ViewModel.MovieNow_VM;
import com.example.moviebag.ViewModel.MoviePop_VM;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;
import com.example.moviebag.databinding.ActivityMainBinding;
import com.example.moviebag.databinding.HomeRecyclerviewlayoutPopularmoviesBinding;
import com.example.moviebag.databinding.HomeViewpagerlayoutPopularmoviesBinding;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Custom_ViewOnItemClickListener {

    private ActivityMainBinding binding;
    private LinearLayoutManager layoutManagerRV;
    private WormDotsIndicator dotsIndicator;

    private MoviePop_VM moviePop_vm;
    private MovieNow_VM movieNow_vm;


    private API_Interface api_interface;
    private List<Movie_Popular.Result> movie_populars = new ArrayList<>();

    private MainActivityRV_Adapter adapter;
    private MainActivityVP_Adapter vp_adapter;

    private List<Movie_Now_Playing.Result> now_playing_movies = new ArrayList<>();

    private int page = 1;
    private int totalPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        This will turn onn the page loading animation(The Shimmer Effect)
//        binding.shimmerFrameLayoutRV.setVisibility(View.VISIBLE);
//        binding.shimmerFrameLayoutRV.startShimmer();
        binding.shimmerFrameLayoutVP.setVisibility(View.VISIBLE);
        binding.shimmerFrameLayoutVP.startShimmer();

//          Setting up the Recycler in the layout
        binding.homeRecyclerViewID.setHasFixedSize(true);
        binding.homeRecyclerViewID.setItemViewCacheSize(20);
        layoutManagerRV = new LinearLayoutManager(this);
        binding.homeRecyclerViewID.setLayoutManager(layoutManagerRV);

//        Initializing viewModel and also observing here.
        initializeViewModelForNowPlayingMovies();
        initializeViewModelForPopularMovies();

    }

    private void initializeViewModelForNowPlayingMovies() {

        movieNow_vm = new ViewModelProvider(this).get(MovieNow_VM.class);
        movieNow_vm.getListLiveDataMovieNow().observe(this, results -> {
            List<Movie_Now_Playing.Result> newList = results;
            now_playing_movies.addAll(newList);
            updateAdapterForViewPager();
        });
    }

    private void initializeViewModelForPopularMovies() {

        moviePop_vm = new ViewModelProvider(this).get(MoviePop_VM.class);
        adapter = new MainActivityRV_Adapter(this, movie_populars, this);

//        Setting up scroll listener for nestedScrollView because recyclerViewOnScrollListener is inactive inside nestedScrollView
        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1)
                            .getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        if (!binding.nestedScrollView.canScrollVertically(1)) {

                            if (page <= totalPage) {
                                page++;
                                popularMovieShows();
                            }
                        }
                    }
                }
            }
        });
        popularMovieShows();
    }

    private void popularMovieShows() {
        moviePop_vm.getListLiveData(page).observe(this, results -> {
            if (results != null) {
                totalPage = results.getTotalPages();
                if (results.getResults() != null) {
                    binding.shimmerFrameLayoutRV.setVisibility(View.GONE);
                    movie_populars.addAll(results.getResults());
                    int oldCount = movie_populars.size();
                    binding.homeRecyclerViewID.setAdapter(adapter);
                    adapter.notifyItemRangeInserted(oldCount, movie_populars.size());
                }
            }
        });
    }

    //    Now Playing Movie section ViewPager Data binding
    private void updateAdapterForViewPager() {

//      Stops the page loading animation when response is received successfully
        binding.shimmerFrameLayoutVP.stopShimmer();
        binding.shimmerFrameLayoutVP.setVisibility(View.GONE);

        if (vp_adapter == null) {
            vp_adapter = new MainActivityVP_Adapter(this, now_playing_movies, this);
            binding.homeViewPagerID.setAdapter(vp_adapter);
            vp_adapter.notifyDataSetChanged();
        } else if (movieNow_vm.getListLiveDataMovieNow().getValue() != null) {
            vp_adapter = new MainActivityVP_Adapter(this, movieNow_vm.getListLiveDataMovieNow().getValue(), this);
            binding.homeViewPagerID.setAdapter(vp_adapter);
        } else {
            vp_adapter.notifyDataSetChanged();
        }

    }

    /**
     * ||=============Below all the method belongs to click listener Interface class to handle click listener events============||
     */
    //    Click Methods for the views
    @Override
    public void ViewOnItemClick(int position) {

        int movieID = this.movie_populars.get(position).getId();

        Intent intent = new Intent(MainActivity.this, Movie_Profile.class);
        intent.putExtra("MovieID", movieID);
        startActivity(intent);

    }

    @Override
    public void ViewOnItemClickVP(int position) {

        int movieID = this.now_playing_movies.get(position).getId();

        Intent intent = new Intent(MainActivity.this, Movie_Profile.class);
        intent.putExtra("MovieID", movieID);
        startActivity(intent);

    }

    @Override
    public void ViewOnItemLongClick(int position) {

    }
}