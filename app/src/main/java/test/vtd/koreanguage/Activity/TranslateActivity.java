package test.vtd.koreanguage.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import test.vtd.koreanguage.R;
import test.vtd.koreanguage.databinding.ActivityTranslateBinding;
import test.vtd.koreanguage.ViewModel.TranslateViewModel;

public class TranslateActivity extends AppCompatActivity {
    ActivityTranslateBinding binding;
    String[] fromLanguages = {"From", "English", "Korean", "VietNamese"};
    String[] toLanguages = {"To", "English", "Korean", "VietNamese"};
    private TranslateViewModel viewModel;
    private TextToSpeech textToSpeech;
    private ActivityResultLauncher<Intent> speechLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranslateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new TranslateViewModel(this);
        textToSpeech = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(TranslateActivity.this, "TextToSpeech initialization failed!", Toast.LENGTH_SHORT).show();
            }
        });

        initializeSpinners();

        initializeLaunchers();

        setButtonListeners();
    }

    private void initializeSpinners() {
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.toSpinner.setAdapter(toAdapter);

        binding.fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.setFromLanguageCode(fromLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.setToLanguageCode(toLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initializeLaunchers() {
        speechLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<String> speechResults = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (speechResults != null && !speechResults.isEmpty()) {
                            binding.edtSource.setText(speechResults.get(0));
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap photo = (Bitmap) Objects.requireNonNull(result.getData().getExtras()).get("data");
                        if (photo != null) {
                            viewModel.recognizeTextFromImage(photo, binding.edtSource::setText);
                        }
                    }
                });
    }

    private void setButtonListeners() {
        binding.btTranslate.setOnClickListener(view -> {
            String sourceText = binding.edtSource.getText().toString();
            if (TextUtils.isEmpty(sourceText)) {
                Toast.makeText(this, "Please enter text to translate", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.translateText(sourceText, binding.tvTranslated::setText);
        });

        binding.icMic.setOnClickListener(view -> viewModel.launchSpeechRecognizer(speechLauncher));

        binding.icPic.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });

        binding.imgSpeak.setOnClickListener(view -> {
            String textToRead = binding.tvTranslated.getText().toString();
            viewModel.speakText(textToRead, textToSpeech);
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
