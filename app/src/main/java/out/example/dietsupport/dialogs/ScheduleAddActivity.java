package out.example.dietsupport.dialogs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import out.example.dietsupport.Food;
import out.example.dietsupport.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScheduleAddActivity extends AppCompatActivity {

    Spinner week;
    Spinner time;
    Spinner food;

    Button confirm;
    Button cancel;

    ArrayAdapter weekAdapter;
    ArrayAdapter timeAdapter;
    ArrayAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_add);

        week = findViewById(R.id.schedule_add_week);
        time = findViewById(R.id.schedule_add_time);
        food = findViewById(R.id.schedule_add_food);
        confirm = findViewById(R.id.schedule_add_confirm);
        cancel = findViewById(R.id.schedule_add_cancel);

        Intent intent = getIntent();

        List<Food> foods = intent.getParcelableArrayListExtra("foods");
//        List<String> str = new ArrayList<>();
//        for (Food food : foods) {
//            str.add(food.getName());
//        }

        // 스피너에서 선택하라고 만드는 어댑터
        weekAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
        timeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
        foodAdapter = new ArrayAdapter<Food>(getApplicationContext(), R.layout.spinner_item);

        weekAdapter.addAll(new String[]{"월", "화", "수", "목", "금", "토", "일"});
        timeAdapter.addAll(new String[]{"아침", "점심", "저녁", "간식"});
        foodAdapter.addAll(foods);

        weekAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        foodAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        week.setAdapter(weekAdapter);
        time.setAdapter(timeAdapter);
        food.setAdapter(foodAdapter);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 날짜, 시간, 음식 종류를 모두 설정했으면 인텐트에 값을 담고 결과값으로 설정 후 액티비티 종료
                Intent intent = getIntent();
                int weekSelected = week.getSelectedItemPosition();
                int timeSelected = time.getSelectedItemPosition();
                int foodSelected = food.getSelectedItemPosition();
                if (weekSelected >= 0 && timeSelected >= 0 && foodSelected >= 0) {
                    intent.putExtra("week", weekSelected);
                    intent.putExtra("time", timeSelected);
                    intent.putExtra("food", foodSelected);

                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "아직 모두 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //setActionBarName();
    }
}
