package test.vtd.koreanguage.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import test.vtd.koreanguage.R;

public class Register extends AppCompatActivity {

    TextInputEditText edt_email, edt_password, edt_repassword;
    Button btn_register;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        anhxa();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, repassword;
                email = String.valueOf(edt_email.getText());
                password = String.valueOf(edt_password.getText());
                repassword = String.valueOf(edt_repassword.getText());
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(repassword)) {
                    Toast.makeText(Register.this, "Enter repassword", Toast.LENGTH_SHORT).show();
                } else {
                    if(password.equals(repassword)) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this,
                                                    "Account created.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Register.this,
                                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(Register.this, "Password and repassword are not same.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void anhxa() {
        edt_email = findViewById(R.id.email_dky);
        edt_password = findViewById(R.id.pass_dky);
        edt_repassword = findViewById(R.id.repass_dky);
        btn_register = findViewById(R.id.btndangky);
        mAuth = FirebaseAuth.getInstance();
    }
}