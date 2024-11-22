package test.vtd.koreanguage.DatabaseSet;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import test.vtd.koreanguage.DAO.NewWordDAO;
import test.vtd.koreanguage.DAO.SubjectDAO;
import test.vtd.koreanguage.Model.NewWord;
import test.vtd.koreanguage.Model.Subject;

@Database(entities = {Subject.class, NewWord.class}, version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {
    private static final String DATABASE_NAME = "testNewWord.db";
    private static RoomDatabase instance;
    public static synchronized RoomDatabase getInstance(Context context){
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
    public abstract SubjectDAO subjectDAO();
    public abstract NewWordDAO newWordDAO();
}
