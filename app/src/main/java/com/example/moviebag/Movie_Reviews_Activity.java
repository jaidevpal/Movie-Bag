package com.example.moviebag;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviebag.MovieData_Models.Movie_Reviews;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;
import com.example.moviebag.databinding.ActivityMovieReviewsBinding;
import com.example.moviebag.databinding.ReviewMovieLayoutBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Movie_Reviews_Activity extends AppCompatActivity {

    private ActivityMovieReviewsBinding binding;
    private LinearLayoutManager linearLayoutManager;
    private List<Movie_Reviews> reviewList;
    private API_Interface api_interface;

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

        callMovieReviewsData();
    }

    private void callMovieReviewsData() {
        api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);
        Call<Movie_Reviews> call = api_interface.getMoviesReviews(movieID);
        call.enqueue(new Callback<Movie_Reviews>() {
            @Override
            public void onResponse(Call<Movie_Reviews> call, Response<Movie_Reviews> response) {
                if (response.isSuccessful()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerViewReviews.setAdapter(new RecyclerViewAdapter(Movie_Reviews_Activity.this, response.body().getResults()));
                }
            }

            @Override
            public void onFailure(Call<Movie_Reviews> call, Throwable t) {

            }
        });
    }

    /**
     * |======================From here the adapter classes started for all the view inside activity.======================|
     */
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

        private Context context;
        private List<Movie_Reviews.Result> reviewList;

        public RecyclerViewAdapter(Context context, List<Movie_Reviews.Result> reviewList) {
            this.context = context;
            this.reviewList = reviewList;
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

            ReviewMovieLayoutBinding activityMovieReviewsBinding = ReviewMovieLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
            return new RecyclerViewAdapter.MyViewHolder(activityMovieReviewsBinding);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter.MyViewHolder holder, int position) {
            String imageURL, reviewerName, reviewDate, reviewContent;
            float rating = 0;

            imageURL = "https://image.tmdb.org/t/p/w500/" + reviewList.get(position).getAuthorDetails().getAvatarPath();

            reviewerName = reviewList.get(position).getAuthorDetails().getUsername();
            reviewDate = reviewList.get(position).getCreatedAt();
            reviewContent = reviewList.get(position).getContent();
            if (reviewList.get(position).getAuthorDetails().getRating() == null || reviewList.get(position).getAuthorDetails().getRating() == 0) {
                rating = 0.0f;
            } else {
                rating = reviewList.get(position).getAuthorDetails().getRating().floatValue();
            }

            holder.BindViews(imageURL, reviewerName, reviewDate, rating, reviewContent);

        }

        @Override
        public int getItemCount() {
            return reviewList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ReviewMovieLayoutBinding binding;

            public MyViewHolder(@NonNull ReviewMovieLayoutBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void BindViews(String imageURL, String reviewerName, String reviewDate, float ratings, String reviewContent) {
                Picasso.get()
                        .load(imageURL)
                        .placeholder(R.drawable.no_profile_image_placeholder)
                        .centerCrop()
                        .fit().into(binding.imageViewReviewAuthor);
                binding.textviewReviewAuthor.setText(reviewerName);
                try {
                    Custom_Utilities custom_utilities = Custom_Utilities.class.newInstance();
                    binding.textviewReviewDate.setText("Created On: " + custom_utilities.getFormattedDateAndTime(reviewDate));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                binding.ratingBarMovieRating.setRating(ratings / 2);
                binding.ratingInNumber.setText(String.valueOf(ratings / 2));
                binding.textviewReviewBody.setText(reviewContent);

            }
        }
    }
}