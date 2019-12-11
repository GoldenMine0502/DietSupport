package out.example.dietsupport;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class BMIActivity extends AppCompatActivity {

    EditText height;
    EditText weight;
    EditText age;

    Button calculate;

    TextView result;
    TextView result2;

    RadioButton man;
    RadioButton woman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        height = findViewById(R.id.bmi_height);
        weight = findViewById(R.id.bmi_weight);
        age = findViewById(R.id.bmi_age);

        calculate = findViewById(R.id.bmi_calculate);

        result = findViewById(R.id.bmi_result);
        result2 = findViewById(R.id.bmi_result2);

        man = findViewById(R.id.bmi_man);
        woman = findViewById(R.id.bmi_woman);

        // "계산" 버튼을 클릭했을 때 이벤트 설정
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // calBmi(height, weight)를 이용해 먼저 BMI를 구하고 이를 2자리수까지 반올림함
                    double heightDigit = Double.parseDouble(height.getText().toString());
                    double weightDigit = Double.parseDouble(weight.getText().toString());
                    int ageDigit = Integer.parseInt(age.getText().toString());
                    boolean isMan = man.isChecked();

                    double basecalc = round(isMan ? manCalc(heightDigit, weightDigit, ageDigit) : womanCalc(heightDigit, weightDigit, ageDigit), 2);

                    result.setText("BMI: " + round(calBmi(heightDigit, weightDigit), 2));
                    result2.setText("기초대사량: " + basecalc);
                    Toast.makeText(getApplicationContext(), "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getSharedPreferences("bmi", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putFloat("basecalc", (float)basecalc);

                    edit.apply();
                } catch(Exception ex) {
                    Toast.makeText(getApplicationContext(), "숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setActionBarName();
    }

    // 상단 액션바의 타이틀을 수정하는데 글씨체를 적용하려면 따로 커스텀 레이아웃을 만들어 적용해야 함
    public void setActionBarName() {
        ActionBar ab = getSupportActionBar();

        // 커스텀 뷰 사용 ON / 기본 title 사용 OFF
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        //ListView처럼 inflate후 안에 있는 textview 수정
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_actionbar, null);
        TextView textView = view.findViewById(R.id.actionbar_text);
        textView.setText("BMI 계산하기");

        ab.setCustomView(view);
    }

    public static double calBmi(double height, double weight) {
        return weight / Math.pow((height/100), 2); // BMI 공식 pow = 거듭제곱 즉 ^2
    }

    public static double round(double value, int digit) { // 반올림 예를들어 (10.123, 1)로 호출하면 10.1이 된다.
        double digits = Math.pow(10, digit);

        return Math.round(value*digits) / digits; // 값을 곱해 반올림 후 다시 나누는 방식
    }

    public static double manCalc(double height, double weight, int age) {
        return 66.47 + (13.75 * weight) + 5 * height - (6.76 * age);
    }

    public static double womanCalc(double height, double weight, int age) {
        return 655.1 + (9.56 * weight) + 1.85 * height - (4.68 * age);
    }
}
