package by.bstu.razvod.timecalculation;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MainViewModel extends AndroidViewModel {
    private BehaviorSubject<String> timeState = BehaviorSubject.createDefault("");

    public MutableLiveData<HashMap<String, Double>> calculatingResult = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
        HashMap<String, Double> timeMap = new HashMap<String, Double>();
        timeMap.put("sec", 1.0);
        timeMap.put("millisec", 1000.0);
        timeMap.put("microsec", 1000000.0);

        calculatingResult.postValue(timeMap);
    }

    public void changeSelection(HashMap<String, Double> timeMap) {
        calculatingResult.postValue(timeMap);
    }

    public void calculateFromSeconds(double time){
        HashMap<String, Double> timeMap = new HashMap<String, Double>();
        timeMap.put("sec", time);
        timeMap.put("millisec", time * 1000);
        timeMap.put("microsec", time * 1000000.0);
        changeSelection(timeMap);
    }

    public void calculateFromMilliseconds(double time){
        HashMap<String, Double> timeMap = new HashMap<String, Double>();
        timeMap.put("sec", time / 1000);
        timeMap.put("millisec", time);
        timeMap.put("microsec", time * 1000.0);
        changeSelection(timeMap);
    }

    public void calculateFromMicroseconds(double time){
        HashMap<String, Double> timeMap = new HashMap<String, Double>();
        timeMap.put("sec", time / 1000000.0);
        timeMap.put("millisec", time / 1000);
        timeMap.put("microsec", time);
        changeSelection(timeMap);
    }

    public boolean checkingInputData(Double time){
        if (time < 0){
            return false;
        }
        return true;
    }
}
