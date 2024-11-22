package test.vtd.koreanguage.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import test.vtd.koreanguage.Model.NewWord;

@Dao
public interface NewWordDAO {
    @Insert
    void insertNewWord(NewWord newWord);

    @Query("SELECT * FROM NewWord WHERE subject = :subject")
    LiveData<List<NewWord>> getNewWordBySubject(String subject);

    @Query("SELECT * FROM NewWord WHERE newWord = :newWord OR mean = :mean")
    List<NewWord> checkNewWord(String newWord, String mean);

    @Update
    void updateNewWord(NewWord newWord);

    @Query("UPDATE NewWord SET subject = :newSubject WHERE subject = :oldSubject")
    void updateNewWordBySubject(String oldSubject, String newSubject);

    @Delete
    void deleteNewWord(NewWord newWord);

    @Query("SELECT * FROM NewWord ORDER BY RANDOM() LIMIT 1")
    NewWord getRandomWord();

    @Query("SELECT mean FROM NewWord WHERE mean != :correctMean ORDER BY RANDOM() LIMIT 3")
    List<String> getRandomMeansExcept(String correctMean);
}
