package test.vtd.koreanguage.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import test.vtd.koreanguage.Model.NewWord;
import test.vtd.koreanguage.Repository.TestDefaultRepository;

public class TestDefaultViewModel extends ViewModel {
    private final TestDefaultRepository testDefaultRepository;
    private final MutableLiveData<Integer> questionState = new MutableLiveData<>(0);
    private final MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<String> feedbackMessage = new MutableLiveData<>();
    private List<NewWord> newWords = new ArrayList<>();
    private int currentQuestionIndex = 0;

    public TestDefaultViewModel() {
        testDefaultRepository = new TestDefaultRepository();
    }

    public LiveData<Integer> getQuestionState() {
        return questionState;
    }

    public LiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<String> getFeedbackMessage() {
        return feedbackMessage;
    }

    public void loadNewWords() {
        testDefaultRepository.fetchNewWord(new TestDefaultRepository.testDefaultCallBack() {
            @Override
            public void onSuccess(List<NewWord> newWordList) {
                newWords = newWordList;
                Collections.shuffle(newWords);
                randomState();
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void randomState() {
        Random random = new Random();
        questionState.setValue(random.nextInt(2));
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < newWords.size()) {
            NewWord word = newWords.get(currentQuestionIndex);
            if (questionState.getValue() == 0) {
                currentQuestion.setValue(createMultipleChoiceQuestion(word));
            } else {
                currentQuestion.setValue(createTextInputQuestion(word));
            }
        } else {
            currentQuestionIndex = 0;
            Collections.shuffle(newWords);
            randomState();
        }
    }

    public void submitAnswer(String answer) {
        Question question = currentQuestion.getValue();
        if (question != null && question.getCorrectAnswer().equals(answer)) {
            feedbackMessage.setValue("Correct!");
            currentQuestionIndex++;
            randomState();
        } else {
            feedbackMessage.setValue("Incorrect!");
        }
    }

    private Question createMultipleChoiceQuestion(NewWord word) {
        List<String> options = testDefaultRepository.getIncorrectAnswers(newWords, word.getMean());
        options.add(word.getMean());
        Collections.shuffle(options);
        return new Question(word.getNewWord(), options, word.getMean());
    }

    private Question createTextInputQuestion(NewWord word) {
        return new Question(word.getPartWord() + ": " + word.getMean(), null, word.getNewWord());
    }

    public static class Question {
        private final String displayQuestion;
        private final List<String> options;
        private final String correctAnswer;

        public Question(String displayQuestion, List<String> options, String correctAnswer) {
            this.displayQuestion = displayQuestion;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String getDisplayQuestion() {
            return displayQuestion;
        }

        public List<String> getOptions() {
            return options;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }
}
