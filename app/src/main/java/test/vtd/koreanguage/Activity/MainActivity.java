package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import test.vtd.koreanguage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imvDictionary.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
            startActivity(intent);
        });

        binding.imvTest.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TestMainActivity.class);
            startActivity(intent);
        });

        binding.imvMovie.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            startActivity(intent);
        });

        binding.imvIdentify.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, IdentifyActivity.class);
            startActivity(intent);
        });
    }
}