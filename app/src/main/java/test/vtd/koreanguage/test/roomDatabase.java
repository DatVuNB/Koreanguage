package test.vtd.koreanguage.test;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {subject.class, newWord.class}, version = 1)
public abstract class roomDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "testNewWord.db";
    private static roomDatabase instance;
    public static synchronized roomDatabase getInstance(Context context){
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), roomDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
    public abstract subjectDAO subjectDAO();
    public abstract newWordDAO newWordDAO();
}
