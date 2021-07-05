package com.example.moviebag.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebag.MovieData_Models.Movie_Popular;
import com.example.moviebag.R;
import com.example.moviebag.Tools.Custom_Utilities;
import com.example.moviebag.Tools.Custom_ViewOnItemClickListener;
import com.example.moviebag.databinding.HomeRecyclerviewlayoutPopularmoviesBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivityRV_Adapter extends RecyclerView.Adapter<MainActivityRV_Adapter.VH> {

    private static final int VIEW_TYPE_ITEM = 11;
    private static final int VIEW_TYPE_LOADING = 12;
    private List<Movie_Popular.Result> moviePop;
    private Context context;
    private Custom_ViewOnItemClickListener custom_viewOnItemClickListener;

    public MainActivityRV_Adapter(Context context, List<Movie_Popular.Result> moviePop, Custom_ViewOnItemClickListener custom_viewOnItemClickListener) {

        this.context = context;
        this.moviePop = moviePop;
        this.custom_viewOnItemClickListener = custom_viewOnItemClickListener;

    }

    //        To check while loading new item inside RecyclerView
    @Override
    public int getItemViewType(int position) {

        if (moviePop.get(position) != null) {
            return VIEW_TYPE_ITEM;
        } else {
            return VIEW_TYPE_LOADING;
        }
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
//            Creating Views inside RecyclerView
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        HomeRecyclerviewlayoutPopularmoviesBinding binding = HomeRecyclerviewlayoutPopularmoviesBinding.inflate(layoutInflater, parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MainActivityRV_Adapter.VH holder, int position) {
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

    public class VH extends RecyclerView.ViewHolder {

        HomeRecyclerviewlayoutPopularmoviesBinding cardView_binding;

        public VH(@NonNull @NotNull HomeRecyclerviewlayoutPopularmoviesBinding cardView_binding) {
            super(cardView_binding.getRoot());
            this.cardView_binding = cardView_binding;
        }

        //            This method bind all the views and data together received from onBindViewHolder method.
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


    /**
     * =========================================================================**/
//    @NonNull
//    @NotNull
//    @Override
//    public MainActivityRV_Adapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull MainActivityRV_Adapter.VH holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public class VH extends RecyclerView.ViewHolder {
//        public VH(@NonNull @NotNull View itemView) {
//            super(itemView);
//        }
//    }
}
