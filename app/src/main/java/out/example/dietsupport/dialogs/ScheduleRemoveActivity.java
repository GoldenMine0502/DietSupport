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

public class ScheduleRemoveActivity extends AppCompatActivity {

    Spinner week;
    Spinner time;

    Button confirm;
    Button cancel;

    ArrayAdapter weekAdapter;
    ArrayAdapter timeAdapter;
    ArrayAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 위의 액션바 없애기
        setContentView(R.layout.activity_schedule_remove);

        week = findViewById(R.id.schedule_add_week);
        time = findViewById(R.id.schedule_add_time);
        confirm = findViewById(R.id.schedule_add_confirm);
        cancel = findViewById(R.id.schedule_add_cancel);

        // listView 초기화
        weekAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
        timeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
        foodAdapter = new ArrayAdapter<Food>(getApplicationContext(), R.layout.spinner_item);

        weekAdapter.addAll(new String[]{"월", "화", "수", "목", "금", "토", "일"});
        timeAdapter.addAll(new String[]{"아침", "점심", "저녁", "간식"});

        week.setAdapter(weekAdapter);
        time.setAdapter(timeAdapter);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 날짜, 시간, 음식 종류를 모두 설정했으면 인텐트에 값을 담고 결과값으로 설정 후 액티비티 종료
                Intent intent = getIntent();
                int weekSelected = week.getSelectedItemPosition();
                int timeSelected = time.getSelectedItemPosition();
                if (weekSelected >= 0 && timeSelected >= 0) {
                    intent.putExtra("week", weekSelected);
                    intent.putExtra("time", timeSelected);

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
    }
}
