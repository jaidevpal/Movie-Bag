package com.example.moviebag.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebag.MovieData_Models.Movie_Reviews;
import com.example.moviebag.R;
import com.example.moviebag.Tools.Custom_Utilities;
import com.example.moviebag.databinding.ReviewMovieLayoutBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieReview_Adapter extends RecyclerView.Adapter<MovieReview_Adapter.VH> {

    private Context context;
    private List<Movie_Reviews.Result> reviewList;

    public MovieReview_Adapter(Context context, List<Movie_Reviews.Result> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @NotNull
    @Override
    public MovieReview_Adapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        ReviewMovieLayoutBinding activityMovieReviewsBinding = ReviewMovieLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new VH(activityMovieReviewsBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull MovieReview_Adapter.VH holder, int position) {
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

    public class VH extends RecyclerView.ViewHolder {

        ReviewMovieLayoutBinding binding;

        public VH(@NonNull ReviewMovieLayoutBinding binding) {
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
