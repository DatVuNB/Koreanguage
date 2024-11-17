package test.vtd.koreanguage.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import test.vtd.koreanguage.model.subject;

@Dao
public interface subjectDAO {
    @Insert
    void insertSubject(subject subject);

    @Query("SELECT * FROM subject")
    List<subject> getListSubject();

    @Query("SELECT * FROM subject WHERE subjectName = :subjectName")
    List<subject> checkSubject(String subjectName);
    @Update
    void updateSubject(subject subject);

    @Delete
    void deleteSubject(subject subject);
}