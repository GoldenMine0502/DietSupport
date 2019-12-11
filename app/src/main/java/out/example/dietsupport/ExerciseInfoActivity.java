package out.example.dietsupport;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExerciseInfoActivity extends AppCompatActivity {

    private static final List<Pair<String, Integer>> exercises = new ArrayList<>(); // per hour

    static {
        exercises.add(new Pair<>("조깅", 196));
        exercises.add(new Pair<>("계단타기", 141));
        exercises.add(new Pair<>("자전거", 483));
        exercises.add(new Pair<>("달리기", 315));
        exercises.add(new Pair<>("테니스", 240));
        exercises.add(new Pair<>("등산", 196));
        exercises.add(new Pair<>("수영", 518));
        exercises.add(new Pair<>("농구", 200));
        exercises.add(new Pair<>("야구", 180));
        exercises.add(new Pair<>("탁구", 200));
    }

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        int calories = getIntent().getIntExtra("calories", 0);

        text = findViewById(R.id.exercise_info_text);


        StringBuilder sb = new StringBuilder();
        for(Pair<String, Integer> exercise : exercises) {
            sb.append(exercise.first + " : " + round((double)calories / exercise.second, 1) + " 시간 \n");
        }
        text.setText(sb.toString());

        setActionBarName();
    }

    public static double round(double value, int key) {
        double divide = Math.pow(10, key);

        return Math.round(value * divide) / divide;
    }

    public void setActionBarName() {
        ActionBar ab = getSupportActionBar();

        // 커스텀 뷰 사용 ON / 기본 title 사용 OFF
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        //ListView처럼 inflate후 안에 있는 textview 수정
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_actionbar, null);
        TextView textView = view.findViewById(R.id.actionbar_text);
        textView.setText("필요 운동량");

        ab.setCustomView(view);
    }
}
