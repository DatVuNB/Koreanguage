package test.vtd.koreanguage.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.Locale;

public class TranslateViewModel {
    private final Context context;
    private int fromLanguageCode = 0, toLanguageCode = 0;

    public TranslateViewModel(Context context) {
        this.context = context;
    }

    public void setFromLanguageCode(String language) {
        this.fromLanguageCode = getLanguageCode(language);
    }

    public void setToLanguageCode(String language) {
        this.toLanguageCode = getLanguageCode(language);
    }

    public void translateText(String sourceText, ResultCallback<String> callback) {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(getLanguageTag(fromLanguageCode))
                .setTargetLanguage(getLanguageTag(toLanguageCode))
                .build();

        Translator translator = Translation.getClient(options);
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> translator.translate(sourceText)
                        .addOnSuccessListener(callback::onResult)
                        .addOnFailureListener(e -> Toast.makeText(context, "Translation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(context, "Model download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void recognizeTextFromImage(Bitmap bitmap, ResultCallback<String> callback) {
        TextRecognizer recognizer = fromLanguageCode == 2
                ? TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build())
                : TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        InputImage image = InputImage.fromBitmap(bitmap, 0);
        recognizer.process(image)
                .addOnSuccessListener(visionText -> callback.onResult(visionText.getText()));
    }

    public void launchSpeechRecognizer(ActivityResultLauncher<Intent> launcher) {
        if (fromLanguageCode == 0) {
            Toast.makeText(context, "Please select source language", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getLanguageTag(fromLanguageCode));
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        launcher.launch(intent);
    }

    public void speakText(String text, TextToSpeech textToSpeech) {
        if (text == null || text.isEmpty()) {
            return;
        }
        textToSpeech.setLanguage(getLocaleFromLanguageCode(toLanguageCode));
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private String getLanguageTag(int code) {
        switch (code) {
            case 1:
                return "en";
            case 2:
                return "ko";
            case 3:
                return "vi";
        }
        return "en";
    }

    private Locale getLocaleFromLanguageCode(int code) {
        switch (code) {
            case 2:
                return Locale.KOREAN;
            case 3:
                return new Locale("vi");
            default:
                return Locale.ENGLISH;
        }
    }

    private int getLanguageCode(String language) {
        switch (language) {
            case "English":
                return 1;
            case "Korean":
                return 2;
            case "VietNamese":
                return 3;
            default:
                return 0;
        }
    }

    public interface ResultCallback<T> {
        void onResult(T result);
    }
}
