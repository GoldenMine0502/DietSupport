package out.example.dietsupport.dialogs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import out.example.dietsupport.Food;
import out.example.dietsupport.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FoodAdminActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter;

    private HashSet<Integer> list = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_admin);

        listView = findViewById(R.id.food_admin_listView);
        adapter = new ListViewAdapter();

        List<Food> foods = getIntent().getParcelableArrayListExtra("foods");
        if(foods == null) {
            Toast.makeText(getApplicationContext(), "음식 받아오는 중 에러 발생", Toast.LENGTH_SHORT).show();
            finish();
        }

        for(Food food : foods) {
            adapter.addKey(food.toString());
        }

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        setActionBarName();
    }

    @Override
    public void finish() {
        // 종료할 때 리스트뷰의 체크박스가 선택되어있는 인덱스만 따로 꺼내 result로 설정
        ArrayList<Integer> indices = new ArrayList<>();

        Log.d("dietSupport", "size: " + listView.getChildCount());


        for(int i = 0; i < listView.getChildCount(); i++) {
            CheckBox box = listView.getChildAt(i).findViewById(R.id.fragment_food_admin_remove);
            Log.d("dietSupport", "selected" + i + ": " + box.isChecked());
            if(box.isChecked())
                indices.add(i);
        }

        Toast.makeText(getApplicationContext(), indices.size() + "개 제거 예정", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putIntegerArrayListExtra("list", indices);

        setResult(RESULT_OK, intent);

        super.finish();
    }

    public void setActionBarName() {
        ActionBar ab = getSupportActionBar();

        // 커스텀 뷰 사용 ON / 기본 title 사용 OFF
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        //ListView처럼 inflate후 안에 있는 textview 수정
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_actionbar, null);
        TextView textView = view.findViewById(R.id.actionbar_text);
        textView.setText("음식 관리, 제거하기");

        ab.setCustomView(view);
    }

    class ListViewAdapter extends BaseAdapter {
        List<String> keys = new ArrayList<>();

        public ListViewAdapter() {

        }

        public void addKey(String key) {
            keys.add(key);
        }

        @Override
        public int getCount() {
            return keys.size();
        }

        @Override
        public Object getItem(int i) {
            return keys.get(i);
        }

        @Override
        public long getItemId(int i) {
            return keys.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_food_admin, null, false);
            }

            TextView textView = view.findViewById(R.id.fragment_food_admin_info);
            textView.setText(keys.get(i));



            return view;
        }
    }
}
