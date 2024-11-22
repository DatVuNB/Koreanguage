package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import test.vtd.koreanguage.Model.NewWord;
import test.vtd.koreanguage.R;
import test.vtd.koreanguage.Adapter.NewWordAdapter;
import test.vtd.koreanguage.ViewModel.DetailTestAddNewWordViewModel;
import test.vtd.koreanguage.databinding.ActivityDetailTestAddNewWordBinding;

public class DetailTestAddNewWordActivity extends AppCompatActivity {
    private NewWordAdapter newWordAdapter;
    String subjectName;
    boolean nowState;
    ActivityDetailTestAddNewWordBinding binding;
    NewWord randomWord;
    DetailTestAddNewWordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTestAddNewWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subjectName = getIntent().getStringExtra("subjectName");

        initViewModel();
        initRecyclerView();

        binding.btnAddNewWord.setOnClickListener(v -> {
            if(!nowState)
                addNewWord();
            else
                Toast.makeText(DetailTestAddNewWordActivity.this,
                        "Do not add new word now!", Toast.LENGTH_SHORT).show();
        });
        binding.btnKiemTra.setOnClickListener(v -> {
            if(!nowState){
                viewModel.setButtonState(true);
                test();
            }else{
                viewModel.setButtonState(false);
            }
        });
    }

    private void initRecyclerView() {
        newWordAdapter = new NewWordAdapter(new NewWordAdapter.IClickItemNewWord() {
            @Override
            public void updateNewWord(NewWord newWord) {
                clickUpdateNewWord(newWord);
            }

            @Override
            public void deleteNewWord(NewWord newWord) {
                clickDeleteNewWord(newWord);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcvListNewWord.setLayoutManager(linearLayoutManager);
        binding.rcvListNewWord.setAdapter(newWordAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void initViewModel() {
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(DetailTestAddNewWordViewModel.class);
        viewModel.getNewWordBySubject(subjectName).observe(this, newWords ->
                newWordAdapter.setData(newWords));
        viewModel.getStatus().observe(this, status ->
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show());
        viewModel.getButtonState().observe(this, state -> {
            nowState = state;
            if (!state) {
                binding.rcvListNewWord.setVisibility(View.VISIBLE);
                binding.layoutTest.setVisibility(View.GONE);
                binding.btnKiemTra.setText("Start test");
                binding.btnKiemTra.setBackgroundResource(R.drawable.background_button_on);
            } else {
                binding.rcvListNewWord.setVisibility(View.GONE);
                binding.layoutTest.setVisibility(View.VISIBLE);
                binding.btnKiemTra.setText("Finish test");
                binding.btnKiemTra.setBackgroundResource(R.drawable.background_button_off);
            }
        });

        viewModel.loadButtonState();
    }

    private void test() {
        loadQuestion();
        binding.btnSubmit.setOnClickListener(v -> checkAnswerAndMoveToNext());
    }

    private void checkAnswerAndMoveToNext() {
        int selectedId = binding.radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            if (selectedAnswer.equals(randomWord.getMean())) {
                Toast.makeText(DetailTestAddNewWordActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                binding.radioGroup.clearCheck();
                loadQuestion();
            } else {
                Toast.makeText(DetailTestAddNewWordActivity.this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DetailTestAddNewWordActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuestion() {
        randomWord = viewModel.getRandomWord();
        String correctMean = randomWord.getMean();
        String newWord = randomWord.getNewWord();

        List<String> randomMeans = viewModel.getRandomMeansExcept(correctMean);

        randomMeans.add(correctMean);

        Collections.shuffle(randomMeans);

        binding.tvQuestion.setText(newWord);
        binding.radioOption1.setText(randomMeans.get(0));
        binding.radioOption2.setText(randomMeans.get(1));
        binding.radioOption3.setText(randomMeans.get(2));
        binding.radioOption4.setText(randomMeans.get(3));
    }

    private void addNewWord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new word");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(this);
        inputName.setHint("New word");
        layout.addView(inputName);
        final EditText inputMeaning = new EditText(this);
        inputMeaning.setHint("Mean");
        layout.addView(inputMeaning);
        builder.setView(layout);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String Str_newWord = inputName.getText().toString().trim();
            String Str_mean = inputMeaning.getText().toString().trim();
            if (!Str_newWord.isEmpty() && !Str_mean.isEmpty()) {
                NewWord newWord = new NewWord(Str_newWord, Str_mean,null,subjectName);
                viewModel.insertNewWord(newWord);
            } else {
                Toast.makeText(DetailTestAddNewWordActivity.this, "Please enter new word!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void clickDeleteNewWord(NewWord newWord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure to delete?");

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            viewModel.deleteNewWord(newWord);
            dialog.cancel();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void clickUpdateNewWord(NewWord newWord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update new word");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(this);
        inputName.setHint("New word");
        inputName.setText(newWord.getNewWord());
        layout.addView(inputName);
        final EditText inputMeaning = new EditText(this);
        inputMeaning.setHint("Mean");
        inputMeaning.setText(newWord.getMean());
        layout.addView(inputMeaning);
        builder.setView(layout);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String Str_newWord = inputName.getText().toString().trim();
            String Str_mean = inputMeaning.getText().toString().trim();
            if (!Str_newWord.isEmpty() && !Str_mean.isEmpty()) {
                newWord.setNewWord(Str_newWord);
                newWord.setMean(Str_mean);
                viewModel.updateNewWord(newWord);
            } else {
                Toast.makeText(DetailTestAddNewWordActivity.this, "Please enter new word!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}