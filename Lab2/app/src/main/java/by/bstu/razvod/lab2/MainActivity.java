package by.bstu.razvod.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RadioGroup activityGroup;
    RadioButton heightAct;
    RadioButton lowAct;
    RadioButton normalAct;

    RadioGroup genderGroup;
    RadioButton female;
    RadioButton male;

    EditText height;
    EditText weight;
    EditText age;
    EditText result;

    Button calculate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityGroup = (RadioGroup )findViewById(R.id.activeGroup);
        heightAct = (RadioButton) findViewById(R.id.heightID);
        normalAct = (RadioButton) findViewById(R.id.normalD);
        lowAct = (RadioButton) findViewById(R.id.lowID);

        genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
        male = (RadioButton) findViewById(R.id.manRadioButton);
        female = (RadioButton) findViewById(R.id.womanRadioButton);

        height = (EditText) findViewById(R.id.height);
        age = (EditText) findViewById(R.id.age);
        weight = (EditText) findViewById(R.id.weightID);

        calculate = (Button) findViewById(R.id.calculateButton);
    }

//    int weightInt;
//    int ageInt;
//    int heightInt;
    public void onClick(View view) {
        if (!checkFormView()){
            return;
        } else {
            Double calculationResult = Calculait.bennedMethod(getGender(), getActivity(), Integer.parseInt(String.valueOf(height.getText())), Integer.parseInt(String.valueOf(age.getText())), Integer.parseInt(String.valueOf(weight.getText())) );
            Toast toast = Toast.makeText(this, calculationResult.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean checkFormView() {
        return  (height.getText() != null
        && weight.getText() != null
        && age.getText() != null
        && (female.isChecked() || male.isChecked())
        && (normalAct.isChecked() || lowAct.isChecked() || heightAct.isChecked()));
    }

    private GenderEnum getGender(){
        return  genderGroup.getCheckedRadioButtonId() == R.id.manRadioButton ? GenderEnum.MALE : GenderEnum.FEMALE;
    }

    private Float getActivity(){
        int id = activityGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.heightID:
                return ActivityEnum.HEIGHT_ACTIVITY.value;
            case R.id.lowID:
                return ActivityEnum.LOW_ACTIVITY.value;
            case R.id.normalD:
                return  ActivityEnum.NORMAL_ACTIVITY.value;
            default:
                return 0f;
        }
    }

}