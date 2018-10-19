package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.DataCollector;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.BraintreeResponseListener;
import com.braintreepayments.api.interfaces.ConfigurationListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.Configuration;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.AdDialog;
import com.drkeironbrown.lifecoach.custom.MessageDialog;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.custom.WebViewDialog;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.AlarmHelper;
import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;

import java.util.Random;

public class Dashboard2Activity extends AppCompatActivity implements ConfigurationListener,
        PaymentMethodNonceCreatedListener, BraintreeErrorListener, BraintreeCancelListener {


    static final String EXTRA_PAYMENT_RESULT = "payment_result";
    static final String EXTRA_DEVICE_DATA = "device_data";
    static final String EXTRA_COLLECT_DEVICE_DATA = "collect_device_data";
    static final String EXTRA_ANDROID_PAY_CART = "android_pay_cart";

    private static final String EXTRA_AUTHORIZATION = "com.braintreepayments.demo.EXTRA_AUTHORIZATION";
    private static final String EXTRA_CUSTOMER_ID = "com.braintreepayments.demo.EXTRA_CUSTOMER_ID";

    protected BraintreeFragment mBraintreeFragment;

    private boolean mActionBarSetup;
    private String mDeviceData;

    private android.widget.LinearLayout toolbar;
    private android.widget.LinearLayout llCategory;
    private android.widget.LinearLayout llShop;
    private android.widget.LinearLayout llGallery;
    private android.widget.LinearLayout llSlideshow;
    private android.widget.LinearLayout llInspirational;
    private android.widget.LinearLayout llPInspirational;
    private android.widget.LinearLayout llJournal;
    private android.widget.LinearLayout llSecondThought;
    private TfTextView txtTitle;
    private LinearLayout llSettings;
    private LinearLayout llLogout;
    private Random random = new Random();
    private int max = 1000 * 60;
    private int min = 1000 * 20;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isDialogOpen) {
                isDialogOpen = true;
                new AdDialog(Dashboard2Activity.this, new AdDialog.OnDialogClose() {
                    @Override
                    public void dialogClose() {
                        isDialogOpen = false;
                    }
                });
            }
            int randomNumber = random.nextInt(max + 1 - min) + min;
            handler.postDelayed(runnable, randomNumber);
        }
    };
    private Handler handler;
    private boolean isDialogOpen = false;
    private LinearLayout llRef;
    private ImageView imgPaidSlideshow;
    private ImageView imgPaidGallery;
    private boolean isGalleryPaid = true;
    private boolean isSlideshowPaid = true;
    private int PaymentClickType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        PrefUtils.setIsFirstTime(this, false);
        Functions.executeLogcat(this);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        this.imgPaidSlideshow = (ImageView) findViewById(R.id.imgPaidSlideshow);
        this.imgPaidGallery = (ImageView) findViewById(R.id.imgPaidGallery);
        this.llSecondThought = (LinearLayout) findViewById(R.id.llSecondThought);
        this.llJournal = (LinearLayout) findViewById(R.id.llJournal);
        this.llPInspirational = (LinearLayout) findViewById(R.id.llPInspirational);
        this.llInspirational = (LinearLayout) findViewById(R.id.llInspirational);
        this.llSlideshow = (LinearLayout) findViewById(R.id.llSlideshow);
        this.llGallery = (LinearLayout) findViewById(R.id.llGallery);
        this.llShop = (LinearLayout) findViewById(R.id.llShop);
        this.llCategory = (LinearLayout) findViewById(R.id.llCategory);
        this.llSettings = (LinearLayout) findViewById(R.id.llSettings);
        this.llLogout = (LinearLayout) findViewById(R.id.llLogout);
        this.toolbar = (LinearLayout) findViewById(R.id.toolbar);
        this.llRef = (LinearLayout) findViewById(R.id.llRef);
        handler = new Handler();
        llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, CategoriesActivity.class, true);
            }
        });

        llSlideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, SlideshowListActivity.class, true);
            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGalleryPaid) {
                    Functions.fireIntent(Dashboard2Activity.this, GalleryListActivity.class, true);
                } else {
                    Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "Pay", "Cancel", "You need to pay $10 to unlock this functionality.", new Functions.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            if (isYes) {
                                PaymentClickType = 1;
                                PayPal.requestOneTimePayment(mBraintreeFragment, new PayPalRequest("10"));
                            }
                        }
                    });
                }
            }
        });

        llShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, ShopActivity.class, true);
            }
        });

        llJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, JournalListActivity.class, true);
            }
        });

        llPInspirational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, PersonalInspirationalActivity.class, true);
            }
        });

        llInspirational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, InspirationalActivity.class, true);
            }
        });

        llSecondThought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, SecondThoughtActivity.class, true);
            }
        });
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, SettingsActivity.class, true);
            }
        });
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "YES", "NO", "Are you sure want to logout ?", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes) {
                            PrefUtils.setIsFirstTime(Dashboard2Activity.this, true);
                            PrefUtils.setIsLogin(Dashboard2Activity.this, false);

                            PrefUtils.setIsInspirationalSet(Dashboard2Activity.this, false);
                            PrefUtils.setIsPInspirationalSet(Dashboard2Activity.this, false);

                            Functions.fireIntentWithClearFlag(Dashboard2Activity.this, LoginActivity.class, false);
                            finish();
                        }
                    }
                });
            }
        });

        llRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WebViewDialog(Dashboard2Activity.this, "Bibliography", "file:///android_res/raw/ref.html");
            }
        });


        AlarmHelper alarmHelper = new AlarmHelper();
        if (!PrefUtils.isInspirational(this)) {
            final int min = AppConstant.StartingHour;
            final int max = AppConstant.EndingHour;
            final int random = new Random().nextInt((max - min) + 1) + min;
            PrefUtils.setIsInspirationalSet(this, true);
            alarmHelper.setReminder(this, AppConstant.INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, random, 0, false, true);
        }
        if (!PrefUtils.isPInspirational(this) && DBOpenHelper.getPInspirationalCount() > 0) {
            final int min = AppConstant.StartingHour;
            final int max = AppConstant.EndingHour;
            final int random = new Random().nextInt((max - min) + 1) + min;
            PrefUtils.setIsPInspirationalSet(this, true);
            alarmHelper.setReminder(this, AppConstant.P_INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, random, 0, false, false);
        }

        if (getIntent().getStringExtra("msg") != null) {
            new MessageDialog(this, getIntent().getStringExtra("msg"));
        }

        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, "sandbox_bsz8s3fp_225qyv663y373339");
