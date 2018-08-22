package com.drkeironbrown.lifecoach.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.ImageAdapter;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.custom.TfEditText;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.AlarmHelper;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.NotificationScheduler;
import com.drkeironbrown.lifecoach.model.Gallery;
import com.drkeironbrown.lifecoach.model.Image;
import com.esafirm.imagepicker.features.ImagePicker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AddGalleryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private RecyclerView rvImage;
    private TfEditText edtGalleryName;
    private GridLayoutManager gManger;
    private List<Image> list;
    private ImageAdapter adapter;
    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtSelectTime;
    private android.widget.LinearLayout llSelectTime;
    private TfButton btnAdd;
    private Gallery gallery;
    private DatePickerDialog dpd;
    private String selectedDate;
    private TimePickerDialog tpd;
    private String selectedTime;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int min;
    private int randomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gallery);
        randomId = new Random().nextInt();
        llSelectTime = (LinearLayout) findViewById(R.id.llSelectTime);
        txtSelectTime = (TfTextView) findViewById(R.id.txtSelectTime);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtTitle.setText("Add vision board");
        rvImage = (RecyclerView) findViewById(R.id.rvImage);
        btnAdd = (TfButton) findViewById(R.id.btnAdd);
        edtGalleryName = (TfEditText) findViewById(R.id.edtGalleryName);
        gManger = new GridLayoutManager(this, 3);
        rvImage.setLayoutManager(gManger);
        list = new ArrayList<>();

        gallery = (Gallery) getIntent().getSerializableExtra("gallery");
        list = new ArrayList<>();
        if (gallery != null) {
            edtGalleryName.setText(gallery.getGalleryName());
            list = gallery.getImages();
        }

        adapter = new ImageAdapter(this, list, true, new ImageAdapter.OnClickItem() {
            @Override
            public void onAddImage() {
                TedPermission.with(AddGalleryActivity.this).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                addImage();
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Functions.showToast(AddGalleryActivity.this, "You can't add image without permission.", MDToast.TYPE_INFO);
                            }
                        }).check();
            }
        });
        rvImage.setAdapter(adapter);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(AddGalleryActivity.this, v);

                if (selectedDate == null || selectedDate.trim().length() == 0) {
                    Functions.showToast(AddGalleryActivity.this, "Please select date", MDToast.TYPE_INFO);
                    return;
                }
                if (selectedTime == null || selectedTime.trim().length() == 0) {
                    Functions.showToast(AddGalleryActivity.this, "Please select time", MDToast.TYPE_INFO);
                    return;
                }

                if (edtGalleryName.getText().toString().trim().length() == 0) {
                    Functions.showToast(AddGalleryActivity.this, "Please enter vision board name", MDToast.TYPE_INFO);
                    return;
                }
                if (list.size() < 3) {
                    Functions.showToast(AddGalleryActivity.this, "Please add at least 3 images", MDToast.TYPE_INFO);
                    return;
                }
                Gallery galleryReq = new Gallery();
                galleryReq.setGalleryName(edtGalleryName.getText().toString().trim());
                galleryReq.setImages(list);

                AlarmHelper alarmHelper = new AlarmHelper();
                if (gallery != null) {
                    alarmHelper.setReminder(AddGalleryActivity.this, gallery.getNotiId(), GalleryListActivity.class, day, month-1, year, hour, min,true,gallery);
                    galleryReq.setGalleryId(gallery.getGalleryId());
                    DBOpenHelper.updateGallery(galleryReq);
                } else {
                    alarmHelper.setReminder(AddGalleryActivity.this, randomId, GalleryListActivity.class, day, month-1, year, hour, min,true,DBOpenHelper.getLastGallery());
                    DBOpenHelper.addImagesToGallery(galleryReq);
                }

                onBackPressed();
            }
        });

        txtSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate == null || selectedDate.trim().length() == 0) {
                    openDateDialog();
                } else {
                    openTimeDialog();
                }
            }
        });
    }

    private void openTimeDialog() {
        Calendar now = Calendar.getInstance();
        Log.e("month",now.get(Calendar.MONTH)+"");
        tpd = TimePickerDialog.newInstance(
                AddGalleryActivity.this,
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

    private void openDateDialog() {
        Calendar now = Calendar.getInstance();

        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    AddGalleryActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    AddGalleryActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }
        dpd.setThemeDark(false);
        dpd.setTitle("Select date");

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    private void addImage() {
        ImagePicker.create(this)
//                .returnMode(ReturnMode.GALLERY_ONLY) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                .includeVideo(false) // Show video on image picker
//                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(20) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
//                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
//                .origin(images) // original selected images, used in multi mode
//                .exclude(images) // exclude anything that in image.getPath()
//                .excludeFiles(files) // same as exclude but using ArrayList<File>
                .theme(R.style.ImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
                .enableLog(false) // disabling log
//                .imageLoader(new GrayscaleImageLoder()) // custom image loader, must be serializeable
                .start(); // start image picker activity with request code
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<com.esafirm.imagepicker.model.Image> imageList = ImagePicker.getImages(data);
            for (int i = 0; i < imageList.size(); i++) {
                Image image = new Image();
                Log.e("file", imageList.get(i).getPath());
                if (!new File(imageList.get(i).getPath()).exists()) {
                    Log.e("not", "exist");
                }
                image.setImagePath(imageList.get(i).getPath());
                list.add(image);
            }
            adapter.setDataList(list);
            // or get a single image only
//            Image image = ImagePicker.getFirstImageOrNull(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        day = dayOfMonth;
        month = monthOfYear+1;
        this.year = year;
        selectedDate = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear+1) + "/" + year;
        openTimeDialog();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        hour = hourOfDay;
        min = minute;
        selectedTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
        txtSelectTime.setText(selectedDate + " " + selectedTime);
    }
}
