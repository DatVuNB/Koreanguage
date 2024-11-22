package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import test.vtd.koreanguage.ViewModel.RegisterViewModel;
import test.vtd.koreanguage.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        registerViewModel.getRegisterStatusLiveData().observe(this, status -> {
            if (status != null) {
                Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
            }
        });

        registerViewModel.getRegisterStatusLiveData().observe(this, status -> {
            if(status != null){
                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
            }
            if(status.equals("Account created.")){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btndangky.setOnClickListener(view -> {
            String email, password, repassword;
            email = String.valueOf(binding.emailDky.getText());
            password = String.valueOf(binding.passDky.getText());
            repassword = String.valueOf(binding.repassDky.getText());
            registerViewModel.register(email,password,repassword);
        });
    }
}