package out.example.dietsupport;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ScheduleFood implements Parcelable {
    List<String> foods = new ArrayList<>();

    public ScheduleFood() {

    }

    protected ScheduleFood(Parcel in) {
        foods = in.createStringArrayList();
    }


    // 직렬화를 위한 함수/변수들
    public static final Creator<ScheduleFood> CREATOR = new Creator<ScheduleFood>() {
        @Override
        public ScheduleFood createFromParcel(Parcel in) {
            return new ScheduleFood(in);
        }

        @Override
        public ScheduleFood[] newArray(int size) {
            return new ScheduleFood[size];
        }
    };

    // 음식 덮어쓰기
    public void setFoods(List<String> foods) {
        this.foods = foods;
    }

    // 음식 추가하기
    public void addFood(String food) {
        foods.add(food);
    }

    // 해당 객체를 문자열화
    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(foods != null) {
            for (int i = 0; i < foods.size(); i++) {
                sb.append(foods.get(i));
                if (i + 1 != foods.size())
                    sb.append("\n");
            }
        }
        return sb.toString();
    }

    public List<String> getFoods() {
        return foods;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(foods);
    }
}
