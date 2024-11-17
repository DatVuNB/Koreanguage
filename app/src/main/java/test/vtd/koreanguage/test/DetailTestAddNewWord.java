package test.vtd.koreanguage.test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import test.vtd.koreanguage.R;
import test.vtd.koreanguage.databinding.ActivityDetailTestAddNewWordBinding;
import test.vtd.koreanguage.sharedPreference.sharedPreference;

public class DetailTestAddNewWord extends AppCompatActivity {
    private newWordAdapter newWordAdapter;
    private List<newWord> mListNewWord;
    private sharedPreference sharedPreference;
    String subjectName;
    boolean nowState;
    ActivityDetailTestAddNewWordBinding binding;
    newWord randomWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTestAddNewWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        subjectName = getIntent().getStringExtra("subjectName");
        init();
        binding.btnAddNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowState == false)
                    addNewWord();
                else
                    Toast.makeText(DetailTestAddNewWord.this, "Không thể thêm từ mới lúc này!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowState == false){
                    sharedPreference.setButtonPressed(true);
                    checkXML();
                    kiemTra();
                }else{
                    sharedPreference.setButtonPressed(false);
                    checkXML();
                }
            }
        });
    }

    private void checkXML() {
        nowState = sharedPreference.isButtonPressed();
        if(nowState == false){
            binding.rcvListNewWord.setVisibility(View.VISIBLE);
            binding.layoutTest.setVisibility(View.GONE);
            loadData();
            binding.btnKiemTra.setText("Kiểm tra");
            binding.btnKiemTra.setBackgroundResource(R.drawable.background_button_on);
        }else{
            binding.rcvListNewWord.setVisibility(View.GONE);
            binding.layoutTest.setVisibility(View.VISIBLE);
            binding.btnKiemTra.setText("Dừng");
            binding.btnKiemTra.setBackgroundResource(R.drawable.background_button_off);
        }
    }

    private void kiemTra() {
        loadQuestion();
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerAndMoveToNext();
            }
        });
    }

    private void checkAnswerAndMoveToNext() {
        int selectedId = binding.radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            if (selectedAnswer.equals(randomWord.getMean())) {
                Toast.makeText(DetailTestAddNewWord.this, "Correct!", Toast.LENGTH_SHORT).show();
                binding.radioGroup.clearCheck();
                loadQuestion();
            } else {
                Toast.makeText(DetailTestAddNewWord.this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DetailTestAddNewWord.this, "Please select an answer", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuestion() {
        randomWord = roomDatabase.getInstance(this).newWordDAO().getRandomWord();
        String correctMean = randomWord.getMean();
        String newWord = randomWord.getNewWord();

        List<String> randomMeans = roomDatabase.getInstance(this).newWordDAO().getRandomMeansExcept(correctMean);

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
        builder.setTitle("Nhập từ mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(this);
        inputName.setHint("Từ mới");
        layout.addView(inputName);
        final EditText inputMeaning = new EditText(this);
        inputMeaning.setHint("Nghĩa");
        layout.addView(inputMeaning);
        builder.setView(layout);

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Str_newWord = inputName.getText().toString().trim();
                String Str_mean = inputMeaning.getText().toString().trim();
                if (!Str_newWord.isEmpty() && !Str_mean.isEmpty()) {
                    if(isNewWordExist(Str_newWord)){
                        hideSoftKeyboard();
                        dialog.cancel();
                        Toast.makeText(DetailTestAddNewWord.this, "Từ này đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }else{
                        newWord newWord = new newWord(Str_newWord, Str_mean,null,subjectName);
                        roomDatabase.getInstance(getApplicationContext()).newWordDAO().insertNewWord(newWord);
                        Toast.makeText(DetailTestAddNewWord.this, "Thành công!", Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard();
                        dialog.cancel();
                        loadData();
                    }
                } else {
                    Toast.makeText(DetailTestAddNewWord.this, "Nhập từ mới!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void init() {
        newWordAdapter = new newWordAdapter(new newWordAdapter.IClickItemNewWord() {
            @Override
            public void updateNewWord(newWord newWord) {
                clickUpdateNewWord(newWord);
            }

            @Override
            public void deleteNewWord(newWord newWord) {
                clickDeleteNewWord(newWord);
            }
        });
        mListNewWord = new ArrayList<>();
        newWordAdapter.setData(mListNewWord);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcvListNewWord.setLayoutManager(linearLayoutManager);
        binding.rcvListNewWord.setAdapter(newWordAdapter);
        sharedPreference = new sharedPreference(this);
        checkXML();
    }

    private void clickDeleteNewWord(newWord newWord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bạn chắc chắn muốn xóa?");

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roomDatabase.getInstance(getApplicationContext()).newWordDAO().deleteNewWord(newWord);
                Toast.makeText(DetailTestAddNewWord.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                hideSoftKeyboard();
                dialog.cancel();
                loadData();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private boolean isNewWordExist(String newWord){
        List<newWord> list = roomDatabase.getInstance(getApplicationContext()).newWordDAO().checkNewWord(newWord);
        return list!=null && !list.isEmpty();
    }

    private void clickUpdateNewWord(newWord newWord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật từ mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(this);
        inputName.setHint("Từ mới");
        inputName.setText(newWord.getNewWord());
        layout.addView(inputName);
        final EditText inputMeaning = new EditText(this);
        inputMeaning.setHint("Nghĩa");
        inputMeaning.setText(newWord.getMean());
        layout.addView(inputMeaning);
        builder.setView(layout);

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Str_newWord = inputName.getText().toString().trim();
                String Str_mean = inputMeaning.getText().toString().trim();
                if (!Str_newWord.isEmpty() && !Str_mean.isEmpty()) {
                    if(isNewWordExist(Str_newWord)){
                        hideSoftKeyboard();
                        dialog.cancel();
                        Toast.makeText(DetailTestAddNewWord.this, "Từ này đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }else{
                        newWord newWord = new newWord(Str_newWord, Str_mean,null,subjectName);
                        roomDatabase.getInstance(getApplicationContext()).newWordDAO().updateNewWord(newWord);
                        Toast.makeText(DetailTestAddNewWord.this, "Thành công!", Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard();
                        dialog.cancel();
                        loadData();
                    }
                } else {
                    Toast.makeText(DetailTestAddNewWord.this, "Nhập từ mới!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void loadData() {
        mListNewWord = roomDatabase.getInstance(getApplicationContext()).newWordDAO().getNewWordBySubject(subjectName);
        newWordAdapter.setData(mListNewWord);
    }
}