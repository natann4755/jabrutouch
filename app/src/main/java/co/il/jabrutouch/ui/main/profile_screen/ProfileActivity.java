package co.il.jabrutouch.ui.main.profile_screen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.BuildConfig;
import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.model.model.ErrorResponse;
import co.il.model.model.IdAndNameDetailed;
import co.il.model.model.Result;
import co.il.model.model.User;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.s3.interfaces.DownloadListener;
import co.il.s3.utils.Config;
import co.il.s3.utils.S3Helper;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import static co.il.jabrutouch.ui.main.profile_screen.EditProfileActivity.USER_IMAGE;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, LogOutDialog.LogOutDialogListener, RemoveDialog.RemoveDialogListener, InterestsAdapter.InterestsListener {


    public static final String RESULTS_FOR_PROFILE = "RESULTS_FOR_PROFILE";
    public static final String LOG_OUT = "LOG_OUT";
    static final String SCROLL_TO_BOTTOM = "SCROLL_TO_BOTTOM";
    static final String USER = "USER";
    private static final int EDIT_RESULT = 4522;
    public static final String PERCENT = "% ";
    public static final String GO_TO_DONATION = "GO_TO_DONATION";
    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserMail;
    private TextView mUserLocation;
    private User mUser;
    private ProgressBar mProgressBar;
    private LinearLayout mBackBtn;
    private Button mLogOutBtn;
    private TextView mPhoneNumberTV;
    private ImageView mLocationImageIV;
    private LinearLayout mEditBtn;
    private AppCompatTextView mRemoveBtn;
    private AppCompatTextView mChangePassBtn;
    private TextView mBirthdayTV;
    private TextView mCommunityTV;
    private TextView mReligiousTV;
    private TextView mEducationTV;
    private TextView mOccupationTV;
    private TextView mSecondEmailTV;
    private RecyclerView mInterestRecyclerRV;
    private InterestsAdapter mAdapter;
    private ProgressBar mUserImageProgressPB;
    private ProgressBar mProfilePercentPB;
    private TextView mProfilePercentTextV;
    private TextView mLessonWatchCountTV;
    private TextView mLessonDonatedTV;
    private Button mDonateBtnBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViews();
//        getUserProfileFromLocalStorage();
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
        mEditBtn = findViewById(R.id.PA_edit_btn_ACTV);
        mRemoveBtn = findViewById(R.id.PA_remove_account_TV);
        mChangePassBtn = findViewById(R.id.PA_change_pass_TV);
        mBirthdayTV = findViewById(R.id.PA_buirthday_TV);
        mCommunityTV = findViewById(R.id.PA_community_TV);
        mReligiousTV = findViewById(R.id.PA_religious_level_TV);
        mEducationTV = findViewById(R.id.PA_education_TV);
        mOccupationTV = findViewById(R.id.PA_occupation_TV);
        mSecondEmailTV = findViewById(R.id.PA_second_email_TV);
        mInterestRecyclerRV = findViewById(R.id.PA_recycler_RV);
        mUserImageProgressPB = findViewById(R.id.PA_image_progress_bar);
        mProfilePercentPB = findViewById(R.id.PA_progressbar);
        mProfilePercentTextV = findViewById(R.id.PA_profile_precent_TV);
        mLessonWatchCountTV = findViewById(R.id.PA_lesson_donate_count_TV);
        mLessonDonatedTV = findViewById(R.id.PA_lesson_donated_TV);
        mDonateBtnBTN = findViewById(R.id.PA_donate_btn);

        ((TextView) findViewById(R.id.AP_version_number)).setText(String.valueOf(BuildConfig.VERSION_CODE));

    }




    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }





    private void setViewClickedAnimation(View Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }




    /**
     * get user profile from local storage
     */
    private void getUserProfileFromLocalStorage() {

        Gson gson = new Gson();
        mUser = gson.fromJson(UserManager.getUser(this), User.class);

        setUserDetails();

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

        if (mUser.getImage() != null && !mUser.getImage().equals("")) {

            if (existInLocalStorage(mUser.getImage())){

                final String localImageLink = Config.getPathName(this) + USER_IMAGE + "/" + getFIleNameFromFileKEy(mUser.getImage());

//                Bitmap bmImg = BitmapFactory.decodeFile(localImageLink);
//                mUserImage.setImageBitmap(bmImg);

                new Thread(new Runnable() {
                    public void run(){

                        final Bitmap bitmap = getImageBitmap(localImageLink);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mUserImage.setImageBitmap(bitmap);
                                mUser.setImage(localImageLink);
                            }
                        });
                    }
                }).start();


            }else {

                mUserImageProgressPB.setVisibility(View.VISIBLE);
                S3Helper s3Helper = new S3Helper(this);
                s3Helper.downloadFile(this, mUser.getImage().substring(1), mUser.getImage().substring(1), USER_IMAGE + "/", new DownloadListener() {
                    @Override
                    public void onDownloadStart(int id, File fileLocation) {

                    }

                    @Override
                    public void onProgressChanged(int percentDone) {

                    }

                    @Override
                    public void onDownloadWaiting(int id, TransferState state, File fileLocation) {

                    }

                    @Override
                    public void onDownloadReceivedStop(int id, TransferState state, File fileLocation) {

                    }

                    @Override
                    public void onDownloadFinish(int id, String link, String pathName, String pagePathName) {

                        mUserImage.setImageBitmap(getImageBitmap(pathName));
                        mUser.setImage(pathName);
                        mUserImageProgressPB.setVisibility(View.GONE);

                    }

                    @Override
                    public void onDownloadError() {

                    }
                });
            }
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

        mBirthdayTV.setText(mUser.getBirthday());

        if (mUser.getReligiousLevel() != 0){

            mReligiousTV.setText(String.valueOf(mUser.getReligiousLevel()));
        }

        if (mUser.getCommunity() != null) {

            mCommunityTV.setText(mUser.getCommunity().getName());
        }
        if (mUser.getEducation() != null) {

            mEducationTV.setText(mUser.getEducation().getName());
        }
        if (mUser.getOccupation() != null) {

            mOccupationTV.setText(mUser.getOccupation().getName());
        }
        if (mUser.getSecondEmail() != null) {

            mSecondEmailTV.setText(mUser.getSecondEmail());
        }


        if (mUser.getInterest() != null) {

            List<IdAndNameDetailed> newList;
            if (mUser.getInterest().size() > 5) {

                newList = new ArrayList<>(mUser.getInterest().subList(0, 5));
                newList.add(new IdAndNameDetailed("...", 6));

            } else {
                newList = mUser.getInterest();
            }

            mInterestRecyclerRV.setLayoutManager(new GridLayoutManager(this, 3));
            mAdapter = new InterestsAdapter(this, newList, this, false);
            mInterestRecyclerRV.setAdapter(mAdapter);
            mInterestRecyclerRV.setNestedScrollingEnabled(false);
        }

        mProfilePercentPB.setProgress(mUser.getProfilePercent());
        mProfilePercentTextV.setText(mUser.getProfilePercent() + PERCENT + getResources().getString(R.string.full));

        mLessonWatchCountTV.setText(String.valueOf(mUser.getLessonWatchCount()));
        mLessonDonatedTV.setText(String.valueOf(mUser.getLessonDonatedObject().getLessonDonated()));

    }





    /**
     * init listeners
     */
    private void initListeners() {

        mBackBtn.setOnClickListener(this);
        mLogOutBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
        mRemoveBtn.setOnClickListener(this);
        mChangePassBtn.setOnClickListener(this);
        mDonateBtnBTN.setOnClickListener(this);
    }





    private boolean existInLocalStorage(String userIamge) {

        File file = new File(Config.getPathName(this) + USER_IMAGE + "/" + getFIleNameFromFileKEy(userIamge));

        return file.exists();

    }






    private String getFIleNameFromFileKEy(String fileKey) {

        return fileKey.substring(fileKey.lastIndexOf("/") + 1);

    }





    /**
     * parse image from url to bitmap
     *
     * @param url String
     * @return Bitmap
     */
    private Bitmap getImageBitmap(String url) {
        Bitmap bitmap = BitmapFactory.decodeFile(url);

//                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true); to resize the image

        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(url);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        return bitmap;

    }





    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.PA_back_arrow_IV:

                setViewClickedAnimation(mBackBtn);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;

            case R.id.PA_log_out_btn:

                LogOutDialog logOutDialog = new LogOutDialog();
                logOutDialog.showDialog(this, this);
                break;


            case R.id.PA_edit_btn_ACTV:

                setViewClickedAnimation(mEditBtn);

                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra(SCROLL_TO_BOTTOM, false);
                intent.putExtra(USER, mUser);
                startActivityForResult(intent, EDIT_RESULT);

                break;


            case R.id.PA_remove_account_TV:

                setViewClickedAnimation(mRemoveBtn);

                RemoveDialog removeDialog = new RemoveDialog();
                removeDialog.showDialog(this, this);

                break;


            case R.id.PA_change_pass_TV:

                setViewClickedAnimation(mChangePassBtn);

                Intent intent2 = new Intent(this, EditProfileActivity.class);
                intent2.putExtra(SCROLL_TO_BOTTOM, true);
                intent2.putExtra(USER, mUser);
                startActivity(intent2);

                break;



            case R.id.PA_donate_btn:

                moveToDonateScreen();

                break;
        }


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_RESULT) {

            getUserProfileFromLocalStorage();

        }
    }






    private void moveToDonateScreen() {


        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULTS_FOR_PROFILE, GO_TO_DONATION);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();


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
        returnIntent.putExtra(RESULTS_FOR_PROFILE, LOG_OUT);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();


    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public void onInterestAdd(IdAndNameDetailed interest) {

    }



    @Override
    public void onInterestRemoved(IdAndNameDetailed interest) {

    }

    @Override
    public void onRemoveBtnClicked() {


        RequestManager.deleteUser(UserManager.getToken(this), String.valueOf(mUser.getId())).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {


                UserManager.setToken(null, ProfileActivity.this);
                UserManager.setUser(null, ProfileActivity.this);

                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULTS_FOR_PROFILE, LOG_OUT);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onError(Throwable e) {

                if (e instanceof HttpException){
                    HttpException exception = (HttpException) e;
                    ErrorResponse response = null;
                    try {
                        response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                        Toast.makeText(ProfileActivity.this, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
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
}
