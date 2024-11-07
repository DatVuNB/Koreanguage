package test.vtd.koreanguage.test;

public class newWordObject {
    private String newWord, mean, partWord;

    public newWordObject() {
    }

    public newWordObject(String newWord, String mean, String partWord) {
        this.newWord = newWord;
        this.mean = mean;
        this.partWord = partWord;
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
}
