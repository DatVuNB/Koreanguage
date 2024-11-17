package test.vtd.koreanguage.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.Locale;

import test.vtd.koreanguage.R;

public class translate extends AppCompatActivity {

    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceEdt;
    private ImageView micIV, img_speak, icPic;
    private Button translateBtn;
    private TextView translatedTV;
    String[] fromLanguages = {"From", "English","Korean", "VietNamese"};
    String[] toLanguages = {"To", "English","Korean", "VietNamese"};
    int fromLanguageCode, toLanguageCode = 0;
    String fromLanguage, toLanguage;
    private TextToSpeech textToSpeech;
    TextRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        anhxa();

        // Khởi tạo TextToSpeech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                } else {
                    Toast.makeText(translate.this, "TextToSpeech initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromLanguages[i]);
                fromLanguageCode_to_fromLanguage(fromLanguageCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguages[i]);
                toLanguageCode_to_toLanguage(toLanguageCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter toAdapter = new ArrayAdapter(this, R.layout.spinner_item, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");
                if(TextUtils.isEmpty(sourceEdt.getText().toString())){
                    Toast.makeText(translate.this, "Please enter your text to translate",
                            Toast.LENGTH_SHORT).show();
                }else if(fromLanguageCode==0){
                    Toast.makeText(translate.this, "Please select source language",
                            Toast.LENGTH_SHORT).show();
                }else if(toLanguageCode==0){
                    Toast.makeText(translate.this, "Please select the language to make translate",
                            Toast.LENGTH_SHORT).show();
                }else {
                    translateText(fromLanguageCode, toLanguageCode, sourceEdt.getText().toString());
                }
            }
        });

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromLanguageCode==0) {
                    Toast.makeText(translate.this, "Please select source language", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, fromLanguage);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to convert into text");
                    try{
                        startActivityForResult(intent,1);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(translate.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        icPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromLanguageCode==0) {
                    Toast.makeText(translate.this, "Please select source language", Toast.LENGTH_SHORT).show();
                }else{
                    // Launch camera if we have permission
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 2);
                    } else {
                        //Request camera permission if we don't have it.
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });
        img_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToRead = translatedTV.getText().toString();
                Locale locale = getLocaleFromLanguageCode(toLanguageCode);
                textToSpeech.setLanguage(locale);
                if (!TextUtils.isEmpty(textToRead)) {
                    textToSpeech.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }
    private void recognizeTextFromImage(Bitmap bitmap) {
        if(fromLanguageCode == 2) {
            recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
        }else{
            recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        }
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<Text> result = recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        String resultText = visionText.getText();
                        sourceEdt.setText(resultText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    // Phương thức để chuyển đổi languageCode sang Locale
    private Locale getLocaleFromLanguageCode(int languageCode) {
        switch (languageCode) {
            case 1: // English
                return Locale.ENGLISH;
            case 2: // Korean
                return Locale.KOREAN;
            case 3: // Vietnamese
                return new Locale("vi", "VN");
            default:
                return Locale.ENGLISH;
        }
    }

    private void anhxa() {
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        micIV = findViewById(R.id.icMic);
        translateBtn = findViewById(R.id.bt_Translate);
        translatedTV = findViewById(R.id.tv_Translated);
        sourceEdt = findViewById(R.id.edtSource);
        img_speak = findViewById(R.id.img_speak);
        icPic = findViewById(R.id.icPic);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK){
            if(data!=null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    sourceEdt.setText(result.get(0));
                }
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                recognizeTextFromImage(photo);
            }
        }
    }

    private void fromLanguageCode_to_fromLanguage(int fromLanguageCode){
        fromLanguage = "0";
        if(fromLanguageCode == 1){
            fromLanguage = "en";
        } else if (fromLanguageCode == 2) {
            fromLanguage = "ko";
        } else if (fromLanguageCode == 3) {
            fromLanguage = "vi";
        }
    }
    private void toLanguageCode_to_toLanguage(int toLanguageCode){
        toLanguage = "0";
        if(toLanguageCode == 1){
            toLanguage = "en";
        } else if (toLanguageCode == 2) {
            toLanguage = "ko";
        } else if (toLanguageCode == 3) {
            toLanguage = "vi";
        }
    }
    private void translateText(int fromLanguageCode, int toLanguageCode, String source){
        fromLanguageCode_to_fromLanguage(fromLanguageCode);
        toLanguageCode_to_toLanguage(toLanguageCode);
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(fromLanguage)
                .setTargetLanguage(toLanguage)
                .build();
        Translator translator = Translation.getClient(options);
        translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Task<String> result =translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translatedTV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(translate.this, "Fail to translate: "
                                + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(translate.this, "Fail to download language model: "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public int getLanguageCode(String language){
        int languageCode=0;
        switch (language){
            case "English":
                languageCode=1;
                break;
            case "Korean":
                languageCode=2;
                break;
            case "VietNamese":
                languageCode=3;
                break;

        }
        return languageCode;
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