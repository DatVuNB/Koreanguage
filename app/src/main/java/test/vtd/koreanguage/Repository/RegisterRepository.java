package test.vtd.koreanguage.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterRepository {
    private final FirebaseAuth auth;
    private final MutableLiveData<String> registerStatusLiveData = new MutableLiveData<>();

    public RegisterRepository() {
        this.auth = FirebaseAuth.getInstance();
    }

    public LiveData<String> getRegisterStatusLiveData(){
        return registerStatusLiveData;
    }

    public void setRegisterStatusLiveData(String s){
        registerStatusLiveData.setValue(s);
    }

    public void register(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        setRegisterStatusLiveData("Account created.");
                    }else{
                        setRegisterStatusLiveData("Authentication failed.");
                    }
                });
    }
}
