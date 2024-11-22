package test.vtd.koreanguage.Repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import test.vtd.koreanguage.Model.MovieObject;

public class MovieRepository {
    private final DatabaseReference databaseReference;

    public MovieRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("movie");
    }

    public void fetchMovies(MovieCallBack callback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MovieObject> movies = new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MovieObject movieObject =  dataSnapshot.getValue(MovieObject.class);
                    if(movieObject != null)
                        movies.add(movieObject);
                }
                callback.onSuccess(movies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }

    public interface MovieCallBack{
        void onSuccess(List<MovieObject> movieObjectList);
        void onFailure(String error);
    }
}
