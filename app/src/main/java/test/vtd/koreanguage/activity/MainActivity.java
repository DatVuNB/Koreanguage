package test.vtd.koreanguage.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import test.vtd.koreanguage.R;

public class MainActivity extends AppCompatActivity {

    ImageView imv_dictionary, imv_test, imv_movie, imv_identify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        anhxa();

        imv_dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, translate.class);
                startActivity(intent);
            }
        });

        imv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, testMain.class);
                startActivity(intent);
            }
        });

        imv_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, movie.class);
                startActivity(intent);
            }
        });

        imv_identify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, identify.class);
                startActivity(intent);
            }
        });
    }

    private void anhxa() {
        imv_dictionary = findViewById(R.id.imv_dictionary);
        imv_test = findViewById(R.id.imv_test);
        imv_movie = findViewById(R.id.imv_movie);
        imv_identify = findViewById(R.id.imv_identify);
    }
}