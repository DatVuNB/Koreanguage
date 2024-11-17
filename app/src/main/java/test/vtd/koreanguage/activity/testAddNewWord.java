package test.vtd.koreanguage.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import test.vtd.koreanguage.R;
import test.vtd.koreanguage.adapter.subjectAdapter;
import test.vtd.koreanguage.database.roomDatabase;
import test.vtd.koreanguage.model.subject;

public class testAddNewWord extends AppCompatActivity {
    private Button btn_addSubject;
    private RecyclerView rcv_listSubject;
    private test.vtd.koreanguage.adapter.subjectAdapter subjectAdapter;
    private List<subject> mListSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_add_new_word);
        anhxa();
        btn_addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject();
            }
        });
    }

    private void addSubject() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập tên chủ đề");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String topicName = input.getText().toString().trim();
                if (!topicName.isEmpty()) {
                    if(isSubjectExist(topicName)){
                        hideSoftKeyboard();
                        dialog.cancel();
                        Toast.makeText(testAddNewWord.this, "Topic đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }else{
                        subject subject = new subject(topicName);
                        roomDatabase.getInstance(getApplicationContext()).subjectDAO().insertSubject(subject);
                        Toast.makeText(testAddNewWord.this, "Thành công!", Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard();
                        dialog.cancel();
                        loadData();
                    }
                } else {
                    Toast.makeText(testAddNewWord.this, "Nhập tên chủ đề!", Toast.LENGTH_SHORT).show();
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
        mListSubject = roomDatabase.getInstance(getApplicationContext()).subjectDAO().getListSubject();
        subjectAdapter.setData(mListSubject);
    }


    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isSubjectExist(String subjectName){
        List<subject> list = roomDatabase.getInstance(getApplicationContext()).subjectDAO().checkSubject(subjectName);
        return list!=null && !list.isEmpty();
    }

    private void anhxa() {
        btn_addSubject = findViewById(R.id.btn_addSubject);
        rcv_listSubject = findViewById(R.id.rcv_listSubject);
        subjectAdapter = new subjectAdapter(new subjectAdapter.IClickItemSubject() {
            @Override
            public void updateSubject(subject subject) {
                clickUpdateSubject(subject);
            }
            public void deleteSubject(subject subject){
                clickDeleteSubject(subject);
            }
            public void onSubjectClick(subject subject) {
                clickOnSubject(subject);
            }
        });
        mListSubject = new ArrayList<>();
        subjectAdapter.setData(mListSubject);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_listSubject.setLayoutManager(linearLayoutManager);
        rcv_listSubject.setAdapter(subjectAdapter);
        loadData();
    }

    private void clickOnSubject(subject subject) {
        Intent intent = new Intent(testAddNewWord.this, DetailTestAddNewWord.class);
        intent.putExtra("subjectName", subject.getSubjectName());
        startActivity(intent);
    }

    private void clickDeleteSubject(subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bạn chắc chắn muốn xóa?");

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roomDatabase.getInstance(getApplicationContext()).subjectDAO().deleteSubject(subject);
                Toast.makeText(testAddNewWord.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
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

    private void clickUpdateSubject(subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật chủ đề");

        EditText input = new EditText(this);
        builder.setView(input);
        input.setText(subject.getSubjectName());

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTopicName = input.getText().toString().trim();
                if (!newTopicName.isEmpty()) {
                    if(isSubjectExist(newTopicName)){
                        hideSoftKeyboard();
                        Toast.makeText(testAddNewWord.this, "Topic đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }else{
                        String oldSubject = subject.getSubjectName();
                        subject.setSubjectName(newTopicName);
                        roomDatabase.getInstance(getApplicationContext()).newWordDAO().updateNewWordBySubject(oldSubject, subject.getSubjectName());
                        roomDatabase.getInstance(getApplicationContext()).subjectDAO().updateSubject(subject);
                        Toast.makeText(testAddNewWord.this, "Thành công!", Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard();
                        dialog.cancel();
                        loadData();
                    }
                } else {
                    Toast.makeText(testAddNewWord.this, "Nhập tên chủ đề!", Toast.LENGTH_SHORT).show();
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
}