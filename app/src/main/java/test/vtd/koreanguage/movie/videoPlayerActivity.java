package test.vtd.koreanguage.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.Toast;

import test.vtd.koreanguage.R;

public class videoPlayerActivity extends AppCompatActivity {
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("videoUrl")) {
            String videoUrl = intent.getStringExtra("videoUrl");
            String extractedUrl = extractM3u8Url(videoUrl);

            if (extractedUrl != null) {
                playVideo(extractedUrl);
            } else {
                // Xử lý lỗi trích xuất đường dẫn m3u8
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
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.start();
    }
}
