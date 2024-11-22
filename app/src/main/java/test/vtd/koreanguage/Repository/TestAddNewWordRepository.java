package test.vtd.koreanguage.Repository;

import test.vtd.koreanguage.DAO.NewWordDAO;
import test.vtd.koreanguage.DAO.SubjectDAO;
import test.vtd.koreanguage.DatabaseSet.RoomDatabase;
import test.vtd.koreanguage.Model.Subject;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TestAddNewWordRepository {
    private final SubjectDAO subjectDAO;
    private final NewWordDAO newWordDAO;

    public TestAddNewWordRepository(Context context) {
        RoomDatabase database = RoomDatabase.getInstance(context);
        subjectDAO = database.subjectDAO();
        newWordDAO = database.newWordDAO();
    }

    public LiveData<List<Subject>> getAllSubject() {
        return subjectDAO.getListSubject();
    }

    public void insertSubject(Subject subject){
        new Thread(() -> subjectDAO.insertSubject(subject)).start();
    }

    public void updateSubject(Subject subject, String oldSubjectName, String newSubjectName){
        new Thread(() -> {
            subjectDAO.updateSubject(subject);
            newWordDAO.updateNewWordBySubject(oldSubjectName, newSubjectName);
        }).start();
    }

    public void deleteSubject(Subject subject){
        new Thread(() -> {
            subjectDAO.deleteSubject(subject);
            newWordDAO.deleteWordsBySubject(subject.getSubjectName());
        }).start();
    }

    public boolean isExisted(String subjectName){
        return !subjectDAO.checkSubject(subjectName).isEmpty();
    }
}
