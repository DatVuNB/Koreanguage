package test.vtd.koreanguage.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import test.vtd.koreanguage.Model.Subject;
import test.vtd.koreanguage.Repository.TestAddNewWordRepository;

public class TestAddNewWordViewModel extends AndroidViewModel {
    private final TestAddNewWordRepository testAddNewWordRepository;
    private final MutableLiveData<String> status = new MutableLiveData<>();

    public TestAddNewWordViewModel(Application application) {
        super(application);
        testAddNewWordRepository = new TestAddNewWordRepository(application.getApplicationContext());
    }

    public LiveData<List<Subject>> getAllSubject(){
        return testAddNewWordRepository.getAllSubject();
    }

    public LiveData<String> getStatus(){
        return status;
    }

    public void insertSubject(String subjectName){
        if(!testAddNewWordRepository.isExisted(subjectName)) {
            testAddNewWordRepository.insertSubject(new Subject(subjectName));
            status.setValue("Success");
        } else {
            status.setValue("This subject is existed");
        }
    }

    public void updateSubject(Subject subject, String oldSubjectName, String newSubjectName){
        testAddNewWordRepository.updateSubject(subject, oldSubjectName, newSubjectName);
        status.setValue("Success");
    }

    public void deleteSubject(Subject subject){
        testAddNewWordRepository.deleteSubject(subject);
        status.setValue("Success");
    }
}
