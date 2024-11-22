package test.vtd.koreanguage.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Locale;

import test.vtd.koreanguage.ViewModel.IdentifyViewModel;
import test.vtd.koreanguage.ViewModel.LoginViewModel;
import test.vtd.koreanguage.databinding.ActivityIdentifyBinding;

public class IdentifyActivity extends AppCompatActivity {
    private ActivityIdentifyBinding binding;
    private IdentifyViewModel identifyViewModel;
    private TextToSpeech textToSpeech;
    int imageSize = 224;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap image = (Bitmap) result.getData().getExtras().get("data");
                    if (image != null) {
                        int dimension = Math.min(image.getWidth(), image.getHeight());
                        image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                        binding.imageView.setImageBitmap(image);

                        image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                        identifyViewModel.classifyImage(image);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIdentifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        identifyViewModel = new ViewModelProvider(this).get(IdentifyViewModel.class);

        identifyViewModel.getClassificationResult().observe(this, result -> binding.result.setText(result));

        textToSpeech = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(this, "TextToSpeech initialization failed!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnTakePicture.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });

        binding.speak.setOnClickListener(view -> {
            String textToRead = binding.result.getText().toString();
            if (!TextUtils.isEmpty(textToRead)) {
                textToSpeech.setLanguage(Locale.KOREAN);
                textToSpeech.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }
}
