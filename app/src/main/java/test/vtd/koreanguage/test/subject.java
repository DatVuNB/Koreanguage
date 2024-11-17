package test.vtd.koreanguage.test;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subject")
public class subject {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int ID;
    private String subjectName;

    public subject(String subjectName) {
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
