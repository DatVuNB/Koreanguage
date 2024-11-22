package test.vtd.koreanguage.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import test.vtd.koreanguage.Model.NewWord;
import test.vtd.koreanguage.Repository.DetailTestAddNewWordRepository;

public class DetailTestAddNewWordViewModel extends AndroidViewModel {
    private final DetailTestAddNewWordRepository repository;
    private final MutableLiveData<String> status = new MutableLiveData<>();
    private final MutableLiveData<Boolean> buttonState = new MutableLiveData<>();

    public DetailTestAddNewWordViewModel(Application application) {
        super(application);
        repository = new DetailTestAddNewWordRepository(application);
    }

    public LiveData<Boolean> getButtonState() {
        return buttonState;
    }

    public void setButtonState(boolean state) {
        repository.setButtonState(state);
        buttonState.setValue(state);
    }

    public void loadButtonState() {
        boolean state = repository.getButtonState();
        buttonState.setValue(state);
    }

    public LiveData<List<NewWord>> getNewWordBySubject(String subject){
        return repository.getNewWordBySubject(subject);
    }
    public LiveData<String> getStatus(){return status;}

    public void insertNewWord(NewWord newWord){
        if(!repository.isExisted(newWord.getNewWord(), newWord.getMean())){
            repository.insertNewWord(newWord);
            status.setValue("Success");
        }else{
            status.setValue("This new word is existed");
        }
    }

    public void updateNewWord(NewWord newWord){
        repository.updateNewWord(newWord);
        status.setValue("Success");
    }

    public void deleteNewWord(NewWord newWord){
        repository.deleteNewWord(newWord);
    }

    public NewWord getRandomWord(){
        return repository.getRandomWord();
    }

    public List<String> getRandomMeansExcept(String correctMean){
        return repository.getRandomMeansExcept(correctMean);
    }
}
