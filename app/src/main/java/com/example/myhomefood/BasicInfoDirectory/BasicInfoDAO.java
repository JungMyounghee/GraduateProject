package com.example.myhomefood.BasicInfoDirectory;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class BasicInfoDAO {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    public BasicInfoDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("basicInformation");
    }

    //등록
    public Task<Void> add(BasicInfo basicInfo) {
        return databaseReference.push().setValue(basicInfo);
    }

    //조회
    public Query get() {
        return databaseReference;
    }

    //업데이트
    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }
}
