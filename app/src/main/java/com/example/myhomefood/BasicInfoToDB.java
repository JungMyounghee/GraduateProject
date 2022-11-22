package com.example.myhomefood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myhomefood.BasicInfoDirectory.BasicInfo;
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

public class BasicInfoToDB extends AppCompatActivity {

    private String address = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000226_1/1/1000";
    private FirebaseDatabase database;

    // 음식 이름을 담을 ArrayList 변수(items) 선언
    private ArrayList<String> items = new ArrayList<String>();

    //기본 정보 담을 basicInfos array
    private ArrayList<BasicInfo> basicInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info_to_db);

        database = FirebaseDatabase.getInstance();

        new Thread(){
            @Override
            public void run() {
                items.clear();

                try {
                    URL url = new URL(address);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is,"utf-8");
                    BufferedReader reader = new BufferedReader(isr);

                    //System.out.println("완료1");
                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();

                    while (line != null) {
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }

                    String jsonData = buffer.toString();
                    JSONObject obj = new JSONObject(jsonData);

                    //System.out.println("완료2");
                    JSONObject allFoodSearchResult = (JSONObject)obj.get("Grid_20150827000000000226_1");
                    JSONArray foodDetailList = (JSONArray)allFoodSearchResult.get("row");

                    for (int i = 0; i < foodDetailList.length(); i++) {
                        JSONObject temp = foodDetailList.getJSONObject(i);

                        String recipe_id = temp.getString("RECIPE_ID");
                        String recipe_name = temp.getString("RECIPE_NM_KO");
                        String summary = temp.getString("SUMRY");
                        String nation_name = temp.getString("NATION_NM");
                        String type_name = temp.getString("TY_NM");
                        String cooking_time = temp.getString("COOKING_TIME");
                        String calorie = temp.getString("CALORIE");
                        String quantity = temp.getString("QNT");
                        String level = temp.getString("LEVEL_NM");
                        String ingredients_code = temp.getString("IRDNT_CODE");
                        String price = temp.getString("PC_NM");

                        BasicInfo temp_basicInfo = new BasicInfo(recipe_id, recipe_name, summary, nation_name, type_name, cooking_time, calorie, quantity, level, ingredients_code, price);
                        basicInfos.add(temp_basicInfo);
                    }

                    //System.out.println("완료3");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Button ToDB = findViewById(R.id.dbbutton);
        ToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("basicInformation").setValue(basicInfos);
                //System.out.println("DB 저장 완료!!!");
            }
        });
    }
}