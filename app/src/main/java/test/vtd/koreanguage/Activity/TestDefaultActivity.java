package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import test.vtd.koreanguage.ViewModel.TestDefaultViewModel;
import test.vtd.koreanguage.databinding.ActivityTestBinding;

public class TestDefaultActivity extends AppCompatActivity {

    private ActivityTestBinding binding;
    private TestDefaultViewModel testDefaultViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        testDefaultViewModel = new ViewModelProvider(this).get(TestDefaultViewModel.class);

        testDefaultViewModel.loadNewWords();
        observeViewModel();

        binding.btnSubmit.setOnClickListener(view -> checkAnswer());
    }

    private void observeViewModel() {
        testDefaultViewModel.getQuestionState().observe(this, questionState -> {
            if (questionState == 0) {
                showQuestion();
            } else {
                showQuestion2();
            }
        });

        testDefaultViewModel.getCurrentQuestion().observe(this, question -> {
            if (question != null) {
                binding.tvQuestion.setText(question.getDisplayQuestion());
                if (testDefaultViewModel.getQuestionState().getValue() == 0) {
                    setupOptions(question);
                }
            }
        });

        testDefaultViewModel.getFeedbackMessage().observe(this, message -> {
            if (!TextUtils.isEmpty(message)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupOptions(TestDefaultViewModel.Question question) {
        binding.radioOption1.setText(question.getOptions().get(0));
        binding.radioOption2.setText(question.getOptions().get(1));
        binding.radioOption3.setText(question.getOptions().get(2));
        binding.radioOption4.setText(question.getOptions().get(3));
    }

    private void showQuestion() {
        binding.radioGroup.setVisibility(View.VISIBLE);
        binding.edtAnswer.setVisibility(View.GONE);
        binding.radioGroup.clearCheck();
    }

    private void showQuestion2() {
        binding.radioGroup.setVisibility(View.GONE);
        binding.edtAnswer.setVisibility(View.VISIBLE);
        binding.edtAnswer.setText("");
    }

    private void checkAnswer() {
        if (testDefaultViewModel.getQuestionState().getValue() == 0) {
            int selectedId = binding.radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                testDefaultViewModel.submitAnswer(selectedRadioButton.getText().toString());
            } else {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        } else {
            String answer = binding.edtAnswer.getText().toString();
            testDefaultViewModel.submitAnswer(answer);
        }
    }
}
