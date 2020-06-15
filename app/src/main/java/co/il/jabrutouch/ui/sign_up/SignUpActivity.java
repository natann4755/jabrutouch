package co.il.jabrutouch.ui.sign_up;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import co.il.jabrutouch.MainActivity;
import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.model.model.DonationData;
import co.il.model.model.ErrorResponse;
import co.il.model.model.TodayDafYomiDetailes;
import co.il.model.model.PagesItem;
import co.il.model.model.User;
import co.il.model.model.RequestSendPhoneNUmber;
import co.il.model.model.RequestSignUp;
import co.il.model.model.RequestValidationCode;
import co.il.model.model.ResponseValidation;
import co.il.model.model.Result;
import co.il.jabrutouch.ui.sign_in.SignInActivity;
import co.il.jabrutouch.ui.sign_up.fragments.SignUpFragment;
import co.il.jabrutouch.ui.sign_up.fragments.VerificationCodeFragment;
import co.il.jabrutouch.ui.sign_up.fragments.VerificationFragment;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.jabrutouch.utils.ActivityRunning;
import co.il.jabrutouch.utils.FragmentHelper;
import co.il.model.model.masechet_list_model.MasechetList;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import static co.il.jabrutouch.ui.main.wall_screen.WallFragment.DAF_YOMI_DETAILES;






