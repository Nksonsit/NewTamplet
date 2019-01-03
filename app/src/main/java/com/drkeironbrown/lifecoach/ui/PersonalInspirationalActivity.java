package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.PersonalInspirationalAdapter;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.AlarmHelper;
import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;
import com.drkeironbrown.lifecoach.model.PersonalInspiration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonalInspirationalActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TfTextView txtTitle;
    private RelativeLayout toolbar;
    private LinearLayout btnAdd;
    private RecyclerView rvPInspirational;
    private List<PersonalInspiration> list;
    private PersonalInspirationalAdapter adapter;
    private boolean isAddingMode = false;
    private LinearLayout llEmptyView;
    private ImageView imgAdd;
    private ImageView imgSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_inspirational);
        rvPInspirational = (RecyclerView) findViewById(R.id.rvPInspirational);
        llEmptyView = (LinearLayout) findViewById(R.id.llEmptyView);
        btnAdd = (LinearLayout) findViewById(R.id.btnAdd);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Personal inspirational");
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        imgSetting = (ImageView) findViewById(R.id.imgSetting);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rvPInspirational.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        list = DBOpenHelper.getPersonalInspirational();
        if (list.size() == 0) {
            llEmptyView.setVisibility(View.VISIBLE);
            rvPInspirational.setVisibility(View.GONE);
        } else {
            llEmptyView.setVisibility(View.GONE);
            rvPInspirational.setVisibility(View.VISIBLE);
        }

        adapter = new PersonalInspirationalAdapter(this, list, false, new PersonalInspirationalAdapter.OnClick() {
            @Override
            public void onRemoveClick() {
                isAddingMode = false;
                if (list.size() == 0) {
                    llEmptyView.setVisibility(View.VISIBLE);
                    rvPInspirational.setVisibility(View.GONE);
                } else {
                    llEmptyView.setVisibility(View.GONE);
                    rvPInspirational.setVisibility(View.VISIBLE);
                }
//                list = DBOpenHelper.getPersonalInspirational();
//                adapter.setDataList(list);
            }

            @Override
            public void onSaveClick(String trim) {
                PersonalInspiration personalInspiration = new PersonalInspiration();
                personalInspiration.setIsByUser(1);
                personalInspiration.setPInspirational(trim);
                DBOpenHelper.addPersonalInspirational(personalInspiration);
                list.add(0, personalInspiration);
                adapter.notifyItemMoved(0, 1);
                adapter.setAddInMode(false);
                isAddingMode = false;
                AlarmHelper alarmHelper = new AlarmHelper();
                if (!PrefUtils.isPInspirational(PersonalInspirationalActivity.this) && DBOpenHelper.getPInspirationalCount() > 0) {
                    final int min = AppConstant.StartingHour;
                    final int max = AppConstant.EndingHour;
                    final int random = new Random().nextInt((max - min) + 1) + min;
                    PrefUtils.setIsPInspirationalSet(PersonalInspirationalActivity.this, true);
                    if (PrefUtils.getPInspirationalNotiTime(PersonalInspirationalActivity.this) != null) {
                        String tempTime = PrefUtils.getPInspirationalNotiTime(PersonalInspirationalActivity.this);
                        String[] timeSplit = tempTime.split(":");
                        alarmHelper.setReminder(PersonalInspirationalActivity.this, AppConstant.P_INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), false, false);
                    } else {
                        alarmHelper.setReminder(PersonalInspirationalActivity.this, AppConstant.P_INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, random, 0, false, false);
                    }
                }
            }
        });
        rvPInspirational.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEmptyView.setVisibility(View.GONE);
                rvPInspirational.setVisibility(View.VISIBLE);
                if (!isAddingMode) {
                    adapter.setAddInMode(true);
                    isAddingMode = false;
                }
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEmptyView.setVisibility(View.GONE);
                rvPInspirational.setVisibility(View.VISIBLE);
                if (!isAddingMode) {
                    adapter.setAddInMode(true);
                    isAddingMode = false;
                }
            }
        });

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(PersonalInspirationalActivity.this, SettingsActivity.class, true);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
