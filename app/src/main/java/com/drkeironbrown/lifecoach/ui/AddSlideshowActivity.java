package com.drkeironbrown.lifecoach.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.drkeironbrown.lifecoach.helper.PathUtils;
import com.drkeironbrown.lifecoach.model.Image;
import com.drkeironbrown.lifecoach.model.Slideshow;
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

public class AddSlideshowActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private RecyclerView rvImage;
    private TfEditText edtSlideshowName;
    private ImageAdapter adapter;
    private List<Image> list;
    private GridLayoutManager gManger;
    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtSelectTime;
    private android.widget.LinearLayout llSelectTime;
    private com.drkeironbrown.lifecoach.custom.TfButton btnAdd;
    private Slideshow slideShow;
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
    private String audioFilePath;
    private TfTextView txtAudio;
    private LinearLayout llSelectAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slideshow);
        llSelectAudio = (LinearLayout) findViewById(R.id.llSelectAudio);
        txtAudio = (TfTextView) findViewById(R.id.txtAudio);
        randomId = new Random().nextInt();
        btnAdd = (TfButton) findViewById(R.id.btnAdd);
        llSelectTime = (LinearLayout) findViewById(R.id.llSelectTime);
        txtSelectTime = (TfTextView) findViewById(R.id.txtSelectTime);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Add vision movie");
        imgBack = (ImageView) findViewById(R.id.imgBack);
        rvImage = (RecyclerView) findViewById(R.id.rvImage);
        edtSlideshowName = (TfEditText) findViewById(R.id.edtSlideshowName);
        gManger = new GridLayoutManager(this, 3);
        rvImage.setLayoutManager(gManger);

        slideShow = (Slideshow) getIntent().getSerializableExtra("slideshow");
        list = new ArrayList<>();
        if (slideShow != null) {
            edtSlideshowName.setText(slideShow.getSlideshowName());
            list = slideShow.getImages();
        }

        adapter = new ImageAdapter(this, list, false, new ImageAdapter.OnClickItem() {
            @Override
            public void onAddImage() {
                TedPermission.with(AddSlideshowActivity.this).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                addImage();
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Functions.showToast(AddSlideshowActivity.this, "You can't add image without permission.", MDToast.TYPE_INFO);
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
                Functions.hideKeyPad(AddSlideshowActivity.this, v);
                if (edtSlideshowName.getText().toString().trim().length() == 0) {
                    Functions.showToast(AddSlideshowActivity.this, "Please enter vision movie name", MDToast.TYPE_INFO);
                    return;
                }
                if (list.size() < 3) {
                    Functions.showToast(AddSlideshowActivity.this, "Please add at least 3 images", MDToast.TYPE_INFO);
                    return;
                }
                Slideshow slideshowReq = new Slideshow();
                slideshowReq.setSlideshowName(edtSlideshowName.getText().toString().trim());
                slideshowReq.setImages(Functions.copyPasteAllImages(list));
                slideshowReq.setAudioPath("");
                if (slideShow != null) {
                    slideshowReq.setAudioPath(slideShow.getAudioPath());
                }
                if (audioFilePath != null && audioFilePath.trim().length() > 0) {
                    slideshowReq.setAudioPath(Functions.copyAudioFile(new File(audioFilePath)));
                }
                AlarmHelper alarmHelper = new AlarmHelper();
                if (slideShow != null) {
                    alarmHelper.setReminder(AddSlideshowActivity.this, slideShow.getNotiId(), GalleryListActivity.class, day, month - 1, year, hour, min, false, slideShow);
                    slideshowReq.setSlideshowId(slideShow.getSlideshowId());
                    DBOpenHelper.updateSlideshow(slideshowReq);
                } else {
                    alarmHelper.setReminder(AddSlideshowActivity.this, randomId, GalleryListActivity.class, day, month - 1, year, hour, min, false, DBOpenHelper.getLastSlideShow());
                    DBOpenHelper.addImages(slideshowReq);
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

        llSelectAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAudioFile();
            }
        });
    }


    private void openTimeDialog() {
        Calendar now = Calendar.getInstance();
        Log.e("month", now.get(Calendar.MONTH) + "");
        tpd = TimePickerDialog.newInstance(
                AddSlideshowActivity.this,
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
                    AddSlideshowActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    AddSlideshowActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }
        dpd.setThemeDark(false);
        dpd.setTitle("Select date");

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
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
                .limit(12) // max images can be selected (99 by default)
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
                image.setImagePath(imageList.get(i).getPath());
                list.add(image);
            }
            adapter.setDataList(list);
            // or get a single image only
//            Image image = ImagePicker.getFirstImageOrNull(data);
        }
        if (requestCode == 8989) {

            if (resultCode == RESULT_OK) {

                //the selected audio.
                Uri uri = data.getData();
                String path = PathUtils.getPath(AddSlideshowActivity.this,uri);
                if (path != null) {
                    File file = new File(path);
                    audioFilePath = file.getAbsolutePath();
                    txtAudio.setText(file.getName());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        day = dayOfMonth;
        month = monthOfYear + 1;
        this.year = year;
        selectedDate = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear + 1) + "/" + year;
        openTimeDialog();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        hour = hourOfDay;
        min = minute;
        selectedTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
        txtSelectTime.setText(selectedDate + " " + selectedTime);
    }

    public void selectAudioFile() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 8989);
    }

}
