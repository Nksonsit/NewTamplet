package com.drkeironbrown.lifecoach.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.custom.WebViewDialog;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.AdvancedSpannableString;
import com.drkeironbrown.lifecoach.helper.AlarmHelper;
import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.UpdateNotificationReq;
import com.drkeironbrown.lifecoach.model.User;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TfTextView txtTitle;
    private RelativeLayout toolbar;
    private TfTextView txtInspirationalTime;
    private LinearLayout llInspirationalTime;
    private TfTextView txtPInspirationalTime;
    private LinearLayout llPInspirationalTime;
    private android.widget.CheckBox cbGetMail2;
    private TfTextView txtReadMore;
    private AlarmHelper alarmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.txtReadMore = (TfTextView) findViewById(R.id.txtReadMore);
        this.cbGetMail2 = (CheckBox) findViewById(R.id.cbGetMail2);
        llPInspirationalTime = (LinearLayout) findViewById(R.id.llPInspirationalTime);
        txtPInspirationalTime = (TfTextView) findViewById(R.id.txtPInspirationalTime);
        llInspirationalTime = (LinearLayout) findViewById(R.id.llInspirationalTime);
        txtInspirationalTime = (TfTextView) findViewById(R.id.txtInspirationalTime);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        alarmHelper = new AlarmHelper();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTitle.setText("Settings");

        if (PrefUtils.getInspirationalNotiTime(SettingsActivity.this) != null) {
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(PrefUtils.getInspirationalNotiTime(SettingsActivity.this));
                txtInspirationalTime.setText(new SimpleDateFormat("K:mm").format(dateObj));
            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }

        if (PrefUtils.getPInspirationalNotiTime(SettingsActivity.this) != null) {
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(PrefUtils.getPInspirationalNotiTime(SettingsActivity.this));
                txtPInspirationalTime.setText(new SimpleDateFormat("K:mm").format(dateObj));
            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }


        llInspirationalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                Log.e("month", now.get(Calendar.MONTH) + "");
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                try {
                                    final SimpleDateFormat sdf = new SimpleDateFormat("K:mm");
                                    final Date dateObj = sdf.parse(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                                    txtInspirationalTime.setText(sdf.format(dateObj));
//                                    Log.e("time2",new SimpleDateFormat("H:mm").format(dateObj));
                                    PrefUtils.setInspirationalNotiTime(SettingsActivity.this, new SimpleDateFormat("H:mm").format(dateObj));
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }

                                String tempTime = PrefUtils.getInspirationalNotiTime(SettingsActivity.this);
                                String[] timeSplit = tempTime.split(":");
                                alarmHelper.setReminder(SettingsActivity.this, AppConstant.INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), false, true);
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
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

                                try {
                                    final SimpleDateFormat sdf = new SimpleDateFormat("K:mm");
                                    final Date dateObj = sdf.parse(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                                    txtPInspirationalTime.setText(sdf.format(dateObj));
//                                    Log.e("time",new SimpleDateFormat("H:mm").format(dateObj)+"  " + sdf.format(dateObj));
                                    PrefUtils.setPInspirationalNotiTime(SettingsActivity.this, new SimpleDateFormat("H:mm").format(dateObj));
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }

                                if (DBOpenHelper.getPInspirationalCount() > 0) {
                                    String tempTime = PrefUtils.getPInspirationalNotiTime(SettingsActivity.this);
                                    String[] timeSplit = tempTime.split(":");
                                    alarmHelper.setReminder(SettingsActivity.this, AppConstant.P_INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), false, false);
                                }
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
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

        AdvancedSpannableString readMore = new AdvancedSpannableString("Read more");
        readMore.setUnderLine("Read more");
        txtReadMore.setText(readMore);
        txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebViewDialog(SettingsActivity.this, "Otp - In", "file:///android_res/raw/optin.html");
            }
        });

        if (PrefUtils.getUserFullProfileDetails(this).getNoti() == 1) {
            cbGetMail2.setChecked(true);
        } else {
            cbGetMail2.setChecked(false);
        }

        cbGetMail2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Functions.isConnected(SettingsActivity.this)) {
                    final UpdateNotificationReq updateNotificationReq = new UpdateNotificationReq();
                    updateNotificationReq.setGetNotification(isChecked ? 1 : 0);
                    updateNotificationReq.setUserId(PrefUtils.getUserFullProfileDetails(SettingsActivity.this).getUserId());
                    RestClient.get().updateNotification(updateNotificationReq).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body() != null && response.body().getStatus() == 1) {
                                User user = PrefUtils.getUserFullProfileDetails(SettingsActivity.this);
                                user.setNoti(updateNotificationReq.getGetNotification());
                                PrefUtils.setUserFullProfileDetails(SettingsActivity.this, user);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
