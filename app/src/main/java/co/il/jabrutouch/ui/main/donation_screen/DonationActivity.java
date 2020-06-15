package co.il.jabrutouch.ui.main.donation_screen;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.jabrutouch.utils.ActivityRunning;
import co.il.jabrutouch.utils.FragmentHelper;
import co.il.model.model.DonationData;
import co.il.model.model.Payment;
import co.il.model.model.Result;
import co.il.model.model.UserDonationStatus;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static co.il.jabrutouch.MainActivity.START_SUB_DETAILS;
import static co.il.jabrutouch.MainActivity.USER_DONATION_STATUS;
import static co.il.jabrutouch.ui.main.donation_screen.DedicationFragment.DONATION_DATA;
import static co.il.jabrutouch.ui.main.donation_screen.DedicationFragment.PAYMENT;


public class DonationActivity extends AppCompatActivity implements
        DonationPaymentFragment.OnDonationPaymentFragmentListener,
        DedicationFragment.OnDedicationFragmentListener,
        FirstDedicationViewPagerFragment.OnFirstDedicationViewPagerFragmentListener,
        PaymentFragment.OnPaymentFragmentListener,
        OthersDedicationViewPagerFragment.OnOthersDedicationViewPagerFragmentListener,
        CreditCardDetailsFragment.OnCreditCardDetailsFragmentListener,
        CreditCardPaymentFragment.OnCreditCardPaymentFragmentListener,
        PaymentSuccesFragment.OnPaymentSuccesFragmentListener,
        PaymentFailedFragment.OnPaymentFailedFragmentListener,
        DonationDetailsFragment.OnDonationDetailsFragmentListener,
        DonationVideoDialogFragment.OnDonationVideoDialogFragmentListener {

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private FragmentHelper mFragmentHelper;
    private DonationPaymentFragment mDonationPaymentFragment;
    private DedicationFragment mDedicationFragment;
    private TextView mTitleTV;
    private PaymentFragment mPaymentFragment;
    private PaymentsClient mPaymentsClient;
    private CreditCardDetailsFragment mCreditCardDetailsFragment;
    private CreditCardPaymentFragment mCreditCardPaymentFragment;
    private PaymentSuccesFragment mPaymentSuccesFragment;
    private PaymentFailedFragment mPaymentFailedFragment;
    private RelativeLayout mArrowBackRL;
    private DonationData mDonationData;
    private boolean mStartSubDetails;
    private DonationDetailsFragment mDonationDetailsFragment;
    private DonationVideoDialogFragment mDonationVideoDialogFragment;
    private RelativeLayout mTopBArRL;
    private FrameLayout mAllViewFL;
    private LinearLayout mAllViewLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);


        initViews();
        initListeners();
        getDonationDataObjectFromLocalStorage();
        startDonationPaymentFragment();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            setGooglePay();