//            mBraintreeFragment = BraintreeFragment.newInstance(this, "sandbox_kswjqspg_b5qng8tnvn3sc48k");
//            mBraintreeFragment = BraintreeFragment.newInstance(this, "sandbox_wvmtjryp_csrx6bnvrd78hyw9");
        } catch (InvalidArgumentException e) {
            onError(e);
        }

        handler.removeCallbacks(runnable);
        int randomNumber = random.nextInt(max + 1 - min) + min;
        handler.postDelayed(runnable, randomNumber);

    }

    @Override
    public void onCancel(int requestCode) {

        Log.e(getClass().getSimpleName(), "Cancel received: " + requestCode);
    }

    @Override
    public void onError(Exception error) {
        Log.e(getClass().getSimpleName(), "Error received (" + error.getClass() + "): " + error.getMessage());
        Log.e(getClass().getSimpleName(), error.toString());
    }

    @Override
    public void onConfigurationFetched(Configuration configuration) {
        DataCollector.collectDeviceData(mBraintreeFragment, new BraintreeResponseListener<String>() {
            @Override
            public void onResponse(String deviceData) {
                mDeviceData = deviceData;
            }
        });

    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {

        Log.e(getClass().getSimpleName(), "Payment Method Nonce received: " + paymentMethodNonce.getTypeLabel());

        Intent intent = new Intent()
                .putExtra(CategoriesActivity.EXTRA_PAYMENT_RESULT, paymentMethodNonce)
                .putExtra(CategoriesActivity.EXTRA_DEVICE_DATA, mDeviceData);
//        setResult(RESULT_OK, intent);
//        finish();

        if (PaymentClickType == 1) {
            isGalleryPaid = true;
            imgPaidGallery.setVisibility(View.GONE);
        } else if (PaymentClickType == 2) {
            isSlideshowPaid = true;
            imgPaidSlideshow.setVisibility(View.GONE);
        }
    }


    public static String getDisplayString(PayPalAccountNonce nonce) {
        return "First name: " + nonce.getFirstName() + "\n" +
                "Last name: " + nonce.getLastName() + "\n" +
                "Email: " + nonce.getEmail() + "\n" +
                "Phone: " + nonce.getPhone() + "\n" +
                "Payer id: " + nonce.getPayerId() + "\n" +
                "Client metadata id: " + nonce.getClientMetadataId() + "\n" +
                "Billing address: " + formatAddress(nonce.getBillingAddress()) + "\n" +
                "Shipping address: " + formatAddress(nonce.getShippingAddress());
    }

    private static String formatAddress(PostalAddress address) {
        return address.getRecipientName() + " " +
                address.getStreetAddress() + " " +
                address.getExtendedAddress() + " " +
                address.getLocality() + " " +
                address.getRegion() + " " +
                address.getPostalCode() + " " +
                address.getCountryCodeAlpha2();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.removeCallbacks(runnable);
        int randomNumber = random.nextInt(max + 1 - min) + min;
        handler.postDelayed(runnable, randomNumber);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
