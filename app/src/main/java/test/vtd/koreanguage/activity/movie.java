package test.vtd.koreanguage.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import test.vtd.koreanguage.R;
import test.vtd.koreanguage.adapter.movieAdapter;
import test.vtd.koreanguage.model.movieObject;

public class movie extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private test.vtd.koreanguage.adapter.movieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new movieAdapter(new ArrayList<>());
        recyclerView.setAdapter(movieAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("movie");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<movieObject> movies = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    movieObject movie = snapshot.getValue(movieObject.class);
                    movies.add(movie);
                }
                movieAdapter = new movieAdapter(movies);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(movie.this, "Failed to load movies.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
