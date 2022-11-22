package com.example.myhomefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class New_Detail extends AppCompatActivity {
    private RecyclerView processList;
    private LinearLayoutManager process_linearLayoutManager;
    private ProcessAdapter process_adapter;
    private ArrayList<ProcessInfo> process_items = new ArrayList<>();

    private RecyclerView ingredientList;
    private GridLayoutManager ingredient_gridLayoutManager;
    private IngredientAdapter ingredient_adapter;
    private ArrayList<IngredientInfo> ingredient_items = new ArrayList<>();

    private RecyclerView settingList;
    private GridLayoutManager setting_gridLayoutManager;
    private IngredientAdapter setting_adapter;
    private ArrayList<IngredientInfo> settingListItems = new ArrayList<>();

    private String food_id = "1";

    //현재 사용자의 setting list 담을 currentUserSettingList
    private ArrayList<String> currentUserSettingList = new ArrayList<>();

    //재료의 정보를 담을 ArrayList 변수 선언
    private ArrayList<ArrayList<String>> type_list = new ArrayList<>();
    private ArrayList<String> intent_list = new ArrayList();

    private ImageView foodImage;

    private FirebaseAuth auth;
    private String key;

    //데이터베이스 객체
    UserDAO dao;

    private User currentUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        //데이터베이스 초기화
        dao = new UserDAO();
        GetCurrentUserInfo();

        processList = findViewById(R.id.processList);
        process_linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        process_adapter = new ProcessAdapter(process_items);
        processList.setLayoutManager(process_linearLayoutManager);
        processList.setAdapter(process_adapter);

        ingredientList = findViewById(R.id.ingredientList);
        ingredient_gridLayoutManager = new GridLayoutManager(this, 4);
        ingredient_adapter = new IngredientAdapter(ingredient_items);
        ingredientList.setLayoutManager(ingredient_gridLayoutManager);
        ingredientList.setAdapter(ingredient_adapter);

        settingList = findViewById(R.id.settingList);
        setting_gridLayoutManager = new GridLayoutManager(this, 4);
        setting_adapter = new IngredientAdapter(settingListItems);
        settingList.setLayoutManager(setting_gridLayoutManager);
        settingList.setAdapter(setting_adapter);

        foodImage = findViewById(R.id.FoodImage);

        getIntent_list();
        get_ingredient();
        get_process();

        String url = null;

        try{
            InputStream is = getBaseContext().getResources().getAssets().open("ImageFiles.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if(wb != null) {
                Sheet sheet = wb.getSheet(0);

                if(sheet != null){
                    int colTotal = sheet.getColumns(); //전체 컬럼
                    int rowIndexStart = 0;
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    int temp_index = 0;

                    for(int row=rowIndexStart; row<rowTotal; row++) {
                        for(int col=0; col<colTotal; col++) {
                            String contents = sheet.getCell(col, row).getContents();

                            if(contents.equals(food_id))
                                temp_index = row;
                        }
                    }

                    url = sheet.getCell(2, temp_index).getContents();

                    System.out.println("****************** Url = " + url);
                }//if
            }//if
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        if (!url.equals(""))
            new DownloadFilesTask2().execute(url);

        auth = FirebaseAuth.getInstance();
        key = null;

        Button like_button = findViewById(R.id.like_button);
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLikeList();
            }
        });

    }

    public void GetCurrentUserInfo() {
        dao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    User userTemp = data.getValue(User.class);
                    if (userTemp.getUserId().equals(auth.getCurrentUser().getUid())) {
                        key = data.getKey();
                        currentUser = userTemp;
                        getSettingList(userTemp);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getSettingList(User user) {
        System.out.println("setting list : " + user.getSetting_list().toString());
        currentUserSettingList = user.getSetting_list();
    }

    public void updateLikeList() {
        ArrayList<String> this_like_list;
        this_like_list = currentUser.getLike_list();
        if(!this_like_list.contains(intent_list.get(1)))
            this_like_list.add(intent_list.get(1));

        System.out.println("저장 중 . . . ");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("like_list", this_like_list);

        dao.update(key, hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class DownloadFilesTask2 extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0];
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            foodImage.setImageBitmap(result);
        }

    }

    public void getIntent_list() {
        intent_list = (ArrayList) getIntent().getSerializableExtra("recipe_id");

        food_id = (String) intent_list.get(0); //ID

        TextView recipe_ID = findViewById(R.id.detailOfRecipe);
        recipe_ID.setText((CharSequence) intent_list.get(1));  //이름

        TextView text2 = findViewById(R.id.sumry);
        text2.setText((CharSequence) intent_list.get(2)); //설명

        TextView text3 = findViewById(R.id.level);
        String temp = "";
        temp = "종류 : " + (String) intent_list.get(3) + " / 난이도 : " + intent_list.get(7);  //3:종류 7:난이도
        text3.setText(temp);

        TextView text4 = findViewById(R.id.detail);
        String temp2 = "";
        temp2 = "조리시간 : " + (String) intent_list.get(4) + " / 칼로리 : " + intent_list.get(5) + " / 양 : " + intent_list.get(6); //4:요리시간 5:칼로리 6:양
        text4.setText(temp2);
    }

    private String process_address = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000228_1/";
    private String ingre_address = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000227_1/";

    private String range;

    public void get_ingredient() {
        ArrayList <ThisAllIngredient> This_list = new ArrayList<>();
        String temp = food_id;
        final int number = Integer.parseInt(temp);

        new Thread() {
            @Override
            public void run() {
                ingredient_items.clear();
                settingListItems.clear();

                //int num = 0;

                if (0 < number && number <= 85) range = "1/999";
                else if (85 < number && number <= 176) range = "1000/1993";
                else if (176 < number && number <= 262) range = "1994/2990";
                else if (262 < number && number <= 359) range = "2991/3985";
                else if (359 < number && number <= 447) range = "3986/4981";
                else if (447 < number && number <= 540) range = "4982/5739";
                else range = "5740/6104";

                String urlAddress = ingre_address + range;

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

                    // jsonData를 먼저 JSONObject 형태로 바꾼다.
                    JSONObject obj = new JSONObject(jsonData);

                    // obj의 "allResult"의 JSONObject를 추출
                    JSONObject allResult = (JSONObject) obj.get("Grid_20150827000000000227_1");

                    // allResult JSONObject에서 "ingredient_List"의 JSONArray 추출
                    JSONArray ingredient_List = (JSONArray) allResult.get("row");

                    for (int i = 0; i < ingredient_List.length(); i++) {
                        JSONObject temp = ingredient_List.getJSONObject(i);
                        String id = temp.getString("RECIPE_ID");

                        if (id.equals(food_id)) {
                            //System.out.println(temp.getString("IRDNT_NM"));

                            String ingredient_name = temp.getString("IRDNT_NM");
                            String quantity = temp.getString("IRDNT_CPCTY");

                            ThisAllIngredient thisAllIngredient = new ThisAllIngredient(ingredient_name, quantity);
                            This_list.add(thisAllIngredient);

                            if(currentUserSettingList.contains(temp.getString("IRDNT_NM"))) {
                                ingredient_items.add(new IngredientInfo(temp.getString("IRDNT_NM")));
                                settingListItems.add(new IngredientInfo(temp.getString("IRDNT_NM")));
                                //setting_items.add(temp.getString("IRDNT_NM"));

                                //ArrayList<String> type_temp = new ArrayList<>();

                                //type_temp.add(temp.getString("IRDNT_NM"));
                                //type_temp.add(temp.getString("IRDNT_TY_NM"));

                                //type_list.add(type_temp);
                            }
                            else
                                ingredient_items.add(new IngredientInfo(temp.getString("IRDNT_NM")));
                        }
                    }

                    //System.out.println("완료3");
                    //System.out.println("총 사이즈 : " + This_list.size());

                    if(settingListItems.size() == 0)
                        settingListItems.add(new IngredientInfo("없습니다 :)"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ingredient_adapter.notifyDataSetChanged();
                            setting_adapter.notifyDataSetChanged();
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

        //재료의 상세 양
        ingredient_adapter.setOnItemClickListener(new IngredientAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final IngredientInfo item = ingredient_items.get(a_position);

                String ingredient_name = item.getProcess();
                Intent intent = new Intent(getApplicationContext(), Ingredient_POPUP.class);
                int temp = 0;
                for(int i=0; i<This_list.size(); i++) {
                    if(This_list.get(i).getIngredient_name().equals(ingredient_name)) {
                        temp = i;
                        break;
                    }
                }

                String data = This_list.get(temp).getQuantity();
                intent.putExtra("ingredient", ingredient_name);
                intent.putExtra("quantity", data);
                startActivity(intent);
            }
        });

        //못 먹는 음식
        setting_adapter.setOnItemClickListener(new IngredientAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(View a_view, int a_position) {
                final IngredientInfo item = settingListItems.get(a_position);

                String ingredient_name = item.getProcess();
                Intent intent = new Intent(getApplicationContext(), PopUpActivity.class);
                intent.putExtra("data", ingredient_name);
                startActivity(intent);
            }
        });
    }

    public void get_process() {
        String temp = food_id;
        final int number = Integer.parseInt(temp);

        new Thread() {
            @Override
            public void run() {
                process_items.clear();

                int num = 0;

                if (0 < number && number <= 183) range = "1/994";
                else if (183 < number && number <= 374) range = "995/1990";
                else if (374 < number && number <= 120476) range = "1991/2987";
                else range = "2988/3022";

                String process_Address = process_address + range;

                try {
                    URL url = new URL(process_Address);

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
                    JSONObject obj = new JSONObject(jsonData);

                    // obj의 "allResult"의 JSONObject를 추출
                    JSONObject allResult = (JSONObject) obj.get("Grid_20150827000000000228_1");

                    // allResult JSONObject에서 "ingredient_List"의 JSONArray 추출
                    JSONArray ingredient_List = (JSONArray) allResult.get("row");

                    int j=0;

                    System.out.println("파싱 진행 중 **** ");

                    for (int i = 0; i < ingredient_List.length(); i++) {
                        JSONObject temp = ingredient_List.getJSONObject(i);
                        String id = temp.getString("RECIPE_ID");

                        if (id.equals(food_id)) {
                            j++;
                            //System.out.println(temp.getString("COOKING_DC"));
                            String naming = j+". "+temp.getString("COOKING_DC");
                            System.out.println(naming);
                            process_items.add(new ProcessInfo(naming));
                        }
                    }

                    System.out.println("완료3");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            process_adapter.notifyDataSetChanged();
                        }
                    });

                    for(int i=0; i< process_items.size(); i++) {
                        System.out.println(process_items.get(i).getProcess());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



    public static class ThisAllIngredient {
        private String ingredient_name;
        private String quantity;

        public ThisAllIngredient(String ingredient_name, String quantity) {
            this.ingredient_name = ingredient_name;
            this.quantity = quantity;
        }

        public String getIngredient_name() {
            return ingredient_name;
        }

        public void setIngredient_name(String ingredient_name) {
            this.ingredient_name = ingredient_name;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }
    }

}