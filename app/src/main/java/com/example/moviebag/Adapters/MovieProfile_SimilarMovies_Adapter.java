package com.example.moviebag.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebag.MovieData_Models.Movie_Similar_Model;
import com.example.moviebag.R;
import com.example.moviebag.Tools.Custom_ViewOnItemClickListener;
import com.example.moviebag.databinding.RecyclerviewSimilarmoviesLayoutBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieProfile_SimilarMovies_Adapter extends RecyclerView.Adapter<MovieProfile_SimilarMovies_Adapter.VH>{

    private Context context;
    private List<Movie_Similar_Model.Result> movieList;
    private Custom_ViewOnItemClickListener customViewOnItemClickListener;

    public MovieProfile_SimilarMovies_Adapter(Context context, List<Movie_Similar_Model.Result> movieList, Custom_ViewOnItemClickListener custom_viewOnItemClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.customViewOnItemClickListener = custom_viewOnItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MovieProfile_SimilarMovies_Adapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        RecyclerviewSimilarmoviesLayoutBinding binding = RecyclerviewSimilarmoviesLayoutBinding.inflate(LayoutInflater.from(context));

        return new MovieProfile_SimilarMovies_Adapter.VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MovieProfile_SimilarMovies_Adapter.VH holder, int position) {
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

    public class VH extends RecyclerView.ViewHolder {

        RecyclerviewSimilarmoviesLayoutBinding bindingMPS;

        public VH(@NonNull @NotNull RecyclerviewSimilarmoviesLayoutBinding bindingMPS) {
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