public class SignUpActivity extends AppCompatActivity implements VerificationFragment.OnVerificationFragmentListener,
        VerificationCodeFragment.OnVerificationCodeFragmentListener,
        SignUpFragment.OnSignUpFragmentListener {

    private static final String ANDROID = "ANDROID";
    private FragmentHelper mFragmentHelper;
    private VerificationFragment mVerificationFragment;
    private VerificationCodeFragment mVerificationCodeFragment;
    private SignUpFragment mSignUpFragment;
    private ProgressBar mProgressBar;
    private PagesItem mDafYomiDetailes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        initViews();
        startVerificationFragment();

    }




    /**
     * init views
     */
    private void initViews() {

        mProgressBar = findViewById(R.id.SUA_progress_bar);
        mFragmentHelper = new FragmentHelper(this, new ActivityRunning());

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





    /**
     * start VerificationFragment
     */
    private void startVerificationFragment() {

        if (mVerificationFragment == null) {
            mVerificationFragment = VerificationFragment.newInstance();
        }

        mFragmentHelper.replaceFragment(R.id.SUA_container_FL, mVerificationFragment, null, null);


    }





    /**
     * start VerificationCodeFragment when user Verify his account
     */
    @Override
    public void OnSendVerificationBtnClicked(String phoneNumber) {

        mProgressBar.setVisibility(View.VISIBLE);

        postRequestSendPhoneNumberAndMoveToVerificationCodeFragment(phoneNumber);


    }





    /**
     * send post request with phone number and when success move to VerificationCodeFragment
     *
     * @param phoneNumber String, user phone number
     */
    private void postRequestSendPhoneNumberAndMoveToVerificationCodeFragment(final String phoneNumber) {

        RequestSendPhoneNUmber requestSendPhoneNUmber = new RequestSendPhoneNUmber();
        requestSendPhoneNUmber.setPhone(phoneNumber);

        RequestManager.sendPhoneNumber(requestSendPhoneNUmber).subscribe(new Observer<Result>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result value) {

                mProgressBar.setVisibility(View.GONE);

                mVerificationCodeFragment = VerificationCodeFragment.newInstance(phoneNumber);

                mFragmentHelper.replaceFragment(R.id.SUA_container_FL, mVerificationCodeFragment, null, null);


            }


            @Override
            public void onError(Throwable e) {

                if (e instanceof HttpException) {
                    HttpException exception = (HttpException) e;
                    ErrorResponse response = null;
                    try {
                        response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                        Toast.makeText(SignUpActivity.this, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
                mProgressBar.setVisibility(View.GONE);

            }


            @Override
            public void onComplete() {

            }

        });


    }





    /**
     * send the code to server and move to SignUpFragment
     *
     * @param code,        String, 4 digits code
     * @param phoneNumber, String, user phone number
     */
    @Override
    public void sendTheCodeAndMoveToSignUpFragment(String code, String phoneNumber) {

        mProgressBar.setVisibility(View.VISIBLE);

        postRequestValidateCodeAndMoveToSignUpFragment(code, phoneNumber);

    }






    /**
     * on send again clicked
     *
     * @param phoneNumber String
     */
    @Override
    public void onSendAgainClicked(String phoneNumber) {

        RequestSendPhoneNUmber requestSendPhoneNUmber = new RequestSendPhoneNUmber();
        requestSendPhoneNUmber.setPhone(phoneNumber);


        RequestManager.sendAgain(requestSendPhoneNUmber).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }





    @Override
    public void onOnBackPressed() {

        if (mVerificationFragment == null) {
            mVerificationFragment = VerificationFragment.newInstance();
        }

        mFragmentHelper.replaceFragment(R.id.SUA_container_FL, mVerificationFragment, null, null);
    }

    @Override
    public void onOnBackPressedFromMail() {


        onBackPressed();
    }


    /**
     * post request validate code and move to SignUpFragment
     *
     * @param code        String
     * @param phoneNumber String
     */
    private void postRequestValidateCodeAndMoveToSignUpFragment(String code, String phoneNumber) {


        final RequestValidationCode requestValidationCode = new RequestValidationCode();
        requestValidationCode.setCode(code);
        requestValidationCode.setPhone(phoneNumber);


        RequestManager.sendValidateCode(requestValidationCode).subscribe(new Observer<Result<ResponseValidation>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<ResponseValidation> responseValidation) {

                mProgressBar.setVisibility(View.GONE);


                if (mSignUpFragment == null) {
                    mSignUpFragment = SignUpFragment.newInstance(String.valueOf(responseValidation.getData().getId()), responseValidation.getData().getPhone());
                }

                mFragmentHelper.replaceFragment(R.id.SUA_container_FL, mSignUpFragment, null, null);


            }

            @Override
            public void onError(Throwable e) {

                mProgressBar.setVisibility(View.GONE);
                mVerificationCodeFragment.resetTheCode();
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onComplete() {

            }
        });


    }






    /**
     * on sign in button clicked
     */
    @Override
    public void OnSignInClicked() {

        Intent signInIntent = new Intent(this, SignInActivity.class);
        startActivity(signInIntent);
        finish();
    }





    /**
     * on sign up button clicked
     *
     * @param userId          String
     * @param firstName       String
     * @param lastName        String
     * @param emailOrPhone    String
     * @param password        String
     * @param userPhoneNumber
     */
    @Override
    public void OnSignUpClicked(final String userId, String firstName, String lastName, String emailOrPhone, String password, String userPhoneNumber) {

        mProgressBar.setVisibility(View.VISIBLE);

        getGemaraDafYomiDetailes();


        final RequestSignUp requestSignUp = new RequestSignUp();
        requestSignUp.setFirstName(firstName);
        requestSignUp.setLastName(lastName);
        requestSignUp.setEmail(emailOrPhone);
        requestSignUp.setPhone(userPhoneNumber);
        requestSignUp.setPassword(password);
        requestSignUp.setDeviceType(ANDROID);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();

                requestSignUp.setFcmToken(token);
                saveFcmToken(token);

                RequestManager.signUp(userId, requestSignUp).subscribe(new Observer<Result<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<User> responseValidationResult) {

                        saveToken(responseValidationResult.getData().getToken());
                        saveUser(responseValidationResult.getData());
                        getDonationData();
                        goToMainActivity(responseValidationResult.getData());

                    }

                    @Override
                    public void onError(Throwable e) {

                        HttpException exception = (HttpException) e;
                        ErrorResponse response = null;
                        try {
                            response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                            Toast.makeText(SignUpActivity.this, Objects.requireNonNull(response).getErrors().get(0).getField() + ": " +
                                    Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        mProgressBar.setVisibility(View.GONE);


                    }

                    @Override
                    public void onComplete() {

                    }
                });


            }
        });


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
                    UserManager.setDonationData(donationDataGson, SignUpActivity.this);
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





    private void getGemaraDafYomiDetailes() {

        Gson gson = new Gson();
        MasechetList mMasechtotList = gson.fromJson(UserManager.getMasechtotList(this), MasechetList.class);

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

                mDafYomiDetailes = gemaraPagesResult.getData();
                mDafYomiDetailes.setMasechetName(dafYomi.getMasechetName());
                mDafYomiDetailes.setSederOrder(finalSederOrder);
                mDafYomiDetailes.setMasechetOrder(finalMasechetOrder);

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
     * move to main activity
     *
     * @param user User object
     */
    private void goToMainActivity(User user) {

        mProgressBar.setVisibility(View.GONE);

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(DAF_YOMI_DETAILES, mDafYomiDetailes);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); // to clear previous activity
        startActivity(mainIntent);
        finish();

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
     * parse the User object to string and save it
     *
     * @param user User
     */
    private void saveUser(User user) {

        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);

        UserManager.setUser(jsonUser, this);

    }





    /**
     * method is used for checking valid email id format.
     *
     * @param email String
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
