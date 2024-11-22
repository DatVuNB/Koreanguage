package test.vtd.koreanguage.Repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import test.vtd.koreanguage.Model.NewWord;

public class TestDefaultRepository {
    private final DatabaseReference databaseReference;

    public TestDefaultRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("NewWord");
    }

    public void fetchNewWord(testDefaultCallBack callBack){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<NewWord> newWordList = new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    NewWord newWord = dataSnapshot.getValue(NewWord.class);
                    if(newWord != null)
                        newWordList.add(newWord);
                }
                callBack.onSuccess(newWordList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onFailure(error.getMessage());
            }
        });
    }

    public List<String> getIncorrectAnswers(List<NewWord> newWords, String correctAnswer) {
        List<String> incorrectAnswers = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            String incorrectAnswer;
            do {
                int randomIndex = random.nextInt(newWords.size());
                incorrectAnswer = newWords.get(randomIndex).getMean();
            } while (incorrectAnswers.contains(incorrectAnswer) || incorrectAnswer.equals(correctAnswer));
            incorrectAnswers.add(incorrectAnswer);
        }
        return incorrectAnswers;
    }
    public interface testDefaultCallBack{
        void onSuccess(List<NewWord> newWordList);
        void onFailure(String error);
    }
}
