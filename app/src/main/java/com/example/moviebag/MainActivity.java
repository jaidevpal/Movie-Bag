package com.example.moviebag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
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

import com.example.moviebag.MovieData_Models.Movie_Now_Playing;
import com.example.moviebag.MovieData_Models.Movie_Popular;
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

    private API_Interface api_interface;
    private List<Movie_Popular.Result> movie_populars = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    private List<Movie_Now_Playing.Result> now_playing_movies = new ArrayList<>();

    private Boolean isScrolling = false;
    private int page = 1;
    private int totalPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        This will turn onn the page loading animation(The Shimmer Effect)
        binding.shimmerFrameLayoutRV.setVisibility(View.VISIBLE);
        binding.shimmerFrameLayoutRV.startShimmer();
        binding.shimmerFrameLayoutVP.setVisibility(View.VISIBLE);
        binding.shimmerFrameLayoutVP.startShimmer();

//          Setting up the Recycler in the layout
        binding.homeRecyclerViewID.setHasFixedSize(true);
        binding.homeRecyclerViewID.setItemViewCacheSize(20);
        layoutManagerRV = new LinearLayoutManager(this);
        binding.homeRecyclerViewID.setLayoutManager(layoutManagerRV);

//        Calling data using API and passing data to views on Viewpager2.
        callDataVP2();

//        Calling Data using API and passing on data to Views on RecyclerView.
        callDataRV();

