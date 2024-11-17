package test.vtd.koreanguage.test;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface newWordDAO {
    @Insert
    void insertNewWord(newWord newWord);

    @Query("SELECT * FROM newWord")
    List<newWord> getListNewWord();

    @Query("SELECT * FROM newWord WHERE subject = :subject")
    List<newWord> getNewWordBySubject(String subject);

    @Query("SELECT * FROM newWord WHERE newWord = :newWord")
    List<newWord> checkNewWord(String newWord);

    @Update
    void updateNewWord(newWord newWord);

    @Delete
    void deleteNewWord(newWord newWord);

    @Query("SELECT * FROM newWord ORDER BY RANDOM() LIMIT 1")
    newWord getRandomWord();

    @Query("SELECT mean FROM newWord WHERE mean != :correctMean ORDER BY RANDOM() LIMIT 3")
    List<String> getRandomMeansExcept(String correctMean);
}
