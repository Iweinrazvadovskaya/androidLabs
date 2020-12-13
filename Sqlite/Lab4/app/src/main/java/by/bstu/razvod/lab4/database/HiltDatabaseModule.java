package by.bstu.razvod.lab4.database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class HiltDatabaseModule {
    @Provides
    @Singleton
    public LearnDataSource getLearnDataSource(DatabaseModule databaseModule){
        return databaseModule.getLearnDataSource();
    }
}