//        Adding Scroll listener for RecyclerView from NestedScrollView,which will help us know the Scrolling state and item counts
        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        callAdditionalData();
                    }
                }
            }
        });

    }

    private void callDataVP2() {
        api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Now_Playing> call = api_interface.getNow_Playing_Movies();
        call.enqueue(new Callback<Movie_Now_Playing>() {
            @Override
            public void onResponse(Call<Movie_Now_Playing> call, Response<Movie_Now_Playing> response) {
                if (response.isSuccessful()) {

//                    Stops the page loading animation when response is recieved successfully
                    binding.shimmerFrameLayoutVP.stopShimmer();
                    binding.shimmerFrameLayoutVP.setVisibility(View.GONE);

//                    Sets the Adapter to ViewPager2 and passes the response to the Adapter for further binding
                    now_playing_movies = response.body().getResults();
                    binding.homeViewPagerID.setAdapter(new ViewPager_Adapter(MainActivity.this, now_playing_movies, MainActivity.this));

//                    This enables the page indicator
                    binding.wormDotIndicator.setViewPager2(binding.homeViewPagerID);

                }
            }

            @Override
            public void onFailure(Call<Movie_Now_Playing> call, Throwable t) {
                Log.e("MainActivity nowMovie: ", "OnFailureResponse: " + t);
            }
        });
    }

    //    This method is called from onCreate method for the first time when there is no data in the view.
    private void callDataRV() {

        api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Popular> call = api_interface.getPopular_Movies(page);
        call.enqueue(new Callback<Movie_Popular>() {
            @Override
            public void onResponse(Call<Movie_Popular> call, Response<Movie_Popular> response) {

                if (response.isSuccessful()) {
                    binding.shimmerFrameLayoutRV.setVisibility(View.GONE);

                    totalPage = response.body().getTotalPages();

                    Log.e("TAG", "Total pages: " + totalPage + " || onResponseSuccess: " + response.body().getResults());

                    movie_populars = response.body().getResults();
                    adapter = new RecyclerViewAdapter(getApplicationContext(), movie_populars, MainActivity.this);
                    binding.homeRecyclerViewID.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Movie_Popular> call, Throwable t) {

                Log.e("Error", "Response Failure: " + t);

            }
        });
    }

    //    This method is called when there is already data available and want to add further through pagination(included).
    private void callAdditionalData() {

        binding.shimmerFrameLayoutRV.setVisibility(View.VISIBLE);
        binding.shimmerFrameLayoutRV.startShimmer();

//        First we are checking the current page number and then increasing it with 1
        if (page <= totalPage) {
            page++;
//            Here if page number exceeds the limit of totalPages then pagination will not be executed.
            if (page > totalPage) {
                Toast.makeText(this, "Sorry, No more Item available!", Toast.LENGTH_SHORT).show();
            } else {
                api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
                Call<Movie_Popular> call = api_interface.getPopular_Movies(page);
                call.enqueue(new Callback<Movie_Popular>() {
                    @Override
                    public void onResponse(Call<Movie_Popular> call, Response<Movie_Popular> response) {

                        if (response.isSuccessful()) {
                            binding.shimmerFrameLayoutRV.setVisibility(View.GONE);
                            binding.shimmerFrameLayoutRV.stopShimmer();
                        }
                        totalPage = response.body().getTotalPages();

//                        Checking the data which is received currently in the terminal.
                        Log.e("TAG", "Total pages: " + totalPage + " || Current page: " + page + " || onResponseSuccess: " + response.body().getResults());

                        movie_populars.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Page No." + page + " loaded!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Movie_Popular> call, Throwable t) {

                        Log.e("Error", "Response Failure: " + t);

                    }
                });

            }
        }


    }

    //    Click Methods for the views
    @Override
    public void ViewOnItemClick(int position) {

        int movieID = this.movie_populars.get(position).getId();
        String title = this.movie_populars.get(position).getOriginalTitle();

        Intent intent = new Intent(MainActivity.this, Movie_Profile.class);
        intent.putExtra("MovieID", movieID);
        startActivity(intent);

    }

    @Override
    public void ViewOnItemClickVP(int position) {

        int movieID = this.now_playing_movies.get(position).getId();
        String title = this.now_playing_movies.get(position).getOriginalTitle();

        Intent intent = new Intent(MainActivity.this, Movie_Profile.class);
        intent.putExtra("MovieID", movieID);
        startActivity(intent);

    }

    @Override
    public void ViewOnItemLongClick(int position) {

    }


    /**
     * |======================From here the adapter classes started for all the view inside activity.======================|
     */

    //    This class is the Adapter for viewpager which will bind and unbind the data to it.
    class ViewPager_Adapter extends RecyclerView.Adapter<ViewPager_Adapter.MyViewPageHolder> {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Movie_Now_Playing.Result> nowPlayingMovies;

        private Custom_ViewOnItemClickListener custom_viewOnItemClickListenerVP;

        public ViewPager_Adapter(Context context, List<Movie_Now_Playing.Result> movienowplayings, Custom_ViewOnItemClickListener custom_viewOnItemClickListener) {
            this.context = context;
            this.nowPlayingMovies = movienowplayings;
            this.custom_viewOnItemClickListenerVP = custom_viewOnItemClickListener;
        }

        @NonNull
        @NotNull
        @Override
        public ViewPager_Adapter.MyViewPageHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            layoutInflater = LayoutInflater.from(context);
            HomeViewpagerlayoutPopularmoviesBinding b = HomeViewpagerlayoutPopularmoviesBinding.inflate(layoutInflater, parent, false);
            return new ViewPager_Adapter.MyViewPageHolder(b);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewPager_Adapter.MyViewPageHolder holder, int position) {
            String image_URL, title, releaseDate;

            image_URL = "https://image.tmdb.org/t/p/w500" + nowPlayingMovies.get(position).getBackdropPath();
            title = now_playing_movies.get(position).getTitle();
            releaseDate = now_playing_movies.get(position).getReleaseDate();
            holder.BindViewPagerViews(image_URL, title, releaseDate);
        }

        @Override
        public int getItemCount() {
            return nowPlayingMovies.size();
        }

        public class MyViewPageHolder extends RecyclerView.ViewHolder {

            private HomeViewpagerlayoutPopularmoviesBinding bindingVP;

            public MyViewPageHolder(@NonNull @NotNull HomeViewpagerlayoutPopularmoviesBinding bindingVP) {
                super(bindingVP.getRoot());
                this.bindingVP = bindingVP;
            }

            public void BindViewPagerViews(String image_URL, String title, String releaseDate) {
//                bindingVP.imageViewMovieBackDropViewPager.setImageResource(image);
                Picasso.get()
                        .load(image_URL)
                        .placeholder(R.drawable.noimage_preview)
                        .centerCrop()
                        .fit().into(bindingVP.imageViewMovieBackDropViewPager);
                bindingVP.tvMovieTitleViewPager.setText(title);
                Custom_Utilities custom_utilities = new Custom_Utilities();
                if (releaseDate == null || releaseDate == "") {
                    bindingVP.tvMovieReleaseDateViewPager.setText("Not Available");
                } else {
                    bindingVP.tvMovieReleaseDateViewPager.setText(custom_utilities.getFormattedDate(releaseDate));
                }

                bindingVP.imageViewMovieBackDropViewPager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custom_viewOnItemClickListenerVP.ViewOnItemClickVP(getAdapterPosition());
                    }
                });
            }
        }
    }


    //    This is an Adapter for recyclerView to Bind views with data
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

        private List<Movie_Popular.Result> moviePop;
        private Context context;
        private Custom_ViewOnItemClickListener custom_viewOnItemClickListener;

        public RecyclerViewAdapter(Context context, List<Movie_Popular.Result> moviePop, Custom_ViewOnItemClickListener custom_viewOnItemClickListener) {

            this.context = context;
            this.moviePop = moviePop;
            this.custom_viewOnItemClickListener = custom_viewOnItemClickListener;

        }

        @NonNull
        @org.jetbrains.annotations.NotNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
