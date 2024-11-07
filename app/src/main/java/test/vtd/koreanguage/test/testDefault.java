package test.vtd.koreanguage.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import test.vtd.koreanguage.databinding.ActivityTestBinding;

public class testDefault extends AppCompatActivity {

    ActivityTestBinding binding;
    DatabaseReference databaseReference;
    List<newWordObject> newWords;
    int currentQuestionIndex = 0;
    String correctAnswer;
    int state = 0;
    newWordObject currentWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        newWords = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("NewWord");

        loadNewWords();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerAndMoveToNext();
            }
        });
    }

    private void randomState(){
        Random random = new Random();
        state = random.nextInt(2);
        if(state == 0)
            showQuestion();
        else
            showQuestion2();
    }

    private void loadNewWords() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        newWordObject model = dataSnapshot.getValue(newWordObject.class);
                        newWords.add(model);
                    }

                    // Shuffle the questions to randomize the order
                    Collections.shuffle(newWords);

                    // Display the first question
                    showQuestion();
                } else {
                    Toast.makeText(testDefault.this, "No words available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(testDefault.this, "Error loading words", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestion() {
        if (currentQuestionIndex < newWords.size()) {
            currentWord = newWords.get(currentQuestionIndex);

            // Get three incorrect answers from other words
            List<String> incorrectAnswers = getIncorrectAnswers(currentWord.getMean());
            // Set the correct answer
            correctAnswer = currentWord.getMean();

            // Shuffle all answers
            List<String> allAnswers = new ArrayList<>();
            allAnswers.add(correctAnswer);
            allAnswers.addAll(incorrectAnswers);
            Collections.shuffle(allAnswers);

            binding.radioGroup.setVisibility(View.VISIBLE);
            binding.edtAnswer.setVisibility(View.GONE);
            binding.btnSubmit.setVisibility(View.VISIBLE);
            // Set the question
            binding.tvQuestion.setText(currentWord.getNewWord());

            // Set answers to radio buttons
            binding.radioOption1.setText(allAnswers.get(0));
            binding.radioOption2.setText(allAnswers.get(1));
            binding.radioOption3.setText(allAnswers.get(2));
            binding.radioOption4.setText(allAnswers.get(3));

            // Clear the selected answer
            binding.radioGroup.clearCheck();
        } else {
            currentQuestionIndex = 0;
            Collections.shuffle(newWords);
            showQuestion();
        }
    }

    private void showQuestion2(){
        binding.edtAnswer.setText("");
        if(currentQuestionIndex < newWords.size()){
            currentWord = newWords.get(currentQuestionIndex);
            binding.radioGroup.setVisibility(View.GONE);
            binding.edtAnswer.setVisibility(View.VISIBLE);
            binding.tvQuestion.setText(currentWord.getPartWord() + ": " + currentWord.getMean());
        }else{
            currentQuestionIndex = 0;
            Collections.shuffle(newWords);
            showQuestion2();
        }
    }

    private List<String> getIncorrectAnswers(String correctAnswer) {
        List<String> incorrectAnswers = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            // Avoid duplicate incorrect answers
            String incorrectAnswer;
            do {
                int randomIndex = random.nextInt(newWords.size());
                incorrectAnswer = newWords.get(randomIndex).getMean();
            } while (incorrectAnswers.contains(incorrectAnswer) || incorrectAnswer.equals(correctAnswer));

            incorrectAnswers.add(incorrectAnswer);
        }

        return incorrectAnswers;
    }

    private void checkAnswerAndMoveToNext() {
        if(state == 0) {
            int selectedId = binding.radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedAnswer = selectedRadioButton.getText().toString();

                if (selectedAnswer.equals(correctAnswer)) {
                    // Correct answer
                    Toast.makeText(testDefault.this, "Correct!", Toast.LENGTH_SHORT).show();
                    // Move to the next question
                    currentQuestionIndex++;
                    randomState();
                } else {
                    // Incorrect answer
                    Toast.makeText(testDefault.this, "Incorrect!", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(testDefault.this, "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        } else if (state == 1) {
            if(!TextUtils.isEmpty(binding.edtAnswer.getText())) {
                if (binding.edtAnswer.getText().toString().toString().equals(currentWord.getNewWord())) {
                    Toast.makeText(testDefault.this, "Correct!", Toast.LENGTH_SHORT).show();
                    currentQuestionIndex++;
                    randomState();
                } else {
                    Toast.makeText(testDefault.this, "Incorrect!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(testDefault.this, "Please enter an answer", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
