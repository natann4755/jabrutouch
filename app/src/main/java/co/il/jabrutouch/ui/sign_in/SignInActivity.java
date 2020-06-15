package co.il.jabrutouch.ui.sign_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import java.util.Objects;
import co.il.jabrutouch.MainActivity;
import co.il.jabrutouch.R;
import co.il.jabrutouch.SplashActivity;
import co.il.jabrutouch.server.RequestManager;
import co.il.model.model.DonationData;
import co.il.model.model.ErrorResponse;
import co.il.jabrutouch.ui.main.profile_screen.EmailSentDialog;
import co.il.jabrutouch.ui.main.profile_screen.ForgotPassDialog;
import co.il.jabrutouch.ui.main.profile_screen.NewRegistrationDialog;
import co.il.model.model.SendMail;
import co.il.model.model.SendMailResponse;
import co.il.model.model.TodayDafYomiDetailes;
import co.il.model.model.PagesItem;
import co.il.model.model.User;
import co.il.model.model.masechet_list_model.MasechetList;
import co.il.model.model.RequestLogIn;
import co.il.model.model.Result;
import co.il.jabrutouch.ui.sign_up.SignUpActivity;
import co.il.jabrutouch.user_manager.UserManager;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import static co.il.jabrutouch.ui.main.wall_screen.WallFragment.DAF_YOMI_DETAILES;
import static co.il.jabrutouch.ui.sign_up.SignUpActivity.isEmailValid;





