package com.example.moviebag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.moviebag.MovieData_Models.Movie_Profile_FullModel;
import com.example.moviebag.MovieData_Models.Movie_Similar_Model;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;
import com.example.moviebag.databinding.ActivityMovieProfileBinding;
import com.example.moviebag.databinding.RecyclerviewMovieprofileLayoutBinding;
import com.example.moviebag.databinding.RecyclerviewSimilarmoviesLayoutBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

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

//        This will set data in the body
        callMovieProfileData();

//        This sets data in the similar movies section
        callSimilarMoviesData();

//        This method handles all the button clicks on this activity
        buttonHandlingClicks();
    }

    //    Calling all the data related to a particular movie that is clicked in previous activity.
    private void callMovieProfileData() {
        Call<Movie_Profile_FullModel> call = api_interface.getMovie_Profile(movieID);
        call.enqueue(new Callback<Movie_Profile_FullModel>() {
            @Override
            public void onResponse(Call<Movie_Profile_FullModel> call, Response<Movie_Profile_FullModel> response) {

                if (response.isSuccessful()){
                    binding.shimmer.setVisibility(View.GONE);
                    binding.shimmer.stopShimmer();
                    binding.appBarLayout.setVisibility(View.VISIBLE);
                    binding.profileLayoutMain.setVisibility(View.VISIBLE);
                }
                Log.e("Profile: ", "Data: " + response.body().getOriginalTitle());
                String backDoprPoster_imageURL = "https://image.tmdb.org/t/p/w500" + response.body().getBackdropPath();

                Picasso.get()
                        .load(backDoprPoster_imageURL)
                        .placeholder(R.drawable.noimage_preview)
                        .fit()
                        .into(binding.backDropPosterProfile);
                binding.tvMovieTitle.setText(response.body().getTitle());
                try {
                    Custom_Utilities custom_utilities = Custom_Utilities.class.newInstance();
                    binding.tvMovieLanguage.setText("Language: " + custom_utilities.getLanguageName(response.body().getOriginalLanguage()));
                    binding.tvMoveReleaseDate.setText("Release Date: " + custom_utilities.getFormattedDate(response.body().getReleaseDate()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

                if (response.body().getStatus() == null || response.body().getStatus() == "") {
                    binding.tvMoveReleaseStatus.setText("Release Status: Not Available");
                } else {
                    binding.tvMoveReleaseStatus.setText("Release Status: " + response.body().getStatus());
                }
                binding.ratingBarMovieRatingsStars.setRating((float) response.body().getVoteAverage().doubleValue() / 2);
                binding.ratingBarMovieRatingsNumber.setText(String.valueOf(response.body().getVoteAverage() / 2));
                binding.tvMovieOverView.setText(response.body().getOverview());

                binding.viewpagerProductionHouse.setAdapter(new MP_RecyclerView_Adapter(Movie_Profile.this, response.body().getProductionCompanies()));
            }

            @Override
            public void onFailure(Call<Movie_Profile_FullModel> call, Throwable t) {

                Log.e("Profile: ", "Failure: " + t);
            }
        });
    }

    private void callSimilarMoviesData() {
        Call<Movie_Similar_Model> call = api_interface.getSimilar_Movies(movieID);
        call.enqueue(new Callback<Movie_Similar_Model>() {
            @Override
            public void onResponse(Call<Movie_Similar_Model> call, Response<Movie_Similar_Model> response) {
                movie_similar = response.body().getResults();
                binding.similarMovieRV.setAdapter(new MP_RV_SimilarMovies_Adapter(Movie_Profile.this, movie_similar, Movie_Profile.this));

            }

            @Override
            public void onFailure(Call<Movie_Similar_Model> call, Throwable t) {

            }
        });
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

    /**
     * |======================From here the adapter classes started for all the view inside activity.======================|
     */

//    This is RecyclerView Adapter for Production House section.
    class MP_RecyclerView_Adapter extends RecyclerView.Adapter<MP_RecyclerView_Adapter.MyViewHolder> {

        private Context context;
        private List<Movie_Profile_FullModel.ProductionCompany> list_imageURL;

        public MP_RecyclerView_Adapter(Context context, List<Movie_Profile_FullModel.ProductionCompany> list_imageURL) {
            this.context = context;
            this.list_imageURL = list_imageURL;
        }

        @NonNull
        @NotNull
        @Override
        public Movie_Profile.MP_RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            RecyclerviewMovieprofileLayoutBinding layoutBinding = RecyclerviewMovieprofileLayoutBinding.inflate(layoutInflater, parent, false);
            return new MP_RecyclerView_Adapter.MyViewHolder(layoutBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull Movie_Profile.MP_RecyclerView_Adapter.MyViewHolder holder, int position) {
            String imageURL = "https://image.tmdb.org/t/p/w500" + list_imageURL.get(position).getLogoPath();
            String companyName = list_imageURL.get(position).getName();

            holder.BindViews(imageURL, companyName);
        }

        @Override
        public int getItemCount() {
            return list_imageURL.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            RecyclerviewMovieprofileLayoutBinding bindingMP;
//            ActivityViewpagerMovieprofileBinding b;

            public MyViewHolder(@NonNull @NotNull RecyclerviewMovieprofileLayoutBinding bindingMP) {
                super(bindingMP.getRoot());
                this.bindingMP = bindingMP;
            }

            void BindViews(String imageURL, String companyName) {
                Picasso.get()
                        .load(imageURL)
                        .placeholder(R.drawable.image_not_supported)
                        .centerInside()
                        .fit()
                        .into(bindingMP.imageViewProduction);

                bindingMP.textViewProduction.setText(companyName);

            }
        }
    }

    //    This is RecyclerView Adapter for Similar Movies section.
    class MP_RV_SimilarMovies_Adapter extends RecyclerView.Adapter<MP_RV_SimilarMovies_Adapter.MyViewHolder> {

        private Context context;
        private List<Movie_Similar_Model.Result> movieList;
        private Custom_ViewOnItemClickListener customViewOnItemClickListener;

        public MP_RV_SimilarMovies_Adapter(Context context, List<Movie_Similar_Model.Result> movieList, Custom_ViewOnItemClickListener custom_viewOnItemClickListener) {
            this.context = context;
            this.movieList = movieList;
            this.customViewOnItemClickListener = custom_viewOnItemClickListener;
        }

        @NonNull
        @NotNull
        @Override
        public MP_RV_SimilarMovies_Adapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

            RecyclerviewSimilarmoviesLayoutBinding binding = RecyclerviewSimilarmoviesLayoutBinding.inflate(LayoutInflater.from(context));

            return new MP_RV_SimilarMovies_Adapter.MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MP_RV_SimilarMovies_Adapter.MyViewHolder holder, int position) {
            String imageURL = "https://image.tmdb.org/t/p/w500" + movieList.get(position).getPosterPath();
            holder.BindViews(imageURL);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Passing the item clicked position to Activity class
                    customViewOnItemClickListener.ViewOnItemClick(position);
                }

            });

        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            RecyclerviewSimilarmoviesLayoutBinding bindingMPS;

            public MyViewHolder(@NonNull @NotNull RecyclerviewSimilarmoviesLayoutBinding bindingMPS) {
                super(bindingMPS.getRoot());
                this.bindingMPS = bindingMPS;
            }

            void BindViews(String imageURL) {
                Picasso.get()
                        .load(imageURL)
                        .placeholder(R.drawable.image_not_supported)
                        .fit()
                        .into(bindingMPS.imageViewSimilarMovieImage);
            }
        }
    }
}