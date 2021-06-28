package com.example.moviebag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.moviebag.MovieData_Models.Movie_Castings;
import com.example.moviebag.api.API_Interface;
import com.example.moviebag.api.RetrofitClientAPI;
import com.example.moviebag.databinding.ActivityMovieCastsDetailBinding;
import com.example.moviebag.databinding.CastsMovieLayoutBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Movie_casts_detail extends AppCompatActivity {

    private ActivityMovieCastsDetailBinding binding;
    private GridLayoutManager gridLayoutManager;

    private API_Interface api_interface;

    private static int movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieCastsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie_casts_detail.this.finish();
            }
        });
        gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerViewCasts.setLayoutManager(gridLayoutManager);
        binding.recyclerViewCasts.setHasFixedSize(true);

        movieID = getIntent().getIntExtra("MovieID", 0);

        api_interface = RetrofitClientAPI.getClient().create(API_Interface.class);

        CallMovieCastsData();


    }

    private void CallMovieCastsData() {
        Call<Movie_Castings> call = api_interface.getMoviesCastings(movieID);
        call.enqueue(new Callback<Movie_Castings>() {
            @Override
            public void onResponse(Call<Movie_Castings> call, Response<Movie_Castings> response) {

                if (response.isSuccessful()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerViewCasts.setAdapter(new RecyclerView_Adapter(Movie_casts_detail.this, response.body().getCast()));
                }
            }

            @Override
            public void onFailure(Call<Movie_Castings> call, Throwable t) {

            }
        });
    }

    class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.MyViewHolder> {

        private Context context;
        private List<Movie_Castings.Cast> castsDetailsList;

        public RecyclerView_Adapter(Context context, List<Movie_Castings.Cast> castsDetailsList) {
            this.context = context;
            this.castsDetailsList = castsDetailsList;
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerView_Adapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            CastsMovieLayoutBinding castsMovieLayoutBinding = CastsMovieLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
            return new RecyclerView_Adapter.MyViewHolder(castsMovieLayoutBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView_Adapter.MyViewHolder holder, int position) {

            String avatarURL = "https://image.tmdb.org/t/p/w500/" + castsDetailsList.get(position).getProfilePath();
            String realName = castsDetailsList.get(position).getOriginalName();
            String reelName = castsDetailsList.get(position).getCharacter();

            holder.BindViews(avatarURL, realName, reelName);
        }

        @Override
        public int getItemCount() {
            return castsDetailsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CastsMovieLayoutBinding castsMovieLayoutBinding;

            public MyViewHolder(@NonNull @NotNull CastsMovieLayoutBinding castsMovieLayoutBinding) {
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
}