public class SignInActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener, ForgotPassDialog.ForgotPassDialogListener,
        EmailSentDialog.EmailSentDialogListener, NewRegistrationDialog.NewRegistrationDialogListener {


    private static final String ANDROID = "ANDROID";
    private static final String JABRUTOUCH_MAIL = "app@dafyomi.es";
    private static final String TAG = SignInActivity.class.getSimpleName();
    private TextView mSignUpTV;
    private EditText mEmailOrPhoneET;
    private EditText mPasswordET;
    private Button mSignInBTN;
    private ProgressBar mProgressBar;
    private TextView mForgotPass;
    private MasechetList mMasechtotList;
    private PagesItem mDafYomiDetailes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        initViews();
        initListeners();
        getMasechtotListFromServer();


    }


    /**
     * init views
     */
    private void initViews() {

        mSignUpTV = findViewById(R.id.SIA_sign_up_TV);
        mEmailOrPhoneET = findViewById(R.id.SIA_email_or_phone_ET);
        mPasswordET = findViewById(R.id.SIA_password_ET);
        mSignInBTN = findViewById(R.id.SIA_sign_in_BTN);
        mProgressBar = findViewById(R.id.SIA_progress_bar);
        mForgotPass = findViewById(R.id.SIA_forgot_pass_TV);

    }




    /**
     * init listeners
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {

        mSignInBTN.setOnClickListener(this);
        mSignUpTV.setOnClickListener(this);
        mEmailOrPhoneET.setOnTouchListener(this);
        mPasswordET.setOnTouchListener(this);
        mForgotPass.setOnClickListener(this);
    }





    /**
     * get masechtot list only in the first time in the app
     */
    private void getMasechtotListFromServer() {


        RequestManager.getMasechtotList().subscribe(new Observer<Result<MasechetList>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<MasechetList> masechetListResult) {

                saveMasechtotList(masechetListResult.getData());
                mMasechtotList = masechetListResult.getData();

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }





    /**
     * save MasechetList Object
     *
     * @param masechetList, masechetList
     */
    private void saveMasechtotList(MasechetList masechetList) {

        Gson gson = new Gson();
        String jsonMasechtotList = gson.toJson(masechetList);

        UserManager.setMasechtotList(jsonMasechtotList, this);

    }





    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.SIA_sign_in_BTN:

                setButtonClickedAnimiation(mSignInBTN);

                onSignInBtnClicked();

                break;


            case R.id.SIA_sign_up_TV:

                setImageClickedAnimation(mSignUpTV);

                Intent signUpActivityIntent = new Intent(this, SignUpActivity.class);
                startActivity(signUpActivityIntent);
                break;

            case R.id.SIA_forgot_pass_TV:

                setImageClickedAnimation(mForgotPass);

                moveToMailAppAndSendEmail();

                break;

        }

    }





    private void moveToMailAppAndSendEmail() {

        ForgotPassDialog forgotPassDialog = new ForgotPassDialog();
        forgotPassDialog.showDialog(this, this);

    }







    private void setImageClickedAnimation(TextView Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }






    private void setButtonClickedAnimiation(Button Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }






    /**
     * on sign in button clicked, check if all fields are filled and sign in
     */
    private void onSignInBtnClicked() {

        if (checkIfAllFieldsAreFilled(new EditText[]{mEmailOrPhoneET, mPasswordET})) {

            mProgressBar.setVisibility(View.VISIBLE);

            final RequestLogIn requestLogIn = new RequestLogIn();

            if (isEmailValid(mEmailOrPhoneET.getText().toString())) {
                requestLogIn.setEmail(mEmailOrPhoneET.getText().toString());
            } else {
                requestLogIn.setPhone(mEmailOrPhoneET.getText().toString());
            }

            requestLogIn.setDeviceType(ANDROID);
            requestLogIn.setPassword(mPasswordET.getText().toString());


            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {

                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    requestLogIn.setFcmToken(token);

                    saveFcmToken(token);
                    RequestManager.login(requestLogIn).subscribe(new Observer<Result<User>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Result<User> userResult) {

                            saveToken(userResult.getData().getToken());
                            Log.d(TAG, "Token: " + userResult.getData().getToken());
                            saveUser(userResult.getData());

                            if (mMasechtotList != null) {

                                getGemaraDafYomiDetailes();
                            }

                            getDonationData();


                        }

                        @Override
                        public void onError(Throwable e) {

                            mProgressBar.setVisibility(View.GONE);

                            if (!isNetworkAvailable()) {

                                Toast.makeText(SignInActivity.this, getResources().getString(R.string.no_internet) , Toast.LENGTH_LONG).show();

                            } else {

                                if (e instanceof HttpException) {
                                    HttpException exception = (HttpException) e;
                                    ErrorResponse response = null;
                                    try {
                                        response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                                        Toast.makeText(SignInActivity.this, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
            });



        }
    }










    /**
     * get donation data and save in local storage
     */
    private void getDonationData() {

        if (UserManager.getToken(this) != null) {

            RequestManager.getDonationData(UserManager.getToken(this)).subscribe(new Observer<Result<DonationData>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result<DonationData> donationDataResult) {

                    Gson gson = new Gson();
                    String donationDataGson = gson.toJson(donationDataResult.getData());
                    UserManager.setDonationData(donationDataGson, SignInActivity.this);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

        }


    }










    private void saveFcmToken(String token) {

        UserManager.setFcmToken(token, this);

    }






    /**
     * check if network available
     *
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(this).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }







    private void getGemaraDafYomiDetailes() {


        Gson gson = new Gson();
        final TodayDafYomiDetailes dafYomi = gson.fromJson(UserManager.getTodayDafYomi(this), TodayDafYomiDetailes.class);

        int masechetId = 0;
        int sederOrder = 0;
        int masechetOrder = 0;

        for (int i = 0; i < mMasechtotList.getSeder().size(); i++) {

            for (int j = 0; j < mMasechtotList.getSeder().get(i).getMasechet().size(); j++) {

                if (dafYomi.getMasechetName().toLowerCase().equals(mMasechtotList.getSeder().get(i).getMasechet().get(j).getName().toLowerCase())) {

                    masechetId = mMasechtotList.getSeder().get(i).getMasechet().get(j).getId();
                    sederOrder = mMasechtotList.getSeder().get(i).getOrder();
                    masechetOrder = mMasechtotList.getSeder().get(i).getMasechet().get(j).getOrder();
                    break;
                }
            }

        }


        final int finalSederOrder = sederOrder;
        final int finalMasechetOrder = masechetOrder;

        RequestManager.getGemaraDafYomi(UserManager.getToken(this), String.valueOf(masechetId),
                dafYomi.getMasechetPage()).subscribe(new Observer<Result<PagesItem>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<PagesItem> gemaraPagesResult) {

                mProgressBar.setVisibility(View.GONE);
                mDafYomiDetailes = gemaraPagesResult.getData();
                mDafYomiDetailes.setMasechetName(dafYomi.getMasechetName());
                mDafYomiDetailes.setSederOrder(finalSederOrder);
                mDafYomiDetailes.setMasechetOrder(finalMasechetOrder);

                goToMainActivity();

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }





    /**
     * save the token
     *
     * @param token String
     */
    private void saveToken(String token) {

        UserManager.setToken(token, this);

    }





    /**
     * save user Object
     *
     * @param user, User
     */
    private void saveUser(User user) {

        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);

        UserManager.setUser(jsonUser, this);

    }






    /**
     * go to main activity
     */
    private void goToMainActivity() {

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(DAF_YOMI_DETAILES, mDafYomiDetailes);
        startActivity(mainIntent);
        finish();

    }





    /**
     * check if all fields are filled and if not change background color
     *
     * @param editTexts EditText[], list of all fields edit text
     * @return boolean allFilled
     */
    private boolean checkIfAllFieldsAreFilled(EditText[] editTexts) {

        boolean allFieldsAreFilled = true;

        for (EditText currentField : editTexts) {

            if (currentField.getText().toString().length() <= 0) {
                currentField.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(this), R.drawable.rounded_blue_edittext));
                allFieldsAreFilled = false;
            }

        }

        return allFieldsAreFilled;

    }





    /**
     * on touch on edit text change the edit text background
     *
     * @param view        View
     * @param motionEvent MotionEvent
     * @return boolean
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        EditText editText = (EditText) view;
        editText.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(this), R.drawable.rounded_edittext_top_shadow_padding));


        return false;

    }





    @Override
    public void onSendNowClicked(final String emailAddress, final Dialog dialog) {

        SendMail sendMail = new SendMail();
        sendMail.setEmail(emailAddress);

        RequestManager.sendForgotPassword(sendMail).subscribe(new Observer<Result<SendMailResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<SendMailResponse> result) {

                if (result.getData().isUserExistStatus()){

                    EmailSentDialog emailSentDialog = new EmailSentDialog(emailAddress);
                    emailSentDialog.showDialog(SignInActivity.this, SignInActivity.this);
                    dialog.dismiss();

                }else {

                    NewRegistrationDialog newRegistrationDialog = new NewRegistrationDialog();
                    newRegistrationDialog.showDialog(SignInActivity.this, SignInActivity.this);
                    dialog.dismiss();



                }
            }

            @Override
            public void onError(Throwable e) {


                if (!isNetworkAvailable()) {

                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.no_internet) , Toast.LENGTH_LONG).show();

                } else {

                    dialog.findViewById(R.id.FD_progress_bar).setVisibility(View.GONE);

                    HttpException exception = (HttpException) e;
                    ErrorResponse response = null;
                    try {
                        response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                        Toast.makeText(SignInActivity.this, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }

            }

            @Override
            public void onComplete() {

            }
        });



    }





    @Override
    public void onOkClicked() {

        Intent signUpActivityIntent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(signUpActivityIntent);
    }
}
