package test.vtd.koreanguage.Repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import test.vtd.koreanguage.DAO.NewWordDAO;
import test.vtd.koreanguage.DatabaseSet.RoomDatabase;
import test.vtd.koreanguage.Model.NewWord;
import test.vtd.koreanguage.SharedPreference.SharedPreference;

public class DetailTestAddNewWordRepository {
    private final NewWordDAO newWordDAO;
    private final SharedPreference sharedPreferences;

    public DetailTestAddNewWordRepository(Context context) {
        RoomDatabase database = RoomDatabase.getInstance(context);
        newWordDAO = database.newWordDAO();
        sharedPreferences = new SharedPreference(context);
    }

    public boolean getButtonState(){
        return sharedPreferences.isButtonPressed();
    }

    public void setButtonState(boolean s){
        sharedPreferences.setButtonPressed(s);
    }

    public LiveData<List<NewWord>> getNewWordBySubject(String subject){
        return newWordDAO.getNewWordBySubject(subject);
    }

    public void insertNewWord(NewWord newWord){
        new Thread(() -> newWordDAO.insertNewWord(newWord)).start();
    }

    public void updateNewWord(NewWord newWord){
        new Thread(() -> newWordDAO.updateNewWord(newWord)).start();
    }

    public void deleteNewWord(NewWord newWord){
        new Thread(() -> newWordDAO.deleteNewWord(newWord)).start();
    }

    public boolean isExisted(String newWord, String mean){
        return newWordDAO.checkNewWord(newWord, mean) == null;
    }

    public NewWord getRandomWord(){
        return newWordDAO.getRandomWord();
    }

    public List<String> getRandomMeansExcept(String correctMean){
        return newWordDAO.getRandomMeansExcept(correctMean);
    }
}
