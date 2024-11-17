package test.vtd.koreanguage.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import test.vtd.koreanguage.model.newWord;

@Dao
public interface newWordDAO {
    @Insert
    void insertNewWord(newWord newWord);

    @Query("SELECT * FROM newWord")
    List<newWord> getListNewWord();

    @Query("SELECT * FROM newWord WHERE subject = :subject")
    List<newWord> getNewWordBySubject(String subject);

    @Query("SELECT * FROM newWord WHERE mean = :mean")
    List<newWord> checkNewWord(String mean);

    @Update
    void updateNewWord(newWord newWord);
    @Query("UPDATE newWord SET subject = :newSubject WHERE subject = :oldSubject")
    void updateNewWordBySubject(String oldSubject, String newSubject);

    @Delete
    void deleteNewWord(newWord newWord);

    @Query("SELECT * FROM newWord ORDER BY RANDOM() LIMIT 1")
    newWord getRandomWord();

    @Query("SELECT mean FROM newWord WHERE mean != :correctMean ORDER BY RANDOM() LIMIT 3")
    List<String> getRandomMeansExcept(String correctMean);
}