//            Creating Views inside RecyclerView
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            HomeRecyclerviewlayoutPopularmoviesBinding binding = HomeRecyclerviewlayoutPopularmoviesBinding.inflate(layoutInflater, parent, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MainActivity.RecyclerViewAdapter.MyViewHolder holder, int position) {
//              Binding data with RecyclerView's views
            String poster_URL, title, languageCode, releaseDate;
            float ratings;

            poster_URL = "https://image.tmdb.org/t/p/w500" + moviePop.get(position).getPosterPath();
            title = moviePop.get(position).getTitle();
            languageCode = moviePop.get(position).getOriginalLanguage();
            releaseDate = moviePop.get(position).getReleaseDate();
            ratings = moviePop.get(position).getVoteAverage().floatValue();

            holder.bindViews(poster_URL, title, languageCode, releaseDate, ratings);
        }

        @Override
        public int getItemCount() {

            return moviePop.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            HomeRecyclerviewlayoutPopularmoviesBinding cardView_binding;

            public MyViewHolder(@NonNull @NotNull HomeRecyclerviewlayoutPopularmoviesBinding cardView_binding) {
                super(cardView_binding.getRoot());
                this.cardView_binding = cardView_binding;
            }

            public void bindViews(String image, String movie_Title, String movie_LanguageCode, String movie_releaseDate, float rating) {

                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.noimage_preview)
                        .fit()
                        .into(cardView_binding.imageViewMoviePoster);
                cardView_binding.textViewMovieTitle.setText(movie_Title);
                try {
                    Custom_Utilities custom_utilities = Custom_Utilities.class.newInstance();
                    if (movie_LanguageCode == null || movie_LanguageCode == "") {
                        cardView_binding.textViewMovieLanguage.setText("Language: Not Available");
                    } else {
                        cardView_binding.textViewMovieLanguage.setText("Language: " + custom_utilities.getLanguageName(movie_LanguageCode));
                    }
                    if (movie_releaseDate == null || movie_releaseDate == "") {
                        cardView_binding.textViewMovieReleaseDate.setText("Release Date: Not Available");
                    } else {
                        cardView_binding.textViewMovieReleaseDate.setText("Release Date: " + custom_utilities.getFormattedDate(movie_releaseDate));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                cardView_binding.ratingBarMovieRating.setRating(rating / 2);
                cardView_binding.ratingInNumber.setText(String.valueOf(rating / 2));

//                Setting click listener for RecyclerView Items
                cardView_binding.cardViewPopularMovies.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custom_viewOnItemClickListener.ViewOnItemClick(getAdapterPosition());
                    }
                });

            }
        }
    }


}