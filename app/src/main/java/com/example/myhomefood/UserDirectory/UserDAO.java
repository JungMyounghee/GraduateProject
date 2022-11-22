package com.example.myhomefood.UserDirectory;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;


public class UserDAO {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    public UserDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    //등록
    public Task<Void> add(User user) {
        return databaseReference.push().setValue(user);
    }

    //조회
    public Query get() {
        auth = FirebaseAuth.getInstance();

        String present = auth.getCurrentUser().getUid();
        databaseReference.orderByChild("userId").equalTo(present);

        return databaseReference;
    }

    //업데이트
    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }



}
