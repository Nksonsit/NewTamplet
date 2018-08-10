package com.drkeironbrown.lifecoach.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Image;
import com.drkeironbrown.lifecoach.model.Slideshow;
import com.esafirm.imagepicker.features.ImagePicker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class AddSlideshowActivity extends AppCompatActivity {

    private RecyclerView rvImage;
    private TfEditText edtSlideshowName;
    private ImageAdapter adapter;
    private List<Image> list;
    private GridLayoutManager gManger;
    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtLabel;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtSelectTime;
    private android.widget.LinearLayout llSelectTime;
    private com.drkeironbrown.lifecoach.custom.TfButton btnAdd;
    private Slideshow slideShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slideshow);
        btnAdd = (TfButton) findViewById(R.id.btnAdd);
        llSelectTime = (LinearLayout) findViewById(R.id.llSelectTime);
        txtSelectTime = (TfTextView) findViewById(R.id.txtSelectTime);
        txtLabel = (TfTextView) findViewById(R.id.txtLabel);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
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
                    Functions.showToast(AddSlideshowActivity.this, "Please enter mind movie name", MDToast.TYPE_INFO);
                    return;
                }
                if (list.size() < 3) {
                    Functions.showToast(AddSlideshowActivity.this, "Please add at least 3 images", MDToast.TYPE_INFO);
                    return;
                }
                Slideshow slideshow = new Slideshow();
                slideshow.setSlideshowName(edtSlideshowName.getText().toString().trim());
                slideshow.setImages(Functions.copyPasteAllImages(list));
                if (slideShow != null) {
                    DBOpenHelper.updateSlideshow(slideshow);
                } else {
                    DBOpenHelper.addImages(slideshow);
                }
                onBackPressed();
            }
        });

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
        super.onActivityResult(requestCode, resultCode, data);
    }
}
