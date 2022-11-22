package com.example.myhomefood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PopUpActivity extends Activity {
    TextView txtText;
    private String replace = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up);

        txtText = (TextView) findViewById(R.id.txtText);
        TextView notice = findViewById(R.id.notice);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        notice.setText(data);

        loadData(data);
    }

    public void mOnClose(View v) {
        Intent intent = new Intent();
        intent.putExtra("data", "Close Popup");
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    //안드로이드 백버튼 막기
    @Override
    public void onBackPressed() {
        return;
    }

    //재료 대체 정보 가져오기
    private void loadData(String ingredient) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ReplaceList");
        Query myTopPostsQuery = mDatabase;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ReplaceIngredients replaceIngredients = dataSnapshot.getValue(ReplaceIngredients.class);
                    if(replaceIngredients.getIngredient().equals(ingredient))
                        replace = replaceIngredients.getReplace();
                }
                Log.w("PopUpActivity", "list = "+ replace);

                if(replace.equals(""))
                    txtText.setText("해당 재료를 대체할 재료를 찾지 못했습니다.");
                else
                    txtText.setText("다음과 같은 재료로 대체가 가능합니다. " + replace);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PopUpActivity", "onCancelled");
            }
        });

    }
}