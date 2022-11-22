package com.example.myhomefood;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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

public class AllIngredients extends AppCompatActivity {
    private int cnt = 0;
    private String urlAddress = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000227_1/";
    private String tempID = "";
    private String prev = "1";

    JSONArray ingredient_List;

    //전역변수로 선언
    private JSONObject obj;
    private JSONObject allFoodSearchResult;

    private ArrayList<ArrayList<String>> main_list = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> addition_list = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> source_list = new ArrayList<ArrayList<String>>();

    ArrayList<DetailOfIngredient> recipe_list = new ArrayList<>();
    private FirebaseDatabase ingredients_DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ingredients);

        ingredients_DB = FirebaseDatabase.getInstance();

        //재료 정보를 DB에 저장
        new Thread() {
            @Override
            public void run() {
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
                            String type = temp.getString("IRDNT_TY_NM");

                            /*
                            if(!prev.equals(tempID)) {
                                DetailOfIngredient temp_temp = new DetailOfIngredient(prev, (ArrayList<ArrayList<String>>) main_list.clone(), (ArrayList<ArrayList<String>>) addition_list.clone(), (ArrayList<ArrayList<String>>) source_list.clone());
                                recipe_list.add(temp_temp);
                                main_list.clear();
                                addition_list.clear();
                                source_list.clear();
                                prev=tempID;
                            }

                             */

                            if(type.equals("주재료")) {
                                ArrayList<String> list_temp = new ArrayList<>();

                                list_temp.add(temp.getString("IRDNT_NM"));
                                list_temp.add(temp.getString("IRDNT_CPCTY"));
                                main_list.add(list_temp);
                            } else if(type.equals("부재료")) {
                                ArrayList<String> list_temp = new ArrayList<>();

                                list_temp.add(temp.getString("IRDNT_NM"));
                                list_temp.add(temp.getString("IRDNT_CPCTY"));
                                addition_list.add(list_temp);
                            } else if(type.equals("양념")) {
                                ArrayList<String> list_temp = new ArrayList<>();

                                list_temp.add(temp.getString("IRDNT_NM"));
                                list_temp.add(temp.getString("IRDNT_CPCTY"));
                                source_list.add(list_temp);
                            }
                        }

                        System.out.println("완료2");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cnt++;
                }
                AllIngredientsListDAO allIngredientsListDAO = new AllIngredientsListDAO();
                allIngredientsListDAO.ingredients_list.addAll(recipe_list);

                ingredients_DB.getReference().child("AllRecipeIngredientsList").setValue(allIngredientsListDAO);
            }
        }.start();
    }//onCreate

    public class AllIngredientsListDAO {
        public ArrayList<DetailOfIngredient> ingredients_list = new ArrayList<>();
    }
}

