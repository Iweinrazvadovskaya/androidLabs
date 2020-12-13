package by.bstu.razvod.lab4.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {ContactEntity.class})
public abstract class LearnDatabase extends RoomDatabase {

    public abstract LearnDao getLearnDao();


}
