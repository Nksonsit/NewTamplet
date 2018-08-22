package com.drkeironbrown.lifecoach.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.model.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {
    private static File cacheDir;


    public static void showUserDetails(final Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("User Details");
        //dialog.setMessage(PrefUtils.getUserFullProfileDetails(context).toString() + "\n" + "GCM: " + PrefUtils.getFCMToken(context));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void openYesNoDialog(Context context) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure want to logout?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public static final String ServerDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String HHmmss = "HH:mm:ss";
    public static final String hhmmAMPM = "hh:mm a";
    public static final String ddMMMYYYY = "dd MMM, yyyy";
    //17 Jan, 2017 03:11pm
    public static final String CommonDateTimeFormat = ddMMMYYYY + "" + hhmmAMPM;

    public static final String ServerDateFormat = "yyyy-MM-dd";

    public static DialogOptionsSelectedListener dialogOptionsSelectedListener = null;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static void fireIntent(Activity context, Class cls, boolean isNewActivity) {
        Intent i = new Intent(context, cls);
        context.startActivity(i);
        Activity activity = (Activity) context;
        if (!isNewActivity) {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public static void fireIntent(Context context, Intent intent, boolean isNewActivity) {
        Activity activity = (Activity) context;
        context.startActivity(intent);
        if (!isNewActivity) {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public static void fireIntent(Activity context, boolean isNewActivity) {
        context.finish();
        if (!isNewActivity) {
            context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public static void fireIntentForResult(Activity context, Class<?> cls, int requestCode, boolean isNewActivity) {

        Intent intent = new Intent(context, cls);
        context.startActivityForResult(intent, requestCode);
        if (!isNewActivity) {
            context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public static void fireIntentWithClearFlag(Activity context, Class cls, boolean isNewActivity) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if (!isNewActivity) {
            context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }

    public static void hideKeyPad(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static Typeface getRegularFont(Context _context) {
        Typeface tf = Typeface.createFromAsset(_context.getAssets(), "fonts/regular.ttf");
        return tf;
    }

    public static Typeface getBoldFont(Context _context) {
        Typeface tf = Typeface.createFromAsset(_context.getAssets(), "fonts/bold.ttf");
        return tf;
    }

    public static boolean emailValidation(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String toStr(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static int toLength(EditText editText) {
        return editText.getText().toString().trim().length();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void openInMap(Context context, double latitude, double longitude, String labelName) {
        String newUri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + labelName + ")";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newUri));
        context.startActivity(intent);
    }

    public static void makePhoneCall(Context context, String callNo) {
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + callNo));
        context.startActivity(dialIntent);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public static String parseDate2(String inputDate, SimpleDateFormat outputFormat, SimpleDateFormat inputFormat) {

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static void showToast(Context context, String message, int type) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, type);
        mdToast.show();
    }

    public static void loadImage(final Context context, String url, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void loadImage(final Context context, File file, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(file)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void loadCircularImage(final Context context, Drawable url, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions().circleCropTransform())
                .into(imageView);
    }

    public static void loadCircularImage(final Context context, int id, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(id)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions().circleCropTransform())
                .into(imageView);
    }

    public static void loadCircularImage(final Context context, Bitmap url, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions().circleCropTransform())
                .into(imageView);
    }

    public static void loadCircularImage(final Context context, String url, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions().circleCropTransform())
                .into(imageView);
    }

    public static void loadImage(final Context context, int drawable, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(drawable)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null)
                            progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }


    public static String checkString(String input) {
        if (input != null && input.trim().toString().length() > 0 && !input.toString().toLowerCase().equals("null")) {
            return input;
        } else {
            return "";
        }
    }

    public static void showAlertDialogWithTwoOption(Context mContext, String positiveText, String negativeText, String message, final DialogOptionsSelectedListener dialogOptionsSelectedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(true);

        if (positiveText.trim().length() > 0) {
            builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialogOptionsSelectedListener != null)
                        dialogOptionsSelectedListener.onSelect(true);
                    dialog.dismiss();
                }
            });
        }
        if (negativeText.trim().length() > 0) {
            builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialogOptionsSelectedListener != null)
                        dialogOptionsSelectedListener.onSelect(false);
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
    }

    public static void hideStatusBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static float getLeftPadding(Context context, float textWidth) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return (width / 2) - (textWidth / 2);
    }

    public static List<Image> copyPasteAllImages(List<Image> list) {
        List<Image> imageList = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            try {
                File mainDir = new File(Environment.getExternalStorageDirectory(), "/LifeCoach");
                if (!mainDir.exists()) {
                    mainDir.mkdir();
                }
                File file = new File(mainDir.getAbsoluteFile(), "username_" + (time + i) + ".jpg");

                Log.e("file", file.getAbsolutePath());

                InputStream in = null;
                in = new FileInputStream(new File(list.get(i).getImagePath()));

                OutputStream out = null;
                out = new FileOutputStream(file);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();


                Image image = new Image();
                image.setImagePath(file.getAbsolutePath());
                imageList.add(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageList;
    }


    public static String copyAudioFile(File file) {
        File newfile = null;
        try {
            File mainDir = new File(Environment.getExternalStorageDirectory(), "/LifeCoach");
            if (!mainDir.exists()) {
                mainDir.mkdir();
            }
            newfile = new File(mainDir.getAbsoluteFile(), "username_" + System.currentTimeMillis() + ".mp3");

            Log.e("input file", file.getAbsolutePath());
            Log.e("output file", newfile.getAbsolutePath());

            InputStream in = null;
            in = new FileInputStream(file);

            OutputStream out = null;
            out = new FileOutputStream(newfile);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newfile != null && newfile.exists()) {
            return newfile.getAbsolutePath();
        } else {
            return "";
        }
    }

    public interface DialogOptionsSelectedListener {
        void onSelect(boolean isYes);
    }


    public static void executeLogcat(Context context) {
        Log.d("System out", "Create Log file..");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir = new File(Environment.getExternalStorageDirectory(), "BadgerAppLog");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();

        File logFile = new File(cacheDir, "logs badger app.log"); // log file name
        int sizePerFile = 60; // size in kilobytes
        int rotationCount = 10; // file rotation count
        String filter = "D"; // Debug priority
        String[] args = new String[]{"logcat",
                "-v", "time",
                "-f", logFile.getAbsolutePath(),
                "-r", Integer.toString(sizePerFile),
                "-n", Integer.toString(rotationCount),
                "*:" + filter};
        try {
            Runtime.getRuntime().exec(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SpacialCharacterNotAllow(EditText editText) {
        if (editText.getText().toString().length() > 0) {

            char x;
            int[] t = new int[editText.getText().toString()
                    .length()];

            for (int i = 0; i < editText.getText().toString()
                    .length(); i++) {
                x = editText.getText().toString().charAt(i);
                int z = (int) x;
                t[i] = z;

                if ((z > 64 && z < 91) || z == 32
                        || (z > 96 && z < 123)) {
                } else {

                    editText.setText(editText
                            .getText()
                            .toString()
                            .substring(
                                    0,
                                    (editText.getText()
                                            .toString().length()) - 1));
                    editText.setSelection(editText
                            .getText().length());
                }
                Log.d("System out", "decimal value of character" + z);

            }
        }
    }

}