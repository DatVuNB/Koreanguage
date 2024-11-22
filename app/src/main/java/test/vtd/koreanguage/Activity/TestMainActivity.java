package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import test.vtd.koreanguage.databinding.ActivityTestMainBinding;

public class TestMainActivity extends AppCompatActivity {
    ActivityTestMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imgDefaultWord.setOnClickListener(v -> {
            Intent intent = new Intent(TestMainActivity.this, TestDefaultActivity.class);
            startActivity(intent);
        });
        binding.imgAddNewWord.setOnClickListener(v -> {
            Intent intent = new Intent(TestMainActivity.this, TestAddNewWordActivity.class);
            startActivity(intent);
        });
    }
}