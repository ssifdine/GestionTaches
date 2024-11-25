package ma.saifdine.hd.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ma.saifdine.hd.domaine.converter.DateConverter;
import ma.saifdine.hd.domaine.model.Task;

@Database(entities = {Task.class} , version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, "tasks_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
