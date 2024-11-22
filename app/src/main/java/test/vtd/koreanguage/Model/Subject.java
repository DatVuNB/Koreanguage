package test.vtd.koreanguage.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subject {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String subjectName;

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
