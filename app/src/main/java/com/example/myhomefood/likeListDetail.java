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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myhomefood.BasicInfoDirectory.BasicInfo;
import com.example.myhomefood.BasicInfoDirectory.BasicInfoDAO;
import com.example.myhomefood.UserDirectory.User;
import com.example.myhomefood.UserDirectory.UserDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class likeListDetail extends AppCompatActivity {

    private RecyclerView basicInfoList;
    private LinearLayoutManager basicInfo_linearLayoutManager;
    private BasicInfoAdapter basicInfo_adapter;
    private ArrayList<BasicItemInfo> basicInfoListItems = new ArrayList<>();

    private RecyclerView ingredientList;
    private GridLayoutManager ingredient_gridLayoutManager;
    private IngredientAdapter ingredient_adapter;
    private ArrayList<IngredientInfo> ingredient_items = new ArrayList<>();

    private RecyclerView settingList;
    private GridLayoutManager setting_gridLayoutManager;
    private IngredientAdapter setting_adapter;
    private ArrayList<IngredientInfo> settingListItems = new ArrayList<>();

    private RecyclerView processList;
    private LinearLayoutManager process_linearLayoutManager;
    private ProcessAdapter process_adapter;
    private ArrayList<ProcessInfo> process_items = new ArrayList<>();

    private String food_id = "1";

    //?????? ???????????? setting list ?????? currentUserSettingList
    private ArrayList<String> currentUserSettingList = new ArrayList<>();

    //????????? ????????? ?????? ArrayList ?????? ??????
    private ArrayList<ArrayList<String>> type_list = new ArrayList<>();
    private ArrayList<String> intent_list = new ArrayList();

    private ImageView foodImage;

    private FirebaseAuth auth;
    private String key;

    //?????????????????? ??????
    UserDAO dao;
    BasicInfoDAO basicInfoDAO;

    String foodName;

    private User currentUser = new User();
    private BasicInfo currentFood = new BasicInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_list_detail);

        foodName = getIntent().getStringExtra("foodName");
        TextView title = findViewById(R.id.detailOfRecipe);
        title.setText(foodName);

        //?????????????????? ?????????
        dao = new UserDAO();
        basicInfoDAO = new BasicInfoDAO();

        GetCurrentUserInfo();

        basicInfoList = findViewById(R.id.basicInfoList);
        basicInfo_linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        basicInfo_adapter = new BasicInfoAdapter(basicInfoListItems);
        basicInfoList.setLayoutManager(basicInfo_linearLayoutManager);
        basicInfoList.setAdapter(basicInfo_adapter);

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

        processList = findViewById(R.id.processList);
        process_linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        process_adapter = new ProcessAdapter(process_items);
        processList.setLayoutManager(process_linearLayoutManager);
        processList.setAdapter(process_adapter);

        foodImage = findViewById(R.id.FoodImage);

        getBasicInfo(foodName);

        auth = FirebaseAuth.getInstance();
        key = null;
    }

    public void getBasicInfo(String foodName) {
        basicInfoDAO.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    BasicInfo basicInfo = data.getValue(BasicInfo.class);

                    if (basicInfo.getRecipe_name().equals(foodName)) {
                        currentFood = new BasicInfo(basicInfo.getRecipe_id(), basicInfo.getRecipe_name(), basicInfo.getSummary(), basicInfo.getNation_name(),
                                basicInfo.getType_name(), basicInfo.getCooking_time(), basicInfo.getCalorie(), basicInfo.getQuantity() , basicInfo.getLevel(),
                                basicInfo.getIngredients_code(), basicInfo.getPrice());
                        break;
                    }
                }

                basicInfoListItems.clear();

                String temp = currentFood.getSummary();
                basicInfoListItems.add(new BasicItemInfo(temp));

                String temp2 = "?????? : " + currentFood.getType_name() + " / ????????? : " + currentFood.getLevel();
                basicInfoListItems.add(new BasicItemInfo(temp2));

                String temp3 = "???????????? : " + currentFood.getCooking_time() + " / ????????? : " + currentFood.getCalorie() + " / ??? : "
                        + currentFood.getQuantity();
                basicInfoListItems.add(new BasicItemInfo(temp3));

                basicInfo_adapter.notifyDataSetChanged();

                get_ingredient();
                get_process();
                set_image();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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

    public void set_image() {
        String url = null;

        try{
            InputStream is = getBaseContext().getResources().getAssets().open("ImageFiles.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if(wb != null) {
                Sheet sheet = wb.getSheet(0);

                if(sheet != null){
                    int colTotal = sheet.getColumns(); //?????? ??????
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

    public void getSettingList(User user) {
        System.out.println("setting list : " + user.getSetting_list().toString());
        currentUserSettingList = user.getSetting_list();
    }

    private String process_address = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000228_1/";
    private String ingre_address = "http://211.237.50.150:7080/openapi/3ff22c028186d9eead169d419bce64386cfa0cdcc26d2ff4ecaf2e071f366b8f/json/Grid_20150827000000000227_1/";
    private String range;

    public void get_ingredient() {
        ArrayList <ThisAllIngredient> This_list = new ArrayList<>();
        food_id = currentFood.getRecipe_id();
        String temp = food_id;
        final int number = Integer.parseInt(temp);

        new Thread() {
            @Override
            public void run() {
                ingredient_items.clear();
                settingListItems.clear();

                int num = 0;

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

                    // jsonData??? ?????? JSONObject ????????? ?????????.
                    JSONObject obj = new JSONObject(jsonData);

                    // obj??? "allResult"??? JSONObject??? ??????
                    JSONObject allResult = (JSONObject) obj.get("Grid_20150827000000000227_1");

                    // allResult JSONObject?????? "ingredient_List"??? JSONArray ??????
                    JSONArray ingredient_List = (JSONArray) allResult.get("row");

                    for (int i = 0; i < ingredient_List.length(); i++) {
                        JSONObject temp = ingredient_List.getJSONObject(i);
                        String id = temp.getString("RECIPE_ID");

                        if (id.equals(food_id)) {
                            System.out.println(temp.getString("IRDNT_NM"));

                            String ingredient_name = temp.getString("IRDNT_NM");
                            String quantity = temp.getString("IRDNT_CPCTY");

                            ThisAllIngredient thisAllIngredient = new ThisAllIngredient(ingredient_name, quantity);
                            This_list.add(thisAllIngredient);

                            if(currentUserSettingList.contains(temp.getString("IRDNT_NM"))) {
                                ingredient_items.add(new IngredientInfo(temp.getString("IRDNT_NM")));
                                settingListItems.add(new IngredientInfo(temp.getString("IRDNT_NM")));
                                //setting_items.add(temp.getString("IRDNT_NM"));

                                ArrayList<String> type_temp = new ArrayList<>();

                                type_temp.add(temp.getString("IRDNT_NM"));
                                type_temp.add(temp.getString("IRDNT_TY_NM"));

                                type_list.add(type_temp);
                            }
                            else
                                ingredient_items.add(new IngredientInfo(temp.getString("IRDNT_NM")));
                        }
                    }

                    System.out.println("??????3");
                    System.out.println("??? ????????? : " + This_list.size());

                    if(settingListItems.size() == 0)
                        settingListItems.add(new IngredientInfo("???????????? :)"));

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

        //????????? ?????? ???
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

        //??? ?????? ??????
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
        food_id = currentFood.getRecipe_id();
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

                    // jsonData??? ?????? JSONObject ????????? ?????????.
                    JSONObject obj = new JSONObject(jsonData);

                    // obj??? "allResult"??? JSONObject??? ??????
                    JSONObject allResult = (JSONObject) obj.get("Grid_20150827000000000228_1");

                    // allResult JSONObject?????? "ingredient_List"??? JSONArray ??????
                    JSONArray ingredient_List = (JSONArray) allResult.get("row");

                    int j=0;

                    System.out.println("?????? ?????? ??? **** ");

                    for (int i = 0; i < ingredient_List.length(); i++) {
                        JSONObject temp = ingredient_List.getJSONObject(i);
                        String id = temp.getString("RECIPE_ID");

                        if (id.equals(food_id)) {
                            j++;
                            System.out.println(temp.getString("COOKING_DC"));
                            String naming = j+". "+temp.getString("COOKING_DC");
                            System.out.println(naming);
                            process_items.add(new ProcessInfo(naming));
                        }
                    }

                    System.out.println("??????3");
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