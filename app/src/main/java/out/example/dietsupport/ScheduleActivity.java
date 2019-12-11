package out.example.dietsupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import out.example.dietsupport.dialogs.AddFoodActivity;
import out.example.dietsupport.dialogs.FoodAdminActivity;
import out.example.dietsupport.dialogs.ScheduleAddActivity;
import out.example.dietsupport.dialogs.ScheduleRemoveActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private static final Type foodType = new TypeToken<ArrayList<Food>>() {
    }.getType();
    private static final Type scheduleType = new TypeToken<ArrayList<ScheduleFood>>() {
    }.getType();

    private static final int ADD_FOOD_KEY = 100;
    private static final int ADMIN_FOOD_KEY = 101;
    private static final int SCHEDULE_ADD_KEY = 102;
    private static final int SCHEDULE_REMOVE_KEY = 103;


    private ArrayList<Food> foods;
    private ArrayList<ScheduleFood> scheduleFoods;
    private Gson gson = new Gson();

    private ListView schedule;
    private ListViewAdapter adpater;

    private double basecalc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        loadAll();
        if(basecalc == 0) {
            Toast.makeText(getApplicationContext(), "BMI 계산하기 탭에서 기초 대사량을 계산해주세요", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "기초 대사량: " + basecalc + "Kcal", Toast.LENGTH_LONG).show();
        }

        schedule = findViewById(R.id.schedule_listView);
        adpater = new ListViewAdapter();
        schedule.setAdapter(adpater);

        // listView 사이에 생기는 얇은 줄 제거
        schedule.setDivider(null);
        schedule.setDividerHeight(0);

        setActionBarName();

        schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = view.findViewById(R.id.schedule_fragment_calories_5);

                int calories = Integer.parseInt(text.getText().toString());

                Intent intent = new Intent(getApplicationContext(), ExerciseInfoActivity.class);
                intent.putExtra("calories", calories);

                startActivity(intent);
            }
        });
    }

    // 메뉴를 생성하기 위한 메소드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_food, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_food_add: { // 음식 추가 버튼 클릭시 추가를 위한 액티비티 띄우기(이하 같음)
                Intent intent = new Intent(getApplicationContext(), AddFoodActivity.class);
                startActivityForResult(intent, ADD_FOOD_KEY);
            }
            break;
            case R.id.menu_food_admin: {
                Intent intent = new Intent(getApplicationContext(), FoodAdminActivity.class);
                intent.putParcelableArrayListExtra("foods", foods);
                startActivityForResult(intent, ADMIN_FOOD_KEY);
            }
                break;
            case R.id.menu_schedule_add: {
                Intent intent = new Intent(getApplicationContext(), ScheduleAddActivity.class);
                intent.putParcelableArrayListExtra("foods", foods);
                startActivityForResult(intent, SCHEDULE_ADD_KEY);
            }
                break;
            case R.id.menu_schedule_remove: {
                Intent intent = new Intent(getApplicationContext(), ScheduleRemoveActivity.class);
                startActivityForResult(intent, SCHEDULE_REMOVE_KEY);
            }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarName() {
        ActionBar ab = getSupportActionBar();

        // 커스텀 뷰 사용 ON / 기본 title 사용 OFF
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        //ListView처럼 inflate후 안에 있는 textview 수정
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_actionbar, null);
        TextView textView = view.findViewById(R.id.actionbar_text);
        textView.setText("스케줄 관리");

        ab.setCustomView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ADD_FOOD_KEY: // 음식 추가하기
                if (resultCode == RESULT_OK) {
                    String foodName = data.getStringExtra("food");

                    boolean can = true;
                    for(Food food : foods) { // 만약 기존 음식중 음식이름이 겹치는게 있으면 can을 false로 설정
                        if(food.getName().equals(foodName)) {
                            can = false;
                            break;
                        }
                    }
                    if(can) {
                        foods.add(new Food(foodName, data.getIntExtra("calc", 0)));
                        saveAll();
                        Toast.makeText(getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(getApplicationContext(), "이미 존재하는 음식입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case ADMIN_FOOD_KEY: // 음식 관리
                Log.d("dietSupport", "admin");
                if(resultCode == RESULT_OK) {
                    List<Integer> indices = data.getIntegerArrayListExtra("list");

                    Log.d("dietSupport", "indices: " + indices);

                    // 만약 지우려는 음식이 등록되어 있으면 지워버린다.
                    for (int index = indices.size() - 1; index >= 0; index--) {
                        Food food = foods.remove(indices.get(index).intValue());
                        for (ScheduleFood scheduleFood : scheduleFoods) {
                            for (int foodIndex = 0; foodIndex < scheduleFood.foods.size(); foodIndex++) {
                                String indexFood = scheduleFood.getFoods().get(foodIndex);
                                if (food.getName().equals(indexFood)) {
                                    scheduleFood.getFoods().remove(foodIndex);

                                    Log.d("dietSupport", "removed" + index + ", " + foodIndex);
                                    foodIndex--;
                                }
                            }
                        }
                    }

                    saveAll();
                }
                break;
            case SCHEDULE_ADD_KEY:
                if(resultCode == RESULT_OK) {
                    // week, time food 데이터 받아오기
                    int week = data.getIntExtra("week", -1);
                    int time = data.getIntExtra("time", -1);
                    int food = data.getIntExtra("food", -1);
                    if(week >= 0 && time >= 0 && food >= 0) {
                        // scheduleFoods구조가 총 28개인데 각 날짜마다 4개의 저장 공간을 필요로함. 따라서 7 * 4 = 28인 것이고
                        // 월요일은 0,1,2,3 화요일은 4,5,6,7 이런 식이며 월요일은 0, 화요일은 1... 이므로 4를 곱해야 한다.
                        // 여기에 time을 더해 시간대까지 맞춰준다. 여기에 음식을 더한다.

                        Log.d("dietSupport", "week: " + week + ", time: " + time + ", food: " + food);

                        scheduleFoods.get(week * 4 + time).foods.add(foods.get(food).getName());
                        saveAll();
                    }
                }
                break;
            case SCHEDULE_REMOVE_KEY:
                if(resultCode == RESULT_OK) {
                    // week, time food 데이터 받아오기
                    int week = data.getIntExtra("week", -1);
                    int time = data.getIntExtra("time", -1);
                    if(week >= 0 && time >= 0) {
                        // scheduleFoods구조가 총 28개인데 각 날짜마다 4개의 저장 공간을 필요로함. 따라서 7 * 4 = 28인 것이고
                        // 월요일은 0,1,2,3 화요일은 4,5,6,7 이런 식이며 월요일은 0, 화요일은 1... 이므로 4를 곱해야 한다.
                        // 여기에 time을 더해 시간대까지 맞춰준다. 여기에 음식을 더한다.

                        Log.d("dietSupport", "week: " + week + ", time: " + time);

                        scheduleFoods.get(week * 4 + time).foods.clear();
                        saveAll();
                    }
                }
                break;
        }
    }

    public void saveAll() {
        //파일에 모든 데이터 저장하기
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        // gson을 사용해 데이터 직렬화
        edit.putString("foods", gson.toJson(foods, foodType));
        edit.putString("schedules", gson.toJson(scheduleFoods, scheduleType));

        adpater.notifyDataSetChanged();
        schedule.deferNotifyDataSetChanged();



        edit.apply();
    }

    public void loadAll() {
        // 파일에서 스케줄 데이터 모두 읽어오기

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);

        foods = gson.fromJson(pref.getString("foods", ""), foodType);
        scheduleFoods = gson.fromJson(pref.getString("schedules", ""), scheduleType);


        // 읽어올게 없으면 새로 초기화
        if (foods == null) foods = new ArrayList<>();
        if (scheduleFoods == null) {
             scheduleFoods = new ArrayList<>();
            for (int i = 0; i < 28; i++)
                scheduleFoods.add(new ScheduleFood());
        }

        Log.d("dietSupport", "size: " + scheduleFoods.size());

        SharedPreferences prefbase = getSharedPreferences("bmi", MODE_PRIVATE);
        basecalc = BMIActivity.round(prefbase.getFloat("basecalc", 0), 2);
    }

    class ListViewAdapter extends BaseAdapter {
        private final String[] weeks = {"월", "화", "수", "목", "금", "토", "일"};

        public ListViewAdapter() {

        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int i) {
            return weeks[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_schedule_week, viewGroup, false);
            }

            // ScheduleFood를 이용한 뷰 초기화

            ScheduleFood food1 = scheduleFoods.get(i * 4);
            ScheduleFood food2 = scheduleFoods.get(i * 4 + 1);
            ScheduleFood food3 = scheduleFoods.get(i * 4 + 2);
            ScheduleFood food4 = scheduleFoods.get(i * 4 + 3);

            TextView week = view.findViewById(R.id.schedule_fragment_week);
            TextView foods1 = view.findViewById(R.id.schedule_fragment_weekFoods_1);
            TextView foods2 = view.findViewById(R.id.schedule_fragment_weekFoods_2);
            TextView foods3 = view.findViewById(R.id.schedule_fragment_weekFoods_3);
            TextView foods4 = view.findViewById(R.id.schedule_fragment_weekFoods_4);

            TextView calories1 = view.findViewById(R.id.schedule_fragment_calories_1);
            TextView calories2 = view.findViewById(R.id.schedule_fragment_calories_2);
            TextView calories3 = view.findViewById(R.id.schedule_fragment_calories_3);
            TextView calories4 = view.findViewById(R.id.schedule_fragment_calories_4);
            TextView calories5 = view.findViewById(R.id.schedule_fragment_calories_5);

            TextView caloriesExercise = view.findViewById(R.id.schedule_fragment_calories_exercise);

            week.setText(weeks[i]);
            foods1.setText(food1.toString());
            foods2.setText(food2.toString());
            foods3.setText(food3.toString());
            foods4.setText(food4.toString());

            int totalCalories1 = getTotalCalories(food1);
            int totalCalories2 = getTotalCalories(food2);
            int totalCalories3 = getTotalCalories(food3);
            int totalCalories4 = getTotalCalories(food4);
            int totalCaloriesAll = totalCalories1 + totalCalories2 + totalCalories3 + totalCalories4;

            calories1.setText(String.valueOf(totalCalories1));
            calories2.setText(String.valueOf(totalCalories2));
            calories3.setText(String.valueOf(totalCalories3));
            calories4.setText(String.valueOf(totalCalories4));
            calories5.setText(String.valueOf(totalCaloriesAll));

            caloriesExercise.setText(basecalc > 0 ? (totalCaloriesAll >= basecalc ? "(운동 필요)" : "(운동 필요 없음)") : "");

            return view;
        }
    }

    // 등록한 날짜와 시간대에 따라 저장되어있는 음식들의 칼로리 합 구하기
    public int getTotalCalories(ScheduleFood foods) {
        int total = 0;

        if(foods.getFoods() != null) {
            for (String foodStr : foods.getFoods()) {
                for (Food food : this.foods) {
                    if (food.getName().equals(foodStr)) {
                        total += food.getCalc();
                        break;
                    }
                }
            }
        }

        return total;

    }
}
