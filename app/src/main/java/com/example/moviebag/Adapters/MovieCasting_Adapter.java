package com.example.moviebag.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebag.MovieData_Models.Movie_Castings;
import com.example.moviebag.R;
import com.example.moviebag.databinding.CastsMovieLayoutBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieCasting_Adapter extends RecyclerView.Adapter<MovieCasting_Adapter.VH> {

    private Context context;
    private List<Movie_Castings.Cast> castsDetailsList;

    public MovieCasting_Adapter(Context context, List<Movie_Castings.Cast> castsDetailsList) {
        this.context = context;
        this.castsDetailsList = castsDetailsList;
    }

    @NonNull
    @NotNull
    @Override
    public MovieCasting_Adapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CastsMovieLayoutBinding castsMovieLayoutBinding = CastsMovieLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MovieCasting_Adapter.VH(castsMovieLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MovieCasting_Adapter.VH holder, int position) {

        String avatarURL = "https://image.tmdb.org/t/p/w500/" + castsDetailsList.get(position).getProfilePath();
        String realName = castsDetailsList.get(position).getOriginalName();
        String reelName = castsDetailsList.get(position).getCharacter();

        holder.BindViews(avatarURL, realName, reelName);
    }

    @Override
    public int getItemCount() {
        return castsDetailsList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        private CastsMovieLayoutBinding castsMovieLayoutBinding;

        public VH(@NonNull @NotNull CastsMovieLayoutBinding castsMovieLayoutBinding) {
            super(castsMovieLayoutBinding.getRoot());
            this.castsMovieLayoutBinding = castsMovieLayoutBinding;
        }

        void BindViews(String imageURL, String realName, String reelName) {

            Picasso.get()
                    .load(imageURL)
                    .placeholder(R.drawable.profile_image)
                    .centerCrop()
                    .fit()
                    .into(castsMovieLayoutBinding.ImageviewCastAvatar);

            castsMovieLayoutBinding.textViewCastRealName.setText(realName);
            castsMovieLayoutBinding.textViewCastReelName.setText(reelName);
        }
    }
}
