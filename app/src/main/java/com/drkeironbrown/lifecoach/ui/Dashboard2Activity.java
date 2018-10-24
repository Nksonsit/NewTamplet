package com.drkeironbrown.lifecoach.ui;

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
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.AdDialog;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.MessageDialog;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.custom.WebViewDialog;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.AlarmHelper;
import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.PaidProduct;
import com.drkeironbrown.lifecoach.model.PaidProductReq;
import com.drkeironbrown.lifecoach.model.PayMoney;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Choose a category and change your life", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes)
                            Functions.fireIntent(Dashboard2Activity.this, CategoriesActivity.class, true);
                    }
                });
            }
        });

        llSlideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSlideshowPaid) {
                    Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Create a slideshow with images of what you will have as you pursue your goals.", new Functions.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            if (isYes)
                                Functions.fireIntent(Dashboard2Activity.this, SlideshowListActivity.class, true);
                        }
                    });

                } else {
                    Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "Pay", "Cancel", "You need to pay $1 to unlock this functionality.", new Functions.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            if (isYes) {
                                PaymentClickType = 2;
                                PayPal.requestOneTimePayment(mBraintreeFragment, new PayPalRequest("1"));
                            }
                        }
                    });
                }

            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGalleryPaid) {
                    Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Construct your Vision Board with images of your future success and happiness", new Functions.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            if (isYes)
                                Functions.fireIntent(Dashboard2Activity.this, GalleryListActivity.class, true);
                        }
                    });

                } else {
                    Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "Pay", "Cancel", "You need to pay $1 to unlock this functionality.", new Functions.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            if (isYes) {
                                PaymentClickType = 1;
                                PayPal.requestOneTimePayment(mBraintreeFragment, new PayPalRequest("1"));
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
                Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Record your thoughts, feelings and progress on your journey to success and happiness.", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes)
                            Functions.fireIntent(Dashboard2Activity.this, JournalListActivity.class, true);
                    }
                });

            }
        });

        llPInspirational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Sometimes your own words are the most encouraging.  Write in your own quotes or messages and have them delivered to you at random times or on a scheduled basis to keep you motivated and on-track.", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes)
                            Functions.fireIntent(Dashboard2Activity.this, PersonalInspirationalActivity.class, true);
                    }
                });

            }
        });

        llInspirational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Have notable quotes sent to you at random times or on a scheduled basis to keep you inspired and motivated!", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes)
                            Functions.fireIntent(Dashboard2Activity.this, InspirationalActivity.class, true);
                    }
                });

            }
        });

        llSecondThought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Sometimes you just want to get things off your chest and just vent! Express your thoughts and feelings here and then securely and safely “Let it go.”", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes)
                            Functions.fireIntent(Dashboard2Activity.this, SecondThoughtActivity.class, true);
                    }
                });

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
                Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "CANCEL", "Are you sure want to logout?", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes){
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
            mBraintreeFragment = BraintreeFragment.newInstance(this, "sandbox_364x39y6_4js5793tp4yg6pz9");
//            mBraintreeFragment = BraintreeFragment.newInstance(this, "sandbox_bsz8s3fp_225qyv663y373339");
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

        PayMoney payMoney = new PayMoney();
        payMoney.setCatId(0);
        payMoney.setAmount("1.0");
        payMoney.setDeviceData(mDeviceData);
        payMoney.setNonce(paymentMethodNonce.getNonce());
        payMoney.setType(PaymentClickType);
        payMoney.setUserId(PrefUtils.getUserFullProfileDetails(Dashboard2Activity.this).getUserId());

        RestClient.get().payMoney(payMoney).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 1) {
                        Functions.showToast(Dashboard2Activity.this, response.body().getMessage(), MDToast.TYPE_SUCCESS);
                        if (PaymentClickType == 1) {
                            isGalleryPaid = true;
                            imgPaidGallery.setVisibility(View.GONE);
                            Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Construct your Vision Board with images of your future success and happiness", new Functions.DialogOptionsSelectedListener() {
                                @Override
                                public void onSelect(boolean isYes) {
                                    if (isYes)
                                        Functions.fireIntent(Dashboard2Activity.this, GalleryListActivity.class, true);
                                }
                            });
                        } else if (PaymentClickType == 2) {
                            isSlideshowPaid = true;
                            Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "OK", "", "Create a slideshow with images of what you will have as you pursue your goals.", new Functions.DialogOptionsSelectedListener() {
                                @Override
                                public void onSelect(boolean isYes) {
                                    if (isYes)
                                        Functions.fireIntent(Dashboard2Activity.this, SlideshowListActivity.class, true);
                                }
                            });
                            imgPaidSlideshow.setVisibility(View.GONE);
                        }
                    } else {
                        Functions.showToast(Dashboard2Activity.this, response.body().getMessage(), MDToast.TYPE_ERROR);
                    }
                } else {
                    Functions.showToast(Dashboard2Activity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Functions.showToast(Dashboard2Activity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
            }
        });


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

    @Override
    protected void onResume() {
        super.onResume();
        PaidProductReq paidProductReq = new PaidProductReq();
        paidProductReq.setUserId(PrefUtils.getUserFullProfileDetails(this).getUserId());
        RestClient.get().getPaidProducts(paidProductReq).enqueue(new Callback<BaseResponse<List<PaidProduct>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PaidProduct>>> call, Response<BaseResponse<List<PaidProduct>>> response) {
                if (response.body() != null && response.body().getStatus() == 1 && response.body().getData() != null && response.body().getData().size() > 0) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        if (response.body().getData().get(i).getType() == 1) {
                            isGalleryPaid = false;
                            imgPaidGallery.setVisibility(View.GONE);
                        }
                        if (response.body().getData().get(i).getType() == 2) {
                            isSlideshowPaid = false;
                            imgPaidSlideshow.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PaidProduct>>> call, Throwable t) {

            }
        });
    }
}
