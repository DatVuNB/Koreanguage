package test.vtd.koreanguage.ViewModel;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import test.vtd.koreanguage.Repository.RegisterRepository;

public class RegisterViewModel extends AndroidViewModel {
    private final RegisterRepository registerRepository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this.registerRepository = new RegisterRepository();
    }

    public LiveData<String> getRegisterStatusLiveData(){
        return registerRepository.getRegisterStatusLiveData();
    }

    public void register(String email, String password, String rePassword){
        if(TextUtils.isEmpty(email)) {
            registerRepository.setRegisterStatusLiveData("Please enter email!");
            return;
        }
        if(TextUtils.isEmpty(password)) {
            registerRepository.setRegisterStatusLiveData("Please enter password!");
            return;
        }
        if (password.length() < 6) {
            registerRepository.setRegisterStatusLiveData("Password cannot be less than 6 character");
            return;
        }
        if(TextUtils.isEmpty(rePassword)) {
            registerRepository.setRegisterStatusLiveData("Please enter repassword!");
            return;
        }
        if(password.equals(rePassword)) {
            registerRepository.register(email,password);
        }else{
            registerRepository.setRegisterStatusLiveData("Password and repassword are not same.");
        }
    }
}