//        }

    }


    private void initViews() {


        mFragmentHelper = new FragmentHelper(this, new ActivityRunning());
        mTitleTV = findViewById(R.id.DA_title_TV);
        mArrowBackRL = findViewById(R.id.DA_arrow_back_RL);
        mTopBArRL = findViewById(R.id.DA_top_bar_RL);
        mAllViewFL = findViewById(R.id.DA_content_frame_FL);
        mAllViewLL = findViewById(R.id.DA_all_view_LL);
        mStartSubDetails = getIntent().getBooleanExtra(START_SUB_DETAILS, false);
    }


    private void startDonationPaymentFragment() {

        if (!mStartSubDetails) {


            if (!UserManager.getNotFirstTimeInDonationFirstTime(this)) {

                if (mDonationVideoDialogFragment == null) {
                    mDonationVideoDialogFragment = DonationVideoDialogFragment.newInstance();
                }


                //            mTopBArRL.setVisibility(View.GONE);
                mAllViewFL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


                mFragmentHelper.addFragment(R.id.DA_content_frame_FL, mDonationVideoDialogFragment, DonationVideoDialogFragment.TAG, null);
            } else {

                if (mDonationPaymentFragment == null) {
                    mDonationPaymentFragment = DonationPaymentFragment.newInstance(mDonationData);
                }

                mTitleTV.setText(getResources().getString(R.string.Tzedaka));
                mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mDonationPaymentFragment, DonationPaymentFragment.TAG, null);

                mFragmentHelper.removeFragment(mDonationVideoDialogFragment);
                mTopBArRL.setVisibility(View.VISIBLE);
                mAllViewFL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 595));

            }

        } else {

            UserDonationStatus userDonationStatus = (UserDonationStatus) getIntent().getSerializableExtra(USER_DONATION_STATUS);

            if (mDonationDetailsFragment == null) {
                mDonationDetailsFragment = DonationDetailsFragment.newInstance(userDonationStatus);
            }

            mTitleTV.setText(getResources().getString(R.string.change_details));
            mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mDonationDetailsFragment, DonationDetailsFragment.TAG, null);


        }
    }


    @Override
    public void onContinueBtnPressed(Payment payment, DonationData mDonationData) {


//        if (mDedicationFragment == null) {
        mDedicationFragment = DedicationFragment.newInstance(payment, mDonationData);
//        }

        mTitleTV.setText(getResources().getString(R.string.dedication));
        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mDedicationFragment, DedicationFragment.TAG, DedicationFragment.TAG);


    }


    @Override
    public void changeTitle() {

        mTitleTV.setText(getResources().getString(R.string.Tzedaka));

    }


    @Override
    public void onDedicationContinueBtnClicked(Payment mPaymentObject, DonationData mDonationData) {


        RequestManager.sendUserPayment(UserManager.getToken(this), mPaymentObject).subscribe(new Observer<Result<Payment>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<Payment> paymentResult) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        Intent returnIntent = new Intent();
        returnIntent.putExtra(PAYMENT, mPaymentObject);
        returnIntent.putExtra(DONATION_DATA, mDonationData);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();


//        mFragmentHelper.removeFromBackStack(PaymentFailedFragment.TAG, CreditCardPaymentFragment.TAG, CreditCardDetailsFragment.TAG);
//        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mCreditCardDetailsFragment, CreditCardDetailsFragment.TAG, CreditCardDetailsFragment.TAG);


//        if (mPaymentFragment == null) {
//            mPaymentFragment = PaymentFragment.newInstance();
//        }
//
//        mTitleTV.setText(getResources().getString(R.string.payment));
//        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mPaymentFragment, PaymentFragment.TAG, PaymentFragment.TAG);


    }


    private void initListeners() {


        mArrowBackRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setViewClickedAnimation(mArrowBackRL);
                onBackPressed();

            }
        });


    }


    private void getDonationDataObjectFromLocalStorage() {

        Gson gson = new Gson();
        mDonationData = gson.fromJson(UserManager.getDonationData(this), DonationData.class);


    }


    private void setViewClickedAnimation(View mView) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        mView.startAnimation(animation);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setGooglePay() {


        mPaymentsClient = PaymentsUtil.createPaymentsClient(this);

    }


    @Override
    // This method is called when the Pay with Google button is clicked.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestGooglePayPayment(View view) {
        // Disables the button to prevent multiple clicks.
