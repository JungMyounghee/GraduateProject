package com.example.myhomefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myhomefood.UserDirectory.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class firebaseReplaceIngredientsDB extends AppCompatActivity {
    private FirebaseDatabase replace_DB;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_replace_ingredients_db);

        replace_DB = FirebaseDatabase.getInstance();

        //데이터베이스 초기화
        ReplaceDAO dao = new ReplaceDAO();

        ArrayList <ReplaceIngredients> temp = new ArrayList<>();
        ArrayList <String> from_DB = new ArrayList<>();

        ReplaceIngredients temp_list3 = new ReplaceIngredients("우유", "두유");
        temp.add(temp_list3);

        ReplaceIngredients temp_list4 = new ReplaceIngredients("콩", "김,미역,멸치");
        temp.add(temp_list4);

        ReplaceIngredients temp_list5 = new ReplaceIngredients("밀", "감자,쌀");
        temp.add(temp_list5);

        ReplaceIngredients temp_list6 = new ReplaceIngredients("달걀", "두부,콩나물");
        temp.add(temp_list6);

        ReplaceIngredients temp_list7 = new ReplaceIngredients("돼지고기", "쇠고기,흰살 생선,콩고기");
        temp.add(temp_list7);

        ReplaceIngredients temp_list8 = new ReplaceIngredients("생선", "두부,달걀,쇠고기,닭고기");
        temp.add(temp_list8);

        ReplaceIngredients temp_list9 = new ReplaceIngredients("새우", "닭고기,표고버섯");
        temp.add(temp_list9);

        ArrayList <ReplaceIngredients> from_temp = new ArrayList<>();

        Button firebaseDBButton = findViewById(R.id.replace_button);
        firebaseDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace_DB.getReference().child("ReplaceList").setValue(temp);
            }
        });

        ReplaceIngredients test_temp = new ReplaceIngredients();

        Button fromDBButton = findViewById(R.id.fromDBButton);
        fromDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.get().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        from_temp.clear();

                        for(DataSnapshot data : snapshot.getChildren()) {
                            ReplaceIngredients replaceIngredients = data.getValue(ReplaceIngredients.class);
                            from_temp.add(replaceIngredients);

                            //아예 안됨 ㅠㅠ
                        }
                        //adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        });


    }
}