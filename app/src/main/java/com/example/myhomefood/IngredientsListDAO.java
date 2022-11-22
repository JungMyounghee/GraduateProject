package com.example.myhomefood;

import com.example.myhomefood.UserDirectory.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class IngredientsListDAO {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    public IngredientsListDAO() {
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
}
