package com.example.moviebag.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebag.MovieData_Models.Movie_Now_Playing;
import com.example.moviebag.R;
import com.example.moviebag.Tools.Custom_Utilities;
import com.example.moviebag.Tools.Custom_ViewOnItemClickListener;
import com.example.moviebag.UI.MainActivity;
import com.example.moviebag.databinding.HomeViewpagerlayoutPopularmoviesBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivityVP_Adapter extends RecyclerView.Adapter<MainActivityVP_Adapter.VH> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Movie_Now_Playing.Result> nowPlayingMovies;

    private Custom_ViewOnItemClickListener custom_viewOnItemClickListenerVP;

    public MainActivityVP_Adapter(Context context, List<Movie_Now_Playing.Result> movienowplayings, Custom_ViewOnItemClickListener custom_viewOnItemClickListener) {
        this.context = context;
        this.nowPlayingMovies = movienowplayings;
        this.custom_viewOnItemClickListenerVP = custom_viewOnItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MainActivityVP_Adapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        HomeViewpagerlayoutPopularmoviesBinding b = HomeViewpagerlayoutPopularmoviesBinding.inflate(layoutInflater, parent, false);
        return new MainActivityVP_Adapter.VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainActivityVP_Adapter.VH holder, int position) {
        String image_URL, title, releaseDate;

        image_URL = "https://image.tmdb.org/t/p/w500" + nowPlayingMovies.get(position).getBackdropPath();
        title = nowPlayingMovies.get(position).getTitle();
        releaseDate = nowPlayingMovies.get(position).getReleaseDate();
        holder.BindViewPagerViews(image_URL, title, releaseDate);
    }

    @Override
    public int getItemCount() {
        return nowPlayingMovies.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        private HomeViewpagerlayoutPopularmoviesBinding bindingVP;

        public VH(@NonNull @NotNull HomeViewpagerlayoutPopularmoviesBinding bindingVP) {
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


    /**
     * =================================================================
     * @param parent
     * @param viewType
     * @return
     */
//    @NonNull
//    @NotNull
//    @Override
//    public MainActivityVP_Adapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull MainActivityVP_Adapter.VH holder, int position) {
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
