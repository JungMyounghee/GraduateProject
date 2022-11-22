package com.example.myhomefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhomefood.UserDirectory.User;
import com.example.myhomefood.UserDirectory.UserDAO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SettingButtonList extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> List = new ArrayList<>();
    private ArrayList<String> like_like = new ArrayList<>();

    private ArrayList<String> grain_list = new ArrayList<>();
    private ArrayList<String> vegetable_list = new ArrayList<>();
    private ArrayList<String> fruit_list = new ArrayList<>();
    private ArrayList<String> meat_list = new ArrayList<>();
    private ArrayList<String> seafood_list = new ArrayList<>();
    private ArrayList<String> seaweed_list = new ArrayList<>();
    private ArrayList<String> egg_list = new ArrayList<>();
    private ArrayList<String> can_list = new ArrayList<>();
    private ArrayList<String> noodle_list = new ArrayList<>();
    private ArrayList<String> mushroom_list = new ArrayList<>();
    private ArrayList<String> etc_list = new ArrayList<>();
    private ArrayList<String> source_list = new ArrayList<>();
    private ArrayList<String> spice_list = new ArrayList<>();
    private ArrayList<String> nut_list = new ArrayList<>();
    private ArrayList<String> powder_list = new ArrayList<>();

    private MyPageFragment myPageFragment;

    private int cnt = 0;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private String result;

    UserDAO dao;

    private String key;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_button_list);

        myPageFragment = new MyPageFragment();

        androidx.gridlayout.widget.GridLayout grain_grid = findViewById(R.id.grain_grid);
        androidx.gridlayout.widget.GridLayout vegetable_grid = findViewById(R.id.vegetable_grid);
        androidx.gridlayout.widget.GridLayout fruit_grid = findViewById(R.id.fruit_grid);
        androidx.gridlayout.widget.GridLayout meat_grid = findViewById(R.id.meat_grid);
        androidx.gridlayout.widget.GridLayout seafood_grid = findViewById(R.id.seafood_grid);
        androidx.gridlayout.widget.GridLayout seaweed_grid = findViewById(R.id.seaweed_grid);
        androidx.gridlayout.widget.GridLayout egg_grid = findViewById(R.id.egg_grid);
        androidx.gridlayout.widget.GridLayout can_grid = findViewById(R.id.can_grid);
        androidx.gridlayout.widget.GridLayout noodle_grid = findViewById(R.id.noodle_grid);
        androidx.gridlayout.widget.GridLayout mushroom_grid = findViewById(R.id.mushroom_grid);
        androidx.gridlayout.widget.GridLayout etc_grid = findViewById(R.id.etc_grid);
        androidx.gridlayout.widget.GridLayout source_grid = findViewById(R.id.source_grid);
        androidx.gridlayout.widget.GridLayout spice_grid = findViewById(R.id.spice_grid);
        androidx.gridlayout.widget.GridLayout nut_grid = findViewById(R.id.nut_grid);
        androidx.gridlayout.widget.GridLayout powder_grid = findViewById(R.id.powder_grid);

        try{
            InputStream is = getBaseContext().getResources().getAssets().open("Ingredients_list.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if(wb != null) {
                Sheet sheet = wb.getSheet(0);

                if(sheet != null){
                    int colTotal = sheet.getColumns();
                    int rowIndexStart = 1;

                    grain_list.clear();
                    vegetable_list.clear();
                    fruit_list.clear();
                    meat_list.clear();
                    seafood_list.clear();
                    seaweed_list.clear();
                    egg_list.clear();
                    can_list.clear();
                    noodle_list.clear();
                    mushroom_list.clear();
                    etc_list.clear();
                    source_list.clear();
                    spice_list.clear();
                    nut_list.clear();
                    powder_list.clear();

                    for(int col=0; col<colTotal; col++) {
                        for(int row = rowIndexStart; row<sheet.getColumn(col).length; row++) {
                            String contents = sheet.getCell(col,row).getContents();

                            if(col==0)
                                grain_list.add(contents);
                            else if(col==1)
                                vegetable_list.add(contents);
                            else if(col==2)
                                fruit_list.add(contents);
                            else if(col==3)
                                meat_list.add(contents);
                            else if(col==4)
                                seafood_list.add(contents);
                            else if(col==5)
                                seaweed_list.add(contents);
                            else if(col==6)
                                egg_list.add(contents);
                            else if(col==7)
                                can_list.add(contents);
                            else if(col==8)
                                noodle_list.add(contents);
                            else if(col==9)
                                mushroom_list.add(contents);
                            else if(col==10)
                                etc_list.add(contents);
                            else if(col==11)
                                source_list.add(contents);
                            else if(col==12)
                                spice_list.add(contents);
                            else if(col==13)
                                nut_list.add(contents);
                            else if(col==14)
                                powder_list.add(contents);
                        } //for
                    }//for
                }//if
            }//if
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        for(int i=0; i<grain_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(grain_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            grain_grid.addView(btn);

        }

        for(int i=0; i<vegetable_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(vegetable_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            vegetable_grid.addView(btn);
        }

        for(int i=0; i<fruit_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(fruit_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            fruit_grid.addView(btn);
        }

        for(int i=0; i<meat_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(meat_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            meat_grid.addView(btn);
        }

        for(int i=0; i<seafood_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(seafood_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            seafood_grid.addView(btn);
        }

        for(int i=0; i<seaweed_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(seaweed_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            seaweed_grid.addView(btn);
        }

        for(int i=0; i<egg_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(egg_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            egg_grid.addView(btn);
        }

        for(int i=0; i<can_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(can_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            can_grid.addView(btn);
        }

        for(int i=0; i<noodle_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(noodle_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            noodle_grid.addView(btn);
        }

        for(int i=0; i<mushroom_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(mushroom_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            mushroom_grid.addView(btn);
        }

        for(int i=0; i<etc_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(etc_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            etc_grid.addView(btn);
        }

        for(int i=0; i<source_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(source_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            source_grid.addView(btn);
        }

        for(int i=0; i<spice_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(spice_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            spice_grid.addView(btn);
        }

        for(int i=0; i<nut_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(nut_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            nut_grid.addView(btn);
        }

        for(int i=0; i<powder_list.size(); i++) {
            Button btn = new Button(this);
            btn.setText(powder_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            powder_grid.addView(btn);
        }

        //onClick 하나씩 다 적용
        final ImageView grain_btn = findViewById(R.id.grain_btn);
        grain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grain_grid.getVisibility() == View.VISIBLE) {
                    grain_grid.setVisibility(View.GONE);
                    grain_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    grain_grid.setVisibility(View.VISIBLE);
                    grain_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView vegetable_btn = findViewById(R.id.vegetable_btn);
        vegetable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vegetable_grid.getVisibility() == View.VISIBLE) {
                    vegetable_grid.setVisibility(View.GONE);
                    vegetable_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    vegetable_grid.setVisibility(View.VISIBLE);
                    vegetable_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView fruit_btn = findViewById(R.id.fruit_btn);
        fruit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fruit_grid.getVisibility() == View.VISIBLE) {
                    fruit_grid.setVisibility(View.GONE);
                    fruit_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    fruit_grid.setVisibility(View.VISIBLE);
                    fruit_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView meat_btn = findViewById(R.id.meat_btn);
        meat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meat_grid.getVisibility() == View.VISIBLE) {
                    meat_grid.setVisibility(View.GONE);
                    meat_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    meat_grid.setVisibility(View.VISIBLE);
                    meat_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView seafood_btn = findViewById(R.id.seafood_btn);
        seafood_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seafood_grid.getVisibility() == View.VISIBLE) {
                    seafood_grid.setVisibility(View.GONE);
                    seafood_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    seafood_grid.setVisibility(View.VISIBLE);
                    seafood_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView seaweed_btn = findViewById(R.id.seaweed_btn);
        seaweed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seaweed_grid.getVisibility() == View.VISIBLE) {
                    seaweed_grid.setVisibility(View.GONE);
                    seaweed_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    seaweed_grid.setVisibility(View.VISIBLE);
                    seaweed_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView egg_btn = findViewById(R.id.egg_btn);
        egg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(egg_grid.getVisibility() == View.VISIBLE) {
                    egg_grid.setVisibility(View.GONE);
                    egg_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    egg_grid.setVisibility(View.VISIBLE);
                    egg_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView can_btn = findViewById(R.id.can_btn);
        can_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(can_grid.getVisibility() == View.VISIBLE) {
                    can_grid.setVisibility(View.GONE);
                    can_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    can_grid.setVisibility(View.VISIBLE);
                    can_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView noodle_btn = findViewById(R.id.noodle_btn);
        noodle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noodle_grid.getVisibility() == View.VISIBLE) {
                    noodle_grid.setVisibility(View.GONE);
                    noodle_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    noodle_grid.setVisibility(View.VISIBLE);
                    noodle_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView mushroom_btn = findViewById(R.id.mushroom_btn);
        mushroom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mushroom_grid.getVisibility() == View.VISIBLE) {
                    mushroom_grid.setVisibility(View.GONE);
                    mushroom_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    mushroom_grid.setVisibility(View.VISIBLE);
                    mushroom_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView etc_btn = findViewById(R.id.etc_btn);
        etc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etc_grid.getVisibility() == View.VISIBLE) {
                    etc_grid.setVisibility(View.GONE);
                    etc_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    etc_grid.setVisibility(View.VISIBLE);
                    etc_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView source_btn = findViewById(R.id.source_btn);
        source_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(source_grid.getVisibility() == View.VISIBLE) {
                    source_grid.setVisibility(View.GONE);
                    source_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    source_grid.setVisibility(View.VISIBLE);
                    source_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView spice_btn = findViewById(R.id.spice_btn);
        spice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spice_grid.getVisibility() == View.VISIBLE) {
                    spice_grid.setVisibility(View.GONE);
                    spice_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    spice_grid.setVisibility(View.VISIBLE);
                    spice_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView nut_btn = findViewById(R.id.nut_btn);
        nut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nut_grid.getVisibility() == View.VISIBLE) {
                    nut_grid.setVisibility(View.GONE);
                    nut_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    nut_grid.setVisibility(View.VISIBLE);
                    nut_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        final ImageView powder_btn = findViewById(R.id.powder_btn);
        powder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(powder_grid.getVisibility() == View.VISIBLE) {
                    powder_grid.setVisibility(View.GONE);
                    powder_btn.setImageResource(R.drawable.circle_plus);
                }

                else {
                    powder_grid.setVisibility(View.VISIBLE);
                    powder_btn.setImageResource(R.drawable.circle_minus);
                }
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //데이터베이스 초기화
        dao = new UserDAO();

        loadCurrentUser();

        //finish 버튼 클릭 시 DB에 저장
        Button finish_button = findViewById(R.id.finish_button);
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCurrentUser();

                String id = auth.getCurrentUser().getUid();
                String email = auth.getCurrentUser().getEmail();
                String name = auth.getCurrentUser().getDisplayName();
                User user = new User(id, email, name, List, like_like);

                result = null;

                if (currentUser==null)
                    createSettingList(user);
                else {
                    if (currentUser.getSetting_list().size() > 0) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("setting_list", List);
                        updateSettingList(key, hashMap);
                    }
                }
                finish();
            }
        });
    }

    private void loadCurrentUser() {
        //user id 확인
        dao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    User alreadyUser = data.getValue(User.class);

                    if(alreadyUser.getUserId().equals(auth.getCurrentUser().getUid())) {
                        key = data.getKey();
                        currentUser = new User(alreadyUser.getUserId(), alreadyUser.getEmail(), alreadyUser.getUserName(), alreadyUser.getSetting_list(), alreadyUser.getLike_list());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateSettingList(String key, HashMap<String, Object> hashMap) {
        dao.update(key, hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "재료 리스트가 업데이트 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "업데이트 ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createSettingList(User user) {
        //추가
        dao.add(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "재료 리스트가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "저장 ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button btn = findViewById(v.getId());
        String s = btn.getText().toString();

        System.out.println(s);

        v.setSelected(!v.isSelected());

        if(v.isSelected()==true) {
            List.add(s);
        }
        else List.remove(s);
    }
}