package test.vtd.koreanguage.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import test.vtd.koreanguage.Model.MovieObject;
import test.vtd.koreanguage.Repository.MovieRepository;

public class MoviewViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<List<MovieObject>> movies = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MoviewViewModel() {
        movieRepository = new MovieRepository();
    }

    public LiveData<List<MovieObject>> getMovies(){
        return movies;
    }

    public LiveData<String> getErrorMessage(){
        return errorMessage;
    }

    public void fetchMovie(){
        movieRepository.fetchMovies(new MovieRepository.MovieCallBack() {
            @Override
            public void onSuccess(List<MovieObject> movieObjectList) {
                movies.setValue(movieObjectList);
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error);
            }
        });
    }
}
