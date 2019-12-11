package out.example.dietsupport;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// Parcelable = 직렬화 할 때 사용
public class Food implements Parcelable {
    private String name;
    private int calc;

    public Food(String name, int calc) {
        this.name = name;
        this.calc = calc;
    }

    protected Food(Parcel in) {
        name = in.readString();
        calc = in.readInt();
    }

    public String getName() {
        return name;
    }

    public int getCalc() {
        return calc;
    }


    // 직렬화를 위한 함수들
    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(calc);
    }
    //여기까지

    @NonNull
    @Override
    public String toString() {
        return name + " / " + calc + " Kcal";
    }
}
