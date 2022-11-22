package com.example.myhomefood;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectFragment newInstance(String param1, String param2) {
        SelectFragment fragment = new SelectFragment();
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

    public ArrayList<String> List = new ArrayList<>();

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

    private int cnt = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select, container, false);

        GridLayout grain_grid = v.findViewById(R.id.grain_grid);
        GridLayout vegetable_grid = v.findViewById(R.id.vegetable_grid);
        GridLayout fruit_grid = v.findViewById(R.id.fruit_grid);
        GridLayout meat_grid = v.findViewById(R.id.meat_grid);
        GridLayout seafood_grid = v.findViewById(R.id.seafood_grid);
        GridLayout seaweed_grid = v.findViewById(R.id.seaweed_grid);
        GridLayout egg_grid = v.findViewById(R.id.egg_grid);
        GridLayout can_grid = v.findViewById(R.id.can_grid);
        GridLayout noodle_grid = v.findViewById(R.id.noodle_grid);
        GridLayout mushroom_grid = v.findViewById(R.id.mushroom_grid);
        GridLayout etc_grid = v.findViewById(R.id.etc_grid);
        GridLayout source_grid = v.findViewById(R.id.source_grid);
        GridLayout spice_grid = v.findViewById(R.id.spice_grid);
        GridLayout nut_grid = v.findViewById(R.id.nut_grid);
        GridLayout powder_grid = v.findViewById(R.id.powder_grid);


        try{
            InputStream is = getActivity().getBaseContext().getResources().getAssets().open("Ingredients_list.xls");
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
            Button btn = new Button(getActivity());
            btn.setText(grain_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            grain_grid.addView(btn);

        }

        for(int i=0; i<vegetable_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(vegetable_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            vegetable_grid.addView(btn);
        }

        for(int i=0; i<fruit_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(fruit_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            fruit_grid.addView(btn);
        }

        for(int i=0; i<meat_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(meat_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            meat_grid.addView(btn);
        }

        for(int i=0; i<seafood_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(seafood_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            seafood_grid.addView(btn);
        }

        for(int i=0; i<seaweed_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(seaweed_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            seaweed_grid.addView(btn);
        }

        for(int i=0; i<egg_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(egg_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            egg_grid.addView(btn);
        }

        for(int i=0; i<can_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(can_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            can_grid.addView(btn);
        }

        for(int i=0; i<noodle_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(noodle_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            noodle_grid.addView(btn);
        }

        for(int i=0; i<mushroom_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(mushroom_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            mushroom_grid.addView(btn);
        }

        for(int i=0; i<etc_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(etc_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            etc_grid.addView(btn);
        }

        for(int i=0; i<source_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(source_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            source_grid.addView(btn);
        }

        for(int i=0; i<spice_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(spice_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            spice_grid.addView(btn);
        }

        for(int i=0; i<nut_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(nut_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            nut_grid.addView(btn);
        }

        for(int i=0; i<powder_list.size(); i++) {
            Button btn = new Button(getActivity());
            btn.setText(powder_list.get(i));
            btn.setId(cnt);
            cnt++;
            btn.setBackgroundResource(R.drawable.selected_color);
            btn.setOnClickListener(this::onClick);
            powder_grid.addView(btn);
        }

        //onClick 하나씩 다 적용
        final ImageView grain_btn = v.findViewById(R.id.grain_btn);
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

        final ImageView vegetable_btn = v.findViewById(R.id.vegetable_btn);
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

        final ImageView fruit_btn = v.findViewById(R.id.fruit_btn);
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

        final ImageView meat_btn = v.findViewById(R.id.meat_btn);
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

        final ImageView seafood_btn = v.findViewById(R.id.seafood_btn);
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

        final ImageView seaweed_btn = v.findViewById(R.id.seaweed_btn);
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

        final ImageView egg_btn = v.findViewById(R.id.egg_btn);
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

        final ImageView can_btn = v.findViewById(R.id.can_btn);
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

        final ImageView noodle_btn = v.findViewById(R.id.noodle_btn);
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

        final ImageView mushroom_btn = v.findViewById(R.id.mushroom_btn);
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

        final ImageView etc_btn = v.findViewById(R.id.etc_btn);
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

        final ImageView source_btn = v.findViewById(R.id.source_btn);
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

        final ImageView spice_btn = v.findViewById(R.id.spice_btn);
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

        final ImageView nut_btn = v.findViewById(R.id.nut_btn);
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

        final ImageView powder_btn = v.findViewById(R.id.powder_btn);
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

        Button finish_button = v.findViewById(R.id.finish_button);
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("가지고 있는 재료 : " + List.toString());

                Intent intent = new Intent(getActivity().getApplicationContext(), CanCookList.class);
                intent.putExtra("Have_item", List);
                startActivity(intent);
            }
        });

        return v;
    }

    public void onClick(View v) {
        Button btn = v.findViewById(v.getId());
        String s = btn.getText().toString();

        //System.out.println(s);

        v.setSelected(!v.isSelected());

        if(v.isSelected()==true) {
            List.add(s);
        }
        else List.remove(s);
    }

}