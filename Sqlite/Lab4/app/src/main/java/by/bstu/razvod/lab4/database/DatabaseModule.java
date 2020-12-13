package by.bstu.razvod.lab4.database;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class DatabaseModule {
    private LearnDatabase learnDatabase;

    @Inject
    DatabaseModule(@ApplicationContext Context context) {
        this.learnDatabase = Room.databaseBuilder(context, LearnDatabase.class, "contactdatabase").build();
    }

    public LearnDataSource getLearnDataSource(){
        return new LearnDataSourceImplementation(learnDatabase.getLearnDao());
    }
}

