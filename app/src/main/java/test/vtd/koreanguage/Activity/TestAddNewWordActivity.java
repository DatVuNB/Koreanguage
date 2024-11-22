package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import test.vtd.koreanguage.Model.Subject;
import test.vtd.koreanguage.Adapter.SubjectAdapter;
import test.vtd.koreanguage.ViewModel.TestAddNewWordViewModel;
import test.vtd.koreanguage.databinding.ActivityTestAddNewWordBinding;

public class TestAddNewWordActivity extends AppCompatActivity {
    ActivityTestAddNewWordBinding binding;
    private SubjectAdapter subjectAdapter;
    TestAddNewWordViewModel testAddNewWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestAddNewWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        initVieModel();

        binding.btnAddSubject.setOnClickListener(v -> addSubject());
    }

    private void initRecyclerView() {
        subjectAdapter = new SubjectAdapter(new SubjectAdapter.IClickItemSubject() {
            @Override
            public void updateSubject(Subject subject) {
                clickUpdateSubject(subject);
            }
            public void deleteSubject(Subject subject){
                clickDeleteSubject(subject);
            }
            public void onSubjectClick(Subject subject) {
                clickOnSubject(subject);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcvListSubject.setLayoutManager(linearLayoutManager);
        binding.rcvListSubject.setAdapter(subjectAdapter);
    }

    private void initVieModel() {
        testAddNewWordViewModel = new ViewModelProvider(this, new TestAddNewWordViewModelFactory(getApplication())).get(TestAddNewWordViewModel.class);
        testAddNewWordViewModel.getAllSubject().observe(this, subjects ->
                subjectAdapter.setData(subjects));
        testAddNewWordViewModel.getStatus().observe(this, status ->
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show());
    }

    private void addSubject() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter subject!");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String topicName = input.getText().toString().trim();
            if (!topicName.isEmpty()) {
                testAddNewWordViewModel.insertSubject(topicName);
                hideSoftKeyboard();
            } else {
                Toast.makeText(TestAddNewWordActivity.this, "Please enter subject!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
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

    private void clickOnSubject(Subject subject) {
        Intent intent = new Intent(TestAddNewWordActivity.this, DetailTestAddNewWordActivity.class);
        intent.putExtra("subjectName", subject.getSubjectName());
        startActivity(intent);
    }

    private void clickDeleteSubject(Subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete?");

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            testAddNewWordViewModel.deleteSubject(subject);
            hideSoftKeyboard();
            dialog.cancel();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void clickUpdateSubject(Subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update subject");

        EditText input = new EditText(this);
        builder.setView(input);
        input.setText(subject.getSubjectName());
        String oldTopicName = subject.getSubjectName();

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String newTopicName = input.getText().toString().trim();
            if (!newTopicName.isEmpty()) {
                testAddNewWordViewModel.updateSubject(subject, oldTopicName, newTopicName);

            } else {
                Toast.makeText(TestAddNewWordActivity.this, "Please enter subject!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}