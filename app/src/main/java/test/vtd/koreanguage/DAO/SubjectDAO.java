package test.vtd.koreanguage.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import test.vtd.koreanguage.Model.Subject;

@Dao
public interface SubjectDAO {
    @Insert
    void insertSubject(Subject subject);

    @Query("SELECT * FROM Subject")
        LiveData<List<Subject>> getListSubject();

    @Query("SELECT * FROM Subject WHERE subjectName = :subjectName")
    List<Subject> checkSubject(String subjectName);

    @Update
    void updateSubject(Subject subject);

    @Delete
    void deleteSubject(Subject subject);
}
