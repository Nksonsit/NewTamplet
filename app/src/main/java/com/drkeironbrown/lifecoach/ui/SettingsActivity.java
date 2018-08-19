package com.drkeironbrown.lifecoach.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TfTextView txtTitle;
    private RelativeLayout toolbar;
    private TfTextView txtInspirationalTime;
    private LinearLayout llInspirationalTime;
    private TfTextView txtPInspirationalTime;
    private LinearLayout llPInspirationalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        llPInspirationalTime = (LinearLayout) findViewById(R.id.llPInspirationalTime);
        txtPInspirationalTime = (TfTextView) findViewById(R.id.txtPInspirationalTime);
        llInspirationalTime = (LinearLayout) findViewById(R.id.llInspirationalTime);
        txtInspirationalTime = (TfTextView) findViewById(R.id.txtInspirationalTime);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTitle.setText("Settings");

        llInspirationalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                Log.e("month", now.get(Calendar.MONTH) + "");
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                txtInspirationalTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", hourOfDay));
                                PrefUtils.setInspirationalNotiTime(SettingsActivity.this,String.format("%02d", hourOfDay) + ":" + String.format("%02d", hourOfDay));
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.setThemeDark(false);
                tpd.setTitle("TimePicker Title");

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        llPInspirationalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                Log.e("month", now.get(Calendar.MONTH) + "");
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                txtPInspirationalTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", hourOfDay));
                                PrefUtils.setPInspirationalNotiTime(SettingsActivity.this,String.format("%02d", hourOfDay) + ":" + String.format("%02d", hourOfDay));
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.setThemeDark(false);
                tpd.setTitle("TimePicker Title");

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
