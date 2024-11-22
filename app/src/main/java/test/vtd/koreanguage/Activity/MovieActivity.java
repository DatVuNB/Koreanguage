package test.vtd.koreanguage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import test.vtd.koreanguage.Adapter.MovieAdapter;
import test.vtd.koreanguage.ViewModel.MovieViewModel;
import test.vtd.koreanguage.databinding.ActivityMovieBinding;

public class MovieActivity extends AppCompatActivity {
    ActivityMovieBinding binding;
    private MovieViewModel moviewViewModel;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inits();

        observerViewModel();
    }

    private void observerViewModel() {
        moviewViewModel.getMovies().observe(this, movies ->{
            if(movies != null){
                movieAdapter.updateMovie(movies);
            }
        });
        moviewViewModel.getErrorMessage().observe(this, errorMessage -> {
            if(errorMessage != null){
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inits() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(movieAdapter);
        moviewViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        moviewViewModel.fetchMovie();
    }
}
