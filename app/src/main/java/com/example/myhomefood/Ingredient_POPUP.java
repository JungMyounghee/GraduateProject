package com.example.myhomefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ingredient_POPUP extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ingredient_popup);

        TextView notice = findViewById(R.id.notice);
        TextView quantity = (TextView) findViewById(R.id.quantity);

        Intent intent = getIntent();
        String ingredient = intent.getStringExtra("ingredient");
        String quantity_temp = intent.getStringExtra("quantity");

        notice.setText(ingredient);
        quantity.setText(quantity_temp);
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
}