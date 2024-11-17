package test.vtd.koreanguage.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import test.vtd.koreanguage.R;

public class testMain extends AppCompatActivity {

    ImageView img_addNewWord, img_defaultWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        anhxa();
        img_defaultWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(testMain.this, testDefault.class);
                startActivity(intent);
            }
        });
        img_addNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(testMain.this, testAddNewWord.class);
                startActivity(intent);
            }
        });
    }

    private void anhxa() {
        img_defaultWord = findViewById(R.id.img_defaultWord);
        img_addNewWord = findViewById(R.id.img_addNewWord);
    }
}