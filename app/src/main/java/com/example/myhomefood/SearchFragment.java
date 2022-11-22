package com.example.myhomefood;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SearchFragment extends Fragment {

    private String address = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000226_1/1/1000";

    private ListView listView;
    private Button btnData;
    ArrayAdapter adapter;

    private FirebaseDatabase database;

    //전역변수로 선언
    private JSONObject obj;
    private JSONObject allFoodSearchResult;
    private JSONArray foodDetailList;

    // 음식 이름을 담을 ArrayList 변수(items) 선언
    ArrayList<String> items = new ArrayList<String>();

    //전체 음식을 담을 FoodList
    public Map<String, String> FoodList = new HashMap<String, String>();

    private EditText edit;
    private String str;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        database = FirebaseDatabase.getInstance();

        listView = (ListView) v.findViewById(R.id.listView1);
        adapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        edit = v.findViewById(R.id.frag_edit);

        btnData = v.findViewById(R.id.btnData);
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().length() > 0) {
                    str = edit.getText().toString();

                    new Thread() {
                        @Override
                        public void run() {
                            items.clear();

                            try {
                                URL url = new URL(address);

                                InputStream is = url.openStream();
                                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                                BufferedReader reader = new BufferedReader(isr);

                                //System.out.println("완료1");
                                StringBuffer buffer = new StringBuffer();
                                String line = reader.readLine();
                                while (line != null) {
                                    buffer.append(line + "\n");
                                    line = reader.readLine();
                                }

                                String jsonData = buffer.toString();

                                // jsonData를 먼저 JSONObject 형태로 바꾼다.
                                obj = new JSONObject(jsonData);
                                //System.out.println("완료2");

                                // obj의 "allFoodSearchResult"의 JSONObject를 추출
                                allFoodSearchResult = (JSONObject) obj.get("Grid_20150827000000000226_1");

                                // allFoodSearchResult JSONObject에서 "foodDetailList"의 JSONArray 추출
                                foodDetailList = (JSONArray) allFoodSearchResult.get("row");

                                for (int i = 0; i < foodDetailList.length(); i++) {

                                    JSONObject temp = foodDetailList.getJSONObject(i);
                                    String name = temp.getString("RECIPE_NM_KO");

                                    FoodList.put(temp.getString("RECIPE_ID"), temp.getString("RECIPE_NM_KO"));
                                    if (name.contains(str)) items.add(name);
                                }
                                //System.out.println("완료3");

                                database.getReference().child("AllFoodList").setValue(FoodList);
                                //System.out.println("DB 저장 완료!!!");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else
                    Toast.makeText(getContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 리스트뷰의 아이템 클릭 이벤트 -> 상세설명 페이지로 넘어감.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) parent.getItemAtPosition(position);
                ArrayList list = new ArrayList();

                try {
                    list = find(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getActivity().getApplicationContext(), New_Detail.class);
                intent.putExtra("recipe_id", list);
                startActivity(intent);
            }
        });

        return v;
    }

    public ArrayList find(String name) throws JSONException {
        ArrayList list = new ArrayList();

        for (int i = 0; i < foodDetailList.length(); i++) {
            JSONObject temp = foodDetailList.getJSONObject(i);
            String movieNm = temp.getString("RECIPE_NM_KO");

            if (movieNm.equals(name)) {
                list.add(temp.getString("RECIPE_ID"));
                list.add(temp.getString("RECIPE_NM_KO"));
                list.add(temp.getString("SUMRY"));
                list.add(temp.getString("NATION_NM"));
                list.add(temp.getString("COOKING_TIME"));
                list.add(temp.getString("CALORIE"));
                list.add(temp.getString("QNT"));
                list.add(temp.getString("LEVEL_NM"));
            }
        }

        return list;
    }

}