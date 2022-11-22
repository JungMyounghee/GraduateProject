package com.example.myhomefood;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class CanCookList extends AppCompatActivity {
    // 요리 리스트를 담을 ArrayList 변수(items) 선언
    private ArrayList<String> canCookList = new ArrayList<String>();

    // 요리 id 리스트를 담을 ArrayList 변수(items) 선언
    private ArrayList<String> ID_LIST = new ArrayList<String>();

    private ArrayList Have_item = new ArrayList();
    private ListView canCookListView;
    private ArrayAdapter canCookAdapter;
    private int cnt = 0;
    private String urlAddress = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000227_1/";
    private String tempID = "";
    private String prev = "1";
    private int allCNT;
    private int sameCNT;
    private double rate;

    JSONArray ingredient_List;

    //전역변수로 선언
    private JSONObject obj;
    private JSONObject allFoodSearchResult;
    private JSONArray foodDetailList;

    private Handler progressHandler;
    private Handler linearLayoutHandler;

    ProgressBar progressBar;
    LinearLayout cantCookLinearLayout;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_cook_list);

        Have_item = (ArrayList) getIntent().getSerializableExtra("Have_item");
        //System.out.println("가지고 있는 아이템 : " + Have_item.toString());

        canCookListView = (ListView) findViewById(R.id.canCookListView);
        canCookAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, canCookList);
        canCookListView.setAdapter(canCookAdapter);

        progressBar = findViewById(R.id.progressBarMedium);
        cantCookLinearLayout = findViewById(R.id.cantCookLinearLayout);

        get_CanCookList();

        // 리스트뷰의 아이템 클릭 이벤트 -> 상세설명 페이지로 넘어감.
        canCookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String)parent.getItemAtPosition(position);
                ArrayList list = new ArrayList();

                try {
                    list = find(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), New_Detail.class);
                intent.putExtra("recipe_id", list);
                startActivity(intent);
            }
        });

        progressHandler = new Handler(){
            public void handleMessage(Message msg){
                progressBar.setVisibility(View.GONE);
            }
        };

        linearLayoutHandler = new Handler(){
            public void handleMessage(Message msg){
                cantCookLinearLayout.setVisibility(View.VISIBLE);
            }
        };
    }

    public void get_CanCookList() {
        allCNT = 0;
        sameCNT = 0;

        new Thread() {
            @Override
            public void run() {
                ID_LIST.clear();

                while (cnt < 7) {
                    try {
                        String temp_address = urlAddress + ((cnt * 1000) + 1) + "/" + ((cnt + 1) * 1000);

                        URL url = new URL(temp_address);

                        InputStream is = url.openStream();
                        InputStreamReader isr = new InputStreamReader(is, "utf-8");
                        BufferedReader reader = new BufferedReader(isr);

                        StringBuffer buffer = new StringBuffer();
                        String line = reader.readLine();
                        while (line != null) {
                            buffer.append(line + "\n");
                            line = reader.readLine();
                        }

                        String jsonData = buffer.toString();

                        // jsonData를 먼저 JSONObject 형태로 바꾼다.
                        obj = new JSONObject(jsonData);

                        // obj의 "allResult"의 JSONObject를 추출
                        allFoodSearchResult = (JSONObject) obj.get("Grid_20150827000000000227_1");

                        // allResult JSONObject에서 "ingredient_List"의 JSONArray 추출
                        ingredient_List = (JSONArray) allFoodSearchResult.get("row");

                        System.out.println("완료1");

                        for (int i = 0; i < ingredient_List.length(); i++) {
                            JSONObject temp = ingredient_List.getJSONObject(i);

                            tempID = temp.getString("RECIPE_ID");

                            if (!prev.equals(tempID)) {
                                rate = (sameCNT / (double) allCNT);

                                if ((rate >= 0.5) && (!ID_LIST.contains(tempID))) {
                                    System.out.println("sameCNT : " + sameCNT);
                                    System.out.println("allCNT : " + allCNT);

                                    ID_LIST.add(prev);
                                }
                                allCNT = 0;
                                sameCNT = 0;

                                prev = temp.getString("RECIPE_ID");
                            }

                            allCNT++;

                            if (Have_item.contains(temp.getString("IRDNT_NM"))) {
                                System.out.println("Have " + temp.getString("RECIPE_ID") + " ? : " + temp.getString("IRDNT_NM"));
                                sameCNT++;
                            }
                        }

                        //System.out.println("완료2");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                canCookAdapter.notifyDataSetChanged();
                            }
                        });

                        //System.out.println("전체 사이즈 : " + ID_LIST.size());
                        //System.out.println("전체 recipe id 출력 : " + ID_LIST.toString());

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cnt++;

                    if(cnt == 7) {
                        Message msg = progressHandler.obtainMessage();
                        progressHandler.sendMessage(msg);
                    }

                }
                //System.out.println("cnt : " + cnt);
                get_information();
            }
        }.start();
    }

    public void get_information() {

        new Thread(){
            @Override
            public void run() {
                canCookList.clear();

                String urlAddress = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000226_1/1/1000";

                    try {
                        URL url = new URL(urlAddress);

                        InputStream is = url.openStream();
                        InputStreamReader isr = new InputStreamReader(is, "utf-8");
                        BufferedReader reader = new BufferedReader(isr);

                        StringBuffer buffer = new StringBuffer();
                        String line = reader.readLine();
                        while (line != null) {
                            buffer.append(line + "\n");
                            line = reader.readLine();
                        }

                        String jsonData = buffer.toString();

                        JSONObject obj = new JSONObject(jsonData);
                        JSONObject allResult = (JSONObject) obj.get("Grid_20150827000000000226_1");
                        foodDetailList = (JSONArray) allResult.get("row");

                        System.out.println("완료2");

                        for (int i = 0; i < foodDetailList.length(); i++) {
                            JSONObject temp = foodDetailList.getJSONObject(i);

                            if(ID_LIST.contains(temp.getString("RECIPE_ID"))) {
                                System.out.println("요리의 이름 : " + temp.getString("RECIPE_NM_KO"));
                                canCookList.add(temp.getString("RECIPE_NM_KO"));
                            }
                        }

                        System.out.println("완료3");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                canCookAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(canCookList.size() == 0) {
                        Message msg = linearLayoutHandler.obtainMessage();
                        linearLayoutHandler.sendMessage(msg);
                    }
                }
        }.start();
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