package by.bstu.razvod.timecalculation;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText seconds;
    private TextInputEditText milliseconds;
    private TextInputEditText microseconds;

    private TextWatcher secondsWatcher;
    private TextWatcher millisecondsWatcher;
    private TextWatcher microsecondsWatcher;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        seconds = (TextInputEditText) findViewById(R.id.editTextSeconds);
        milliseconds = (TextInputEditText) findViewById(R.id.editTextMilliseconds);
        microseconds = (TextInputEditText) findViewById(R.id.editTextMicroseconds);

        secondsWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    if (viewModel.checkingInputData(Double.parseDouble(s.toString()))) {

                        viewModel.calculateFromSeconds(Double.parseDouble(s.toString()));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Negative meaning!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        };

        millisecondsWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    if (viewModel.checkingInputData(Double.parseDouble(seconds.getText().toString()))){
                        viewModel.calculateFromMilliseconds(Double.parseDouble(s.toString()));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Negative meaning!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        };

        microsecondsWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    if (viewModel.checkingInputData(Double.parseDouble(seconds.getText().toString()))){
                        viewModel.calculateFromMicroseconds(Double.parseDouble(s.toString()));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Negative meaning!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        };

        viewModel.calculatingResult.observe(this, resultMap -> {
             this.removeListeners();
            this.setData(resultMap);
            this.addListeners();
        });
    }

    private void setData(HashMap<String, Double> resultMap){
        this.seconds.setText((resultMap.get("sec")).toString());
        this.milliseconds.setText((resultMap.get("millisec")).toString());
        this.microseconds.setText((resultMap.get("microsec")).toString());
    }

    private void removeListeners(){
        seconds.removeTextChangedListener(secondsWatcher);
        milliseconds.removeTextChangedListener(millisecondsWatcher);
        microseconds.removeTextChangedListener(microsecondsWatcher);
    }

    private void addListeners(){
        seconds.addTextChangedListener(secondsWatcher);
        milliseconds.addTextChangedListener(microsecondsWatcher);
        microseconds.addTextChangedListener(microsecondsWatcher);
    }
}