package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.drkeironbrown.lifecoach.adapter.CategoryAdapter;
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.AdDialog;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity implements ConfigurationListener,
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
    private int selectedPos = -1;


    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;
    private android.support.v7.widget.RecyclerView rvCategories;
    private android.widget.LinearLayout llEmptyView;
    private List<Category> list;
    private CategoryAdapter adapter;
    private TfTextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        this.llEmptyView = (LinearLayout) findViewById(R.id.llEmptyView);
        this.rvCategories = (RecyclerView) findViewById(R.id.rvCategories);
        this.toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        this.txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        this.txtEmpty = (TfTextView) findViewById(R.id.txtEmpty);
        this.imgBack = (ImageView) findViewById(R.id.imgBack);
        handler = new Handler();

        list = new ArrayList<>();
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(this, list, new CategoryAdapter.OnBuyClick() {
            @Override
            public void onBuyClick(int pos) {
                selectedPos = pos;
                PayPal.requestOneTimePayment(mBraintreeFragment, new PayPalRequest(list.get(pos).getCategoryPrice()));
            }
        });
        rvCategories.setAdapter(adapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtTitle.setText("Categories");

        if (Functions.isConnected(this)) {
            RestClient.get().getCategories().enqueue(new Callback<BaseResponse<List<Category>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<Category>>> call, Response<BaseResponse<List<Category>>> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1 && response.body().getData() != null) {
                            list = response.body().getData();
                            adapter.setDataList(list);
                            rvCategories.setVisibility(View.VISIBLE);
                            llEmptyView.setVisibility(View.GONE);

                        } else {
                            rvCategories.setVisibility(View.GONE);
                            txtEmpty.setVisibility(View.VISIBLE);

                        }
                    } else {
                        rvCategories.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<Category>>> call, Throwable t) {
                    rvCategories.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Functions.showToast(CategoriesActivity.this, getString(R.string.check_internet), MDToast.TYPE_ERROR);
            rvCategories.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText(R.string.check_internet);
        }


        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, "sandbox_364x39y6_4js5793tp4yg6pz9s");
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
    public void onBackPressed() {
        Functions.fireIntent(this, false);
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

        list.get(selectedPos).setCategoryPrice("0");
        adapter.setDataList(list);
    }

   /* private PayPalRequest getPayPalRequest(@Nullable String amount) {
        PayPalRequest request = new PayPalRequest(amount);

        request.displayName(Functions.getPayPalDisplayName(this));

        String landingPageType = Functions.getPayPalLandingPageType(this);
        if (getString(R.string.paypal_landing_page_type_billing).equals(landingPageType)) {
            request.landingPageType(PayPalRequest.LANDING_PAGE_TYPE_BILLING);
        } else if (getString(R.string.paypal_landing_page_type_login).equals(landingPageType)) {
            request.landingPageType(PayPalRequest.LANDING_PAGE_TYPE_LOGIN);
        }

        String intentType = Functions.getPayPalIntentType(this);
        if (intentType.equals(getString(R.string.paypal_intent_authorize))) {
            request.intent(PayPalRequest.INTENT_AUTHORIZE);
        } else if (intentType.equals(getString(R.string.paypal_intent_order))) {
            request.intent(PayPalRequest.INTENT_ORDER);
        } else if (intentType.equals(getString(R.string.paypal_intent_sale))) {
            request.intent(PayPalRequest.INTENT_SALE);
        }

        if (Functions.isPayPalUseractionCommitEnabled(this)) {
            request.userAction(PayPalRequest.USER_ACTION_COMMIT);
        }

        if (Functions.isPayPalCreditOffered(this)) {
            request.offerCredit(true);
        }

        if (Functions.usePayPalAddressOverride(this)) {
            request.shippingAddressOverride(new PostalAddress()
                    .recipientName("Brian Tree")
                    .streetAddress("123 Fake Street")
                    .extendedAddress("Floor A")
                    .locality("San Francisco")
                    .region("CA")
                    .countryCodeAlpha2("US")
            );
        }

        return request;
    }*/

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

    private Random random = new Random();
    private int max = 1000 * 60;
    private int min = 1000 * 20;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isDialogOpen) {
                isDialogOpen = true;
                new AdDialog(CategoriesActivity.this, new AdDialog.OnDialogClose() {
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