//        mGooglePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        String price = PaymentsUtil.microsToString(1000 + 100);

        // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price);
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }
        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    mPaymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }


    @Override
    public void requestPayPalPayment(View view) {


    }


    @Override
    public void onCreditCardPressed() {


        if (mCreditCardDetailsFragment == null) {
            mCreditCardDetailsFragment = CreditCardDetailsFragment.newInstance();
        }

        mTitleTV.setText(getResources().getString(R.string.payment));
        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mCreditCardDetailsFragment, CreditCardDetailsFragment.TAG, CreditCardDetailsFragment.TAG);


    }


    @Override
    public void onSendPaymentClicked() {


        if (mCreditCardPaymentFragment == null) {
            mCreditCardPaymentFragment = CreditCardPaymentFragment.newInstance();
        }

        mTitleTV.setText(getResources().getString(R.string.payment));
        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mCreditCardPaymentFragment, CreditCardPaymentFragment.TAG, CreditCardPaymentFragment.TAG);


    }


    @Override
    public void onReplaceCardClicked() {


        onBackPressed();

    }


    @Override
    public void onBackPressed() {

        if (mFragmentHelper.isCurrent(DonationPaymentFragment.TAG)){

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();

        }else {
            super.onBackPressed();
        }

    }

    @Override
    public void onPaymentSucced() {


        if (mPaymentSuccesFragment == null) {
            mPaymentSuccesFragment = PaymentSuccesFragment.newInstance();
        }

        mTitleTV.setText(getResources().getString(R.string.payment));
        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mPaymentSuccesFragment, PaymentSuccesFragment.TAG, PaymentSuccesFragment.TAG);


    }

    @Override
    public void onPaymentField() {


        if (mPaymentFailedFragment == null) {
            mPaymentFailedFragment = PaymentFailedFragment.newInstance();
        }

        mTitleTV.setText(getResources().getString(R.string.payment));
        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mPaymentFailedFragment, PaymentFailedFragment.TAG, PaymentFailedFragment.TAG);


    }


    @Override
    public void onDoneSuccesBtnClicked() {

        // TODO


    }


    @Override
    public void onTryAgainClicked() {


        if (mCreditCardDetailsFragment == null) {
            mCreditCardDetailsFragment = CreditCardDetailsFragment.newInstance();
        }

        mTitleTV.setText(getResources().getString(R.string.payment));
        mFragmentHelper.removeFromBackStack(PaymentFailedFragment.TAG, CreditCardPaymentFragment.TAG, CreditCardDetailsFragment.TAG);
        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mCreditCardDetailsFragment, CreditCardDetailsFragment.TAG, CreditCardDetailsFragment.TAG);

    }


    @Override
    public void saveEditNameForAllPager(String donationName) {

        mDedicationFragment.onDonationNameChanges(donationName);
        mDedicationFragment.saveEditNameForAllPager();

    }


    @Override
    public void onDedicationNameChanges(String dedicationName) {

        mDedicationFragment.onDedicationNameChanges(dedicationName);

    }

    @Override
    public void hideTopView() {
        mDedicationFragment.hideTopView();
    }

 @Override
    public void showTopView() {
        mDedicationFragment.showTopView();
    }





    @Override
    public void onSkipVideoClicked() {

        UserManager.setNotFirstTimeInDonationFirstTime(true, this);

        if (mDonationPaymentFragment == null) {
            mDonationPaymentFragment = DonationPaymentFragment.newInstance(mDonationData);
        }

        mTitleTV.setText(getResources().getString(R.string.Tzedaka));
        mFragmentHelper.replaceFragment(R.id.DA_content_frame_FL, mDonationPaymentFragment, DonationPaymentFragment.TAG, null);

        mFragmentHelper.removeFragment(mDonationVideoDialogFragment);
        mTopBArRL.setVisibility(View.VISIBLE);
        mAllViewFL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 595));


    }




    @Override
    public void onContactUsClicked() {


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;
                    case Activity.RESULT_CANCELED:
                        // Nothing to here normally - the user simply cancelled without selecting a
                        // payment method.
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                    default:
                        // Do nothing.
                }

                // Re-enables the Google Pay payment button.
//                mGooglePayButton.setClickable(true);
                break;
        }


    }


    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#PaymentData">Payment
     * Data</a>
     */
    private void handlePaymentSuccess(PaymentData paymentData) {
        String paymentInformation = paymentData.toJson();

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        if (paymentInformation == null) {
            return;
        }
        JSONObject paymentMethodData;

        try {
            paymentMethodData = new JSONObject(paymentInformation).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("type")
                    .equals("PAYMENT_GATEWAY")
                    && paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
                    .equals("examplePaymentMethodToken")) {
                AlertDialog alertDialog =
                        new AlertDialog.Builder(this)
                                .setTitle("Warning")
                                .setMessage(
                                        "Gateway name set to \"example\" - please modify "
                                                + "Constants.java and replace it with your own gateway.")
                                .setPositiveButton("OK", null)
                                .create();
                alertDialog.show();
            }

            String billingName =
                    paymentMethodData.getJSONObject("info").getJSONObject("billingAddress").getString("name");
            Log.d("BillingName", billingName);
            Toast.makeText(this, billingName, Toast.LENGTH_LONG)
                    .show();

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData.getJSONObject("tokenizationData").getString("token"));
        } catch (JSONException e) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString());
            return;
        }
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     *                   WalletConstants.ERROR_CODE_* constants.
     * @see <a
     * href="https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants#constant-summary">
     * Wallet Constants Library</a>
     */
    private void handleError(int statusCode) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }


}
