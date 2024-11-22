package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import test.vtd.koreanguage.ViewModel.LoginViewModel;
import test.vtd.koreanguage.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    LoginViewModel loginViewModel;

    @Override
    public void onStart() {
        super.onStart();
        loginViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser != null){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getLoginStatusLiveData().observe(this, status ->
                Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show());

        binding.tvdangky.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.btndangnhap.setOnClickListener(view -> {
            String email = String.valueOf(binding.emailDn.getText());
            String password = String.valueOf(binding.passDn.getText());
            loginViewModel.login(email, password);
        });

    }
}