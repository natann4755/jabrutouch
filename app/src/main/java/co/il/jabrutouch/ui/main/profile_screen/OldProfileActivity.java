package co.il.jabrutouch.ui.main.profile_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import co.il.jabrutouch.BuildConfig;
import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.model.model.Result;
import co.il.model.model.User;
import co.il.jabrutouch.user_manager.UserManager;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class OldProfileActivity extends AppCompatActivity implements View.OnClickListener, LogOutDialog.LogOutDialogListener {

    private static final String RESULTS = "RESULTS_FOR_PROFILE";
    public static final String LOG_OUT = "LOG_OUT";
    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserMail;
    private TextView mUserLocation;
    private User mUser;
    private ProgressBar mProgressBar;
    private ImageView mBackBtn;
    private Button mLogOutBtn;
    private TextView mPhoneNumberTV;
    private ImageView mLocationImageIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_old);

        findViews();
        getUserProfileFromServer();
        initListeners();


    }


    /**
     * find views
     */
    private void findViews() {

        mUserImage = findViewById(R.id.PA_user_image_CIV);
        mUserName = findViewById(R.id.PA_User_name_TV);
        mUserMail = findViewById(R.id.PA_User_mail_TV);
        mUserLocation = findViewById(R.id.PA_User_location_TV);
        mProgressBar = findViewById(R.id.PA_progress_bar);
        mBackBtn = findViewById(R.id.PA_back_arrow_IV);
        mLogOutBtn = findViewById(R.id.PA_log_out_btn);
        mPhoneNumberTV = findViewById(R.id.PA_phone_number_TV);
        mLocationImageIV = findViewById(R.id.PA_location_image_IV);

        ((TextView) findViewById(R.id.AP_version_number)).setText(String.valueOf(BuildConfig.VERSION_CODE));
    }


    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }


    /**
     * get user profile from server
     */
    private void getUserProfileFromServer() {

        mProgressBar.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        User userFromJson = gson.fromJson(UserManager.getUser(this), User.class);

        RequestManager.getUser(UserManager.getToken(this), String.valueOf(userFromJson.getId())).subscribe(new Observer<Result<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<User> userResult) {
                mUser = userResult.getData();
                mProgressBar.setVisibility(View.GONE);
                setUserDetails();

            }

            @Override
            public void onError(Throwable e) {
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onComplete() {

            }
        });

    }


    /**
     * set user details
     */
    @SuppressLint("SetTextI18n")
    private void setUserDetails() {

        if (!mUser.getImage().equals("")) {

            mUserImage.setImageBitmap(getImageBitmap(mUser.getImage()));
        }
        mUserName.setText(mUser.getFirstName() + " " + mUser.getLastName());
        mUserMail.setText(mUser.getEmail());
        if (mUser.getCountry().equals("")) {

            mLocationImageIV.setVisibility(View.GONE);
        } else {
            mLocationImageIV.setVisibility(View.VISIBLE);
            mUserLocation.setText(mUser.getCountry());
        }
        mPhoneNumberTV.setText(mUser.getPhone());

    }


    /**
     * init listeners
     */
    private void initListeners() {

        mBackBtn.setOnClickListener(this);
        mLogOutBtn.setOnClickListener(this);
    }


    /**
     * parse image from url to bitmap
     *
     * @param url String
     * @return Bitmap
     */
    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
        }
        return bm;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.PA_back_arrow_IV:

                setImageClickedAnimation(mBackBtn);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;

            case R.id.PA_log_out_btn:

                LogOutDialog logOutDialog = new LogOutDialog();
                logOutDialog.showDialog(this, this);
                break;
        }


    }


    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

    }


    /**
     * on log out button clicked, delete the token and user and move to sign in screen
     */
    @Override
    public void OnLogOutClicked() {

        UserManager.setToken(null, this);
        UserManager.setUser(null, this);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULTS, LOG_OUT);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();


    }
}
