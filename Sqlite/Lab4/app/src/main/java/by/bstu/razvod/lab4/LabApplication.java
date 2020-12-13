package by.bstu.razvod.lab4;

import android.app.Application;
import android.util.Log;

import dagger.hilt.android.HiltAndroidApp;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

@HiltAndroidApp
public class LabApplication extends Application {

    private static String TAG = "TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler( error -> {
            Log.d(TAG, error.toString());
        });
    }
}
