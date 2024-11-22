package test.vtd.koreanguage.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewWord {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String newWord, mean, partWord, subject;

    public NewWord() {
    }

    public NewWord(String newWord, String mean, String partWord, String subject) {
        this.newWord = newWord;
        this.mean = mean;
        this.partWord = partWord;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewWord() {
        return newWord;
    }

    public void setNewWord(String newWord) {
        this.newWord = newWord;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getPartWord() {
        return partWord;
    }

    public void setPartWord(String partWord) {
        this.partWord = partWord;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
