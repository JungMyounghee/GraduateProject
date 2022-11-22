package com.example.myhomefood;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPageFragment extends Fragment {

    private RecyclerView settingList;
    private GridLayoutManager settingList_gridLayoutManager;
    private SettingAdapter setting_adapter;
    private ArrayList<SettingItemInfo> settingList_items = new ArrayList<>();

    private RecyclerView likeList;
    private LinearLayoutManager likeList_linearLayoutManager;
    private likeItemAdapter like_adapter;
    private ArrayList<LikeInfo> likeList_items = new ArrayList<>();

    private TextView nameText;
    private TextView emailText;
    private Button Logout_Button;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    //데이터베이스 객체
    private UserDAO dao;

    private User currentUserInfo;
    private String key;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPageFragment newInstance(String param1, String param2) {
        MyPageFragment fragment = new MyPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_page, container, false);

        //데이터베이스 초기화
        dao = new UserDAO();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        nameText = v.findViewById(R.id.name_textView);
        emailText = v.findViewById(R.id.email_textView);
        nameText.setText(auth.getCurrentUser().getDisplayName());
        emailText.setText(auth.getCurrentUser().getEmail());

        ImageView setting = v.findViewById(R.id.settingImg);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingButtonList.class);
                startActivity(intent);
            }
        });

        loadData();

        settingList = v.findViewById(R.id.settingView);
        settingList_gridLayoutManager = new GridLayoutManager(getContext(), 4);
        setting_adapter = new SettingAdapter(settingList_items);
        settingList.setLayoutManager(settingList_gridLayoutManager);
        settingList.setAdapter(setting_adapter);

        likeList = v.findViewById(R.id.likeView);
        likeList_linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        like_adapter = new likeItemAdapter(likeList_items);
        likeList.setLayoutManager(likeList_linearLayoutManager);
        likeList.setAdapter(like_adapter);

        //x 클릭 시
        like_adapter.setOnItemClickListener(new likeItemAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final LikeInfo item = likeList_items.get(a_position);
                Intent intent = new Intent(getActivity().getApplicationContext(), likeListDetail.class);
                intent.putExtra("foodName", item.getLikeItem());
                startActivity(intent);
            }

            @Override
            public void onImageClick(View a_view, int img_position) {
                final LikeInfo item = likeList_items.get(img_position);
                deleteLikeItem(item.getLikeItem());
            }
        });

        Logout_Button = v.findViewById(R.id.logout_button);
        Logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(MyPageFragment.this).commit();
                fragmentManager.popBackStack();

                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    //현재 사용자의 정보 추출
    private void loadData() {
        key = null;

        dao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);

                    if(user.getUserId().equals(auth.getCurrentUser().getUid())) {
                        key = data.getKey();
                        currentUserInfo = new User(user.getUserId(), user.getEmail(), user.getUserName(), user.getSetting_list(), user.getLike_list());
                        printInfo(currentUserInfo);
                        break;
                    }
                }

                setting_adapter.notifyDataSetChanged();
                like_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void printInfo(User thisUser) {
        settingList_items.clear();
        likeList_items.clear();

        //선택해놓은 제외 재료를 담음
        if(thisUser.getSetting_list().size() > 0) {
            for (int i = 0; i < thisUser.getSetting_list().size(); i++) {
                if(!settingList_items.contains(thisUser.getSetting_list().get(i))) {
                    System.out.println("setting item : " + thisUser.getSetting_list().get(i));
                    settingList_items.add(new SettingItemInfo(thisUser.getSetting_list().get(i)));
                }
            }
        }

        //즐겨찾기 목록을 담음
        if(thisUser.getLike_list().size() > 0) {
            for (int i = 0; i < thisUser.getLike_list().size(); i++) {
                if(!likeList_items.contains(thisUser.getLike_list().get(i))) {
                    System.out.println("like list item : " + thisUser.getLike_list().get(i));
                    likeList_items.add(new LikeInfo(thisUser.getLike_list().get(i)));
                }
            }
        }
    }

    private void deleteLikeItem(String foodName) {
        //현재 사용자의 즐겨찾기 목록을 담을 ArrayList
        ArrayList<String> tempList;
        tempList = currentUserInfo.getLike_list();

        for(int i=0; i<tempList.size(); i++) {
            if(tempList.get(i).equals(foodName)) {
                tempList.remove(i);
            }
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("like_list", tempList);

        dao.update(key, hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity().getApplicationContext(), "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
}