package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;

import test.vtd.koreanguage.databinding.ActivityVideoPlayerBinding;

public class VideoPlayerActivity extends AppCompatActivity {
    ActivityVideoPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("videoUrl")) {
            String videoUrl = intent.getStringExtra("videoUrl");
            String extractedUrl = extractM3u8Url(videoUrl);

            if (extractedUrl != null) {
                playVideo(extractedUrl);
            } else {
                Toast.makeText(this, "Error extracting m3u8 URL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hàm để trích xuất đường dẫn m3u8 từ URL
    private String extractM3u8Url(String videoUrl) {
        Uri uri = Uri.parse(videoUrl);
        String query = uri.getQuery();

        if (query != null && query.contains("url=")) {
            String[] parts = query.split("url=");
            if (parts.length > 1) {
                return parts[1];
            }
        }

        return null;
    }

    private void playVideo(String videoUrl) {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(binding.videoView);
        binding.videoView.setMediaController(mediaController);
        binding.videoView.setVideoURI(Uri.parse(videoUrl));
        binding.videoView.start();
    }
}
