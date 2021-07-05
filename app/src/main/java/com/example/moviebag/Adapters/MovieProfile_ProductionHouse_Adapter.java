package com.example.moviebag.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebag.MovieData_Models.Movie_Profile_FullModel;
import com.example.moviebag.R;
import com.example.moviebag.UI.Movie_Profile;
import com.example.moviebag.databinding.RecyclerviewMovieprofileLayoutBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieProfile_ProductionHouse_Adapter extends RecyclerView.Adapter<MovieProfile_ProductionHouse_Adapter.VH> {

    private Context context;
    private List<Movie_Profile_FullModel.ProductionCompany> list_imageURL;

    public MovieProfile_ProductionHouse_Adapter(Context context, List<Movie_Profile_FullModel.ProductionCompany> list_imageURL) {
        this.context = context;
        this.list_imageURL = list_imageURL;
    }

    @NonNull
    @NotNull
    @Override
    public MovieProfile_ProductionHouse_Adapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RecyclerviewMovieprofileLayoutBinding layoutBinding = RecyclerviewMovieprofileLayoutBinding.inflate(layoutInflater, parent, false);
        return new MovieProfile_ProductionHouse_Adapter.VH(layoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MovieProfile_ProductionHouse_Adapter.VH holder, int position) {
        String imageURL = "https://image.tmdb.org/t/p/w500" + list_imageURL.get(position).getLogoPath();
        String companyName = list_imageURL.get(position).getName();

        holder.BindViews(imageURL, companyName);
    }

    @Override
    public int getItemCount() {
        return list_imageURL.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        RecyclerviewMovieprofileLayoutBinding bindingMP;
//            ActivityViewpagerMovieprofileBinding b;

        public VH(@NonNull @NotNull RecyclerviewMovieprofileLayoutBinding bindingMP) {
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
