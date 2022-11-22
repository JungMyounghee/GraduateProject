package com.example.myhomefood;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ReplaceDAO {
    private DatabaseReference databaseReference;

    public ReplaceDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("ReplaceList");
    }

    //등록
    public Task<Void> add(ArrayList<ReplaceIngredients> replaceIngredients) {
        return databaseReference.push().setValue(replaceIngredients);
    }

    //조회
    public Query get() {
        return databaseReference;
    }
}
