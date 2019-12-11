package out.example.dietsupport.dialogs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import out.example.dietsupport.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddFoodActivity extends AppCompatActivity {

    EditText foodName;
    EditText foodCalc;

    Button confirm;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_food);

        foodName = findViewById(R.id.add_food_food);
        foodCalc = findViewById(R.id.add_food_calc);
        confirm = findViewById(R.id.add_food_confirm);
        cancel = findViewById(R.id.add_food_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("food", foodName.getText().toString());
                    intent.putExtra("calc", Integer.parseInt(foodCalc.getText().toString()));

                    setResult(RESULT_OK, intent);
                    finish();
                } catch(Exception ex) {
                    Toast.makeText(getApplicationContext(), "입력한 값을 다시 확인해주세요", Toast.LENGTH_SHORT).show();
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
    // 음식이름, 칼로리
}
