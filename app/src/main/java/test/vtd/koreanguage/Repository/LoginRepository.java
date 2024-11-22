package test.vtd.koreanguage.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRepository {
    private final FirebaseAuth auth;
    private final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> loginStatusLiveData = new MutableLiveData<>();

    public LoginRepository() {
        this.auth = FirebaseAuth.getInstance();
    }

    public LiveData<FirebaseUser> getUserLiveData(){
        return userLiveData;
    }

    public LiveData<String> getLoginStatusLiveData(){
        return loginStatusLiveData;
    }

    public void setLoginStatusLiveData(String s){loginStatusLiveData.setValue(s);}

    public void login(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        userLiveData.setValue(auth.getCurrentUser());
                        setLoginStatusLiveData("Login successful.");
                    }else{
                        setLoginStatusLiveData("Authentication failed.");
                    }
                });
    }
}
