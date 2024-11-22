package test.vtd.koreanguage.ViewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import test.vtd.koreanguage.Repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {
    private final LoginRepository loginRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository();
    }

    public LiveData<FirebaseUser> getUserLiveData(){
        return loginRepository.getUserLiveData();
    }

    public LiveData<String> getLoginStatusLiveData(){
        return loginRepository.getLoginStatusLiveData();
    }

    public void login(String email, String password){
        if (TextUtils.isEmpty(email)) {
            loginRepository.setLoginStatusLiveData("Email cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            loginRepository.setLoginStatusLiveData("Password cannot be empty");
            return;
        }
        loginRepository.login(email, password);
    }
}
