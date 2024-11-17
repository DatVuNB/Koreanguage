package test.vtd.koreanguage.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import test.vtd.koreanguage.R;
import test.vtd.koreanguage.model.movieObject;
import test.vtd.koreanguage.activity.videoPlayerActivity;

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.ViewHolder> {
    private List<movieObject> movieList;

    public movieAdapter(List<movieObject> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(movieList.get(position).getTitle());
        Glide.with(holder.itemView.getContext())
                .load(movieList.get(position).getBannerUrl())
                .into(holder.bannerImageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView;
        TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.bannerImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        movieObject selectedMovie = movieList.get(position);
                        String videoUrl = selectedMovie.getVideoUrl();
                        playVideo(videoUrl);
                    }
                }
            });
        }
        private void playVideo(String videoUrl) {
            // Đưa videoUrl vào Intent và chuyển đến màn hình phát video
            Intent intent = new Intent(itemView.getContext(), videoPlayerActivity.class);
            intent.putExtra("videoUrl", videoUrl);
            itemView.getContext().startActivity(intent);
        }
    }
}

