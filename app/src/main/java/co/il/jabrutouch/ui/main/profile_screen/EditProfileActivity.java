package co.il.jabrutouch.ui.main.profile_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import co.il.jabrutouch.BuildConfig;
import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.jabrutouch.utils.ActivityRunning;
import co.il.jabrutouch.utils.FragmentHelper;
import co.il.model.model.ChangePassword;
import co.il.model.model.ChangePasswordResponse;
import co.il.model.model.ErrorResponse;
import co.il.model.model.IdAndNameDetailed;
import co.il.model.model.ProfileData;
import co.il.model.model.Result;
import co.il.model.model.User;
import co.il.model.model.UserUpdate;
import co.il.s3.interfaces.DownloadListener;
import co.il.s3.interfaces.UploadListener;
import co.il.s3.utils.Config;
import co.il.s3.utils.S3Helper;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static co.il.jabrutouch.ui.main.profile_screen.ProfileActivity.PERCENT;
import static co.il.jabrutouch.ui.main.profile_screen.ProfileActivity.SCROLL_TO_BOTTOM;
import static co.il.jabrutouch.ui.main.profile_screen.ProfileActivity.USER;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener,
        ProfileInterestsFragment.ProfileInterestsFragmentListener, ForgotPassDialog.ForgotPassDialogListener,
        EmailSentDialog.EmailSentDialogListener, InterestsAdapter.InterestsListener {


    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    static final String S3_SUB_FOLDER = "/users-image";
    private static final int MY_CAMERA_REQUEST_CODE = 103;
    private static final String COMMUNITY = "COMMUNITY";
    private static final String RELIGIOUS = "RELIGIOUS";
    private static final String EDUCATION = "EDUCATION";
    private static final String OCCUPATION = "OCCUPATION";
    public static final String USER_IMAGE = "userImage";
    private static final int GALLERY_WRITE_EXTERNAL_STORAGE = 6235;
    private static final String CANCEL = "cancel";
    private static final String JPG = ".jpg";
    private AppCompatTextView topicsEditBtn;
    private FragmentHelper mFragmentHelper;
    private ProfileInterestsFragment mProfileInterestsFragment;
    private RelativeLayout mAddPhoto;
    private Bitmap bitmap;
    private String selectedImagePath;
    private CircleImageView mUserImage;
    private TextView mForgotPass;
    S3Helper s3Helper;
    private ScrollView mScrollView;
    private boolean mScrollToBottom;
    private LinearLayout mChangePassLL;
    private TextView mAddPhotoTextTV;
    private Button mBirthdayET;
    private ProfileData mProfileData;
    private TextView mCommunityTV;
    private TextView mReligiousTV;
    private TextView mEducationTV;
    private TextView mOccupationTV;
    private RecyclerView mInterestRecyclerRV;
    private InterestsAdapter mAdapter;
    private LinearLayout mSaveBtnACTV;
    private User mUser;
    private EditText mFirstNameET;
    private EditText mLastNameET;
    private EditText mEmail;
    private EditText mPhoneNumberET;
    private EditText mSecondEmailET;
    private CountryCodePicker mCountryCCP;
    private List<Integer> mSelectedList;
    private String mImageS3Link;
    private IdAndNameDetailed mCommunityId = null;
    private IdAndNameDetailed mReligiousID = null;
    private IdAndNameDetailed mEducationId = null;
    private IdAndNameDetailed mOccupationId = null;
    private String mImageLocalStorageLink;
    private ProgressBar mProgressBarPB;
    private EditText mConfirmNewPasswordET;
    private EditText mOldPasswordET;
    private EditText mNewPasswordET;
    private String S3ObjectPathRandom;
    private LinearLayout mBackArrowIV;
    private ProgressBar mProfilePercentPB;
    private TextView mProfilePercentTextV;
    private TextView mCountryTextTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initViews();
        initListeners();
        initScrollOption();
        getProfileDataFromServer();
        setAllFiledsFromUser();

    }


    private void initViews() {

        mUser = (User) getIntent().getSerializableExtra(USER);
        topicsEditBtn = findViewById(R.id.EPA_edit_topic_TV);
        mAddPhoto = findViewById(R.id.EPA_add_photo);
        mUserImage = findViewById(R.id.EPA_user_image_IV);
        mForgotPass = findViewById(R.id.EPA_forgot_TV);
        mScrollView = findViewById(R.id.EPA_scrollView);
        mChangePassLL = findViewById(R.id.EPA_change_pass_layout);
        mAddPhotoTextTV = findViewById(R.id.EPA_add_photo_text);
        mBirthdayET = findViewById(R.id.EPA_birthday_ET);
        mCommunityTV = findViewById(R.id.EPA_community_TV);
        mReligiousTV = findViewById(R.id.EPA_religious_TV);
        mEducationTV = findViewById(R.id.EPA_education_TV);
        mOccupationTV = findViewById(R.id.EPA_occupation_TV);
        mInterestRecyclerRV = findViewById(R.id.EPA_recycler_RV);
        mSaveBtnACTV = findViewById(R.id.PA_save_btn_ACTV);
        mFirstNameET = findViewById(R.id.EPA_first_name_ET);
        mLastNameET = findViewById(R.id.EPA_last_name_ET);
        mEmail = findViewById(R.id.EPA_email_ET);
        mPhoneNumberET = findViewById(R.id.EPA_phone_ET);
        mSecondEmailET = findViewById(R.id.EPA_second_email_ET);
        mCountryCCP = findViewById(R.id.EPA_countryPicker_CCP);
        mProgressBarPB = findViewById(R.id.EPA_progress_bar);
        mOldPasswordET = findViewById(R.id.EPA_old_password_ET);
        mNewPasswordET = findViewById(R.id.EPA_new_password_ET);
        mConfirmNewPasswordET = findViewById(R.id.EPA_confirm_new_password_ET);
        mBackArrowIV = findViewById(R.id.EPA_back_arrow_IV);
        mProfilePercentPB = findViewById(R.id.EPA_progressbar);
        mCountryTextTV = findViewById(R.id.EPA_country_text_view_TV);
        mProfilePercentTextV = findViewById(R.id.EPA_profile_precent_TV);

        mFragmentHelper = new FragmentHelper(this, new ActivityRunning());
        mScrollToBottom = Objects.requireNonNull(getIntent().getExtras()).getBoolean(SCROLL_TO_BOTTOM);
        s3Helper = new S3Helper(this);


    }


    private void initListeners() {

        topicsEditBtn.setOnClickListener(this);
        mAddPhoto.setOnClickListener(this);
        mForgotPass.setOnClickListener(this);
        mBirthdayET.setOnClickListener(this);
        mCommunityTV.setOnClickListener(this);
        mReligiousTV.setOnClickListener(this);
        mEducationTV.setOnClickListener(this);
        mOccupationTV.setOnClickListener(this);
        mSaveBtnACTV.setOnClickListener(this);
        mBackArrowIV.setOnClickListener(this);

        mCountryCCP.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                mCountryTextTV.setText(mCountryCCP.getSelectedCountryEnglishName());
            }
        });

    }


    private void initScrollOption() {

        if (mScrollToBottom) {

            mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.scrollTo(0, mScrollView.getBottom());
                }
            });

            mChangePassLL.startAnimation(getBlinkAnimation());

        }

    }


    @Override
    public void onBackPressed() {
        if (mFragmentHelper.isCurrent(ProfileInterestsFragment.TAG)) {

            mFragmentHelper.removeFragment(mProfileInterestsFragment);
        } else {
            super.onBackPressed();
        }
    }


    private void getProfileDataFromServer() {


        RequestManager.getProfileData(UserManager.getToken(this)).subscribe(new Observer<Result<ProfileData>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<ProfileData> profileDataResult) {

                mProfileData = profileDataResult.getData();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


    @SuppressLint("SetTextI18n")
    private void setAllFiledsFromUser() {

        if (mUser != null) {

            if (mUser.getFirstName() != null) {

                mFirstNameET.setText(mUser.getFirstName());
            }

            if (mUser.getLastName() != null) {
                mLastNameET.setText(mUser.getLastName());

            }

            if (mUser.getEmail() != null) {
                mEmail.setText(mUser.getEmail());

            }

            mProfilePercentPB.setProgress(mUser.getProfilePercent());
            mProfilePercentTextV.setText(mUser.getProfilePercent() + PERCENT + getResources().getString(R.string.full));

            if (mUser.getPhone() != null) {
                mPhoneNumberET.setText(mUser.getPhone());
            }

            if (mUser.getSecondEmail() != null) {
                mSecondEmailET.setText(mUser.getSecondEmail());
            }

            if (mUser.getBirthday() != null) {
                mBirthdayET.setText(mUser.getBirthday());
            }


//            mCountryCCP.setCountryForNameCode(getCountryCode(mUser.getCountry()));
            mCountryTextTV.setText(mUser.getCountry());


            if (mUser.getInterest() != null && mUser.getInterest().size() > 0) {

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


            if (mUser.getImage() != null && !mUser.getImage().equals("")) {

                if (existInLocalStorage(mUser.getImage())) {

                    final String localImageLink = Config.getPathName(this) + USER_IMAGE + "/" + getFIleNameFromFileKEy(mUser.getImage());


                    new Thread(new Runnable() {
                        public void run() {

                            final Bitmap bitmap = getImageBitmap(localImageLink);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mUserImage.setImageBitmap(bitmap);
                                    mAddPhotoTextTV.setText("");
                                }
                            });
                        }
                    }).start();


                } else {

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
                            mAddPhotoTextTV.setText("");
                            mUser.setImage(pathName);

                        }

                        @Override
                        public void onDownloadError() {

                        }
                    });
                }
            }


        }


    }




    public String getCountryCode(String countryName) {

        // Get all country codes in a string array.
        String[] isoCountryCodes = Locale.getISOCountries();
        String countryCode = "";
        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code
            Locale locale = new Locale("en_US", code);
            // Get country name for each code.
            String name = locale.getDisplayCountry();
            if(name.equals(countryName))
            {
                countryCode = code;
                break;
            }
        }
        return countryCode;
    }


    private boolean existInLocalStorage(String userIamge) {

        File file = new File(Config.getPathName(this) + USER_IMAGE + "/" + getFIleNameFromFileKEy(userIamge));

        return file.exists();

    }


    private String getFIleNameFromFileKEy(String fileKey) {

        return fileKey.substring(fileKey.lastIndexOf("/") + 1);

    }


    public Animation getBlinkAnimation() {
        Animation animation = new AlphaAnimation(1, 0);         // Change alpha from fully visible to invisible
        animation.setDuration(700);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(2);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in

        return animation;
    }


    /**
     * parse image from url to bitmap
     *
     * @param url String
     * @return Bitmap
     */
    private Bitmap getImageBitmap(String url) {

        Bitmap bitmap = BitmapFactory.decodeFile(url);

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

            case R.id.EPA_edit_topic_TV:

                mProfileInterestsFragment = ProfileInterestsFragment.newInstance(mProfileData.getInterest(), mUser.getInterest());

                mFragmentHelper.replaceFragment(R.id.EPA_linearLL, mProfileInterestsFragment, ProfileInterestsFragment.TAG, null);

                break;


            case R.id.EPA_add_photo:

                openPhotoDialog();

                break;


            case R.id.EPA_forgot_TV:

                ForgotPassDialog forgotPassDialog = new ForgotPassDialog();
                forgotPassDialog.showDialog(this, this);

                break;


            case R.id.EPA_birthday_ET:

                openDatePickerDialog();

                break;


            case R.id.EPA_community_TV:

                openProfileDataDialog(COMMUNITY, (TextView) view);
                break;


            case R.id.EPA_religious_TV:

                openProfileDataDialog(RELIGIOUS, (TextView) view);
                break;


            case R.id.EPA_education_TV:

                openProfileDataDialog(EDUCATION, (TextView) view);
                break;


            case R.id.EPA_occupation_TV:

                openProfileDataDialog(OCCUPATION, (TextView) view);
                break;

            case R.id.PA_save_btn_ACTV:

                setViewClickedAnimation(mSaveBtnACTV);

                saveUserProfile();
                saveUserNewPassword();
                break;


            case R.id.EPA_back_arrow_IV:

                setViewClickedAnimation(mBackArrowIV);

                finish();
                break;


        }

    }


    private void setViewClickedAnimation(View Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }


    private void saveUserProfile() {

        final UserUpdate userUpdate = new UserUpdate();
        userUpdate.setFirstName(mFirstNameET.getText().toString());
        userUpdate.setLastName(mLastNameET.getText().toString());
        userUpdate.setPhone(mPhoneNumberET.getText().toString());
        userUpdate.setEmail(mEmail.getText().toString());

        if (mImageS3Link != null) {

            userUpdate.setImage(mImageS3Link);
        }

        if (!Objects.requireNonNull(mBirthdayET).getText().toString().equals(getResources().getString(R.string.birthday))) {

            userUpdate.setBirthday(mBirthdayET.getText().toString());
        } else {
            userUpdate.setBirthday(null);

        }

        userUpdate.setCountry(mCountryTextTV.getText().toString());


        if (mCommunityId != null) {

            userUpdate.setCommunityId(mCommunityId.getId());
        }

        if (mReligiousID != null) {

            userUpdate.setReligiousLevel(mReligiousID.getId());
        }

        if (mEducationId != null) {

            userUpdate.setEducationId(mEducationId.getId());
        }

        if (mOccupationId != null) {

            userUpdate.setOccupationId(mOccupationId.getId());
        }

        if (mSelectedList != null) {

            userUpdate.setInterestId(mSelectedList);
        } else {

            mSelectedList = new ArrayList<>();

            for (int i = 0; i < mUser.getInterest().size(); i++) {

                mSelectedList.add(mUser.getInterest().get(i).getId());
            }
            userUpdate.setInterestId(mSelectedList);

        }

        userUpdate.setSecondEmail(mSecondEmailET.getText().toString());
        userUpdate.setId(mUser.getId());

        RequestManager.updateUser(UserManager.getToken(this), userUpdate, String.valueOf(mUser.getId())).subscribe(new Observer<Result<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<User> userUpdateResult) {

                updateLocalStorageUser(userUpdateResult.getData());

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);

                finish();

            }

            @Override
            public void onError(Throwable e) {

                if (e instanceof HttpException) {
                    HttpException exception = (HttpException) e;
                    ErrorResponse response = null;
                    try {
                        response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.error_save), Toast.LENGTH_SHORT).show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                finish();
            }

            @Override
            public void onComplete() {

            }
        });


    }


    private void updateLocalStorageUser(User userUpdate) {


        if (mCommunityId != null) {

            mUser.setCommunity(mCommunityId);
        }

        if (mReligiousID != null) {

            mUser.setReligiousLevel(mReligiousID.getId());
        }

        if (mEducationId != null) {

            mUser.setEducation(mEducationId);
        }

        if (mOccupationId != null) {

            mUser.setOccupation(mOccupationId);
        }

        mUser.setBirthday(userUpdate.getBirthday());
        mUser.setCountry(userUpdate.getCountry());
        mUser.setEmail(userUpdate.getEmail());
        mUser.setFirstName(userUpdate.getFirstName());
        mUser.setLastName(userUpdate.getLastName());
        mUser.setPhone(userUpdate.getPhone());
        mUser.setSecondEmail(userUpdate.getSecondEmail());
        mUser.setProfilePercent(userUpdate.getProfilePercent());

        if (mImageLocalStorageLink != null) {

            mUser.setImage(mImageLocalStorageLink);
        }

        Gson gson = new Gson();
        String jsonUser = gson.toJson(mUser);
        UserManager.setUser(jsonUser, EditProfileActivity.this);


    }


    private void saveUserNewPassword() {

        if (checkIfAllFieldsAreFilled(new EditText[]{mOldPasswordET, mNewPasswordET, mConfirmNewPasswordET})) {

            if (mNewPasswordET.getText().toString().equals(mConfirmNewPasswordET.getText().toString())) {

                ChangePassword changePassword = new ChangePassword();
                changePassword.setOldPassword(mOldPasswordET.getText().toString());
                changePassword.setNewPassword(mNewPasswordET.getText().toString());

                RequestManager.changePassword(UserManager.getToken(this), changePassword,
                        String.valueOf(mUser.getId())).subscribe(new Observer<Result<ChangePasswordResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<ChangePasswordResponse> changePasswordResponseResult) {

                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.pass_change), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {

                        HttpException exception = (HttpException) e;
                        ErrorResponse response = null;
                        try {
                            response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                            Toast.makeText(EditProfileActivity.this, response.getErrors().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();

                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });


            } else {

                Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.not_match), Toast.LENGTH_SHORT).show();

            }
        }


    }


    /**
     * check if all fields are filled and if not change background color
     *
     * @param editTexts EditText[], list of all fields edit text
     * @return boolean allFilled
     */
    private boolean checkIfAllFieldsAreFilled(EditText[] editTexts) {

        boolean allFilled = true;

        for (EditText currentField : editTexts) {

            if (currentField.getText().toString().length() <= 0) {
                allFilled = false;
            }

        }

        return allFilled;

    }


    private void openProfileDataDialog(final String name, final TextView view) {

        if (mProfileData != null) {
            List<IdAndNameDetailed> idAndNameDetailedList = null;

            switch (name) {

                case COMMUNITY:

                    idAndNameDetailedList = mProfileData.getCommunities();
                    break;

                case RELIGIOUS:

                    idAndNameDetailedList = new ArrayList<>();
                    for (int i = 1; i <= 10; i++) {

                        idAndNameDetailedList.add(new IdAndNameDetailed(String.valueOf(i), i));

                    }

                    break;

                case EDUCATION:

                    idAndNameDetailedList = mProfileData.getEducation();
                    break;

                case OCCUPATION:

                    idAndNameDetailedList = mProfileData.getOccupation();
                    break;

            }


            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setTitle(name);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);

            for (int i = 0; i < Objects.requireNonNull(idAndNameDetailedList).size(); i++) {

                arrayAdapter.add(idAndNameDetailedList.get(i).getName());

            }


            builderSingle.setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            final List<IdAndNameDetailed> finalIdAndNameDetailedList = idAndNameDetailedList;
            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    view.setText(strName);
                    dialog.dismiss();

                    for (int i = 0; i < finalIdAndNameDetailedList.size(); i++) {

                        if (finalIdAndNameDetailedList.get(i).getName().equals(strName)) {

                            switch (name) {

                                case COMMUNITY:
                                    mCommunityId = finalIdAndNameDetailedList.get(i);
                                    break;
                                case RELIGIOUS:
                                    mReligiousID = finalIdAndNameDetailedList.get(i);
                                    break;
                                case EDUCATION:
                                    mEducationId = finalIdAndNameDetailedList.get(i);
                                    break;
                                case OCCUPATION:
                                    mOccupationId = finalIdAndNameDetailedList.get(i);
                                    break;


                            }
                        }

                    }
                }
            });

            builderSingle.show();

        }
    }


    private void openDatePickerDialog() {

        // Process to get Current Date
        final Calendar c = Calendar.getInstance();

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        mBirthdayET.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, 1987, 11, 11);
        dpd.show();
    }


    private void openPhotoDialog() {

        S3ObjectPathRandom = System.currentTimeMillis() + JPG;

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle(getResources().getString(R.string.upload));
        myAlertDialog.setMessage(getResources().getString(R.string.how_to));

        myAlertDialog.setPositiveButton(getResources().getString(R.string.gallery),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (checkPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, GALLERY_WRITE_EXTERNAL_STORAGE)) {
                            Intent pictureActionIntent = null;

                            pictureActionIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(
                                    pictureActionIntent,
                                    GALLERY_PICTURE);
                        }

                    }
                });


        myAlertDialog.setNegativeButton(getResources().getString(R.string.camera),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        String[] PERMISSIONS = {

                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA
                        };

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!hasPermissions(EditProfileActivity.this, PERMISSIONS)) {

                                requestPermissions(PERMISSIONS, MY_CAMERA_REQUEST_CODE);

                            } else {


                                Intent intent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);

                                File file = new File(Config.getPathName(EditProfileActivity.this) + USER_IMAGE);

                                if (!file.exists()) {
                                    file.mkdirs();
                                }

                                File f = new File(Config.getPathName(EditProfileActivity.this) + USER_IMAGE, S3ObjectPathRandom);


                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", f));

                                startActivityForResult(intent,
                                        CAMERA_REQUEST);

                            }
                        }


                    }
                });

        myAlertDialog.show();


    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Check if permission "Manifest.permission.READ_EXTERNAL_STORAGE" or
     * "Manifest.permission.WRITE_EXTERNAL_STORAGE" was granted.
     * If the permission wasn't granted, the app request it and the result is handled at:
     */
    private static boolean checkPermission(Activity activity, String permission,
                                           int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(permission)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    // Explain to the user why we need to read the contacts
                }

                activity.requestPermissions(new String[]{permission}, permissionCode);
                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique
                return false;
            }
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                File file = new File(Config.getPathName(EditProfileActivity.this) + USER_IMAGE);

                if (!file.exists()) {
                    file.mkdirs();
                }

                File f = new File(Config.getPathName(EditProfileActivity.this) + USER_IMAGE, S3ObjectPathRandom);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", f));

                startActivityForResult(intent,
                        CAMERA_REQUEST);


            }

        }


        if (requestCode == GALLERY_WRITE_EXTERNAL_STORAGE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent pictureActionIntent = null;

                pictureActionIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(
                        pictureActionIntent,
                        GALLERY_PICTURE);


            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            File f = new File(Config.getPathName(this) + USER_IMAGE);

            if (!f.exists()) {
                f.mkdirs();
            }

            for (File temp : f.listFiles()) {
                if (temp.getName().equals(S3ObjectPathRandom)) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(getBaseContext(),

                        "Error while capturing image", Toast.LENGTH_LONG)

                        .show();

                return;

            }

            try {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
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


                mUserImage.setImageBitmap(bitmap);
                mAddPhotoTextTV.setText("");
                uploadImageToS3(this, f.getAbsolutePath(), false);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();

                if (selectedImagePath != null) {
                }

                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load

                mUserImage.setImageBitmap(bitmap);
                mAddPhotoTextTV.setText("");
                uploadImageToS3(this, selectedImagePath, true);

            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void uploadImageToS3(Context context, final String imagePath, final boolean fromGallery) {

        mProgressBarPB.setVisibility(View.VISIBLE);
        File fileToSave = new File(imagePath);


        s3Helper.upload(S3_SUB_FOLDER, S3ObjectPathRandom, fileToSave, new UploadListener() {
            @Override
            public void onUploadStart(int id, File fileLocation) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onUploadWaiting(int id, TransferState state, File fileLocation) {

            }

            @Override
            public void onUploadReceivedStop(int id, TransferState state, File fileLocation) {

            }

            @Override
            public void onUploadFinish(int id, String link) {

                mImageS3Link = link;
                if (fromGallery) {
                    mImageLocalStorageLink = link;
                } else {

                    mImageLocalStorageLink = imagePath;
                }
                mProgressBarPB.setVisibility(View.GONE);
            }

            @Override
            public void onUploadError(int id, File fileLocation, Exception ex) {

                mProgressBarPB.setVisibility(View.GONE);
            }
        });


    }


    @Override
    public void onSendNowClicked(String emailAdress, Dialog dialog) {


        EmailSentDialog emailSentDialog = new EmailSentDialog(emailAdress);
        emailSentDialog.showDialog(this, this);

    }


    @Override
    public void onSaveClicked(List<IdAndNameDetailed> yourList) {

        mFragmentHelper.removeFragment(mProfileInterestsFragment);

        mSelectedList = new ArrayList<>();
        mSelectedList.clear();

        for (int i = 0; i < yourList.size(); i++) {

            mSelectedList.add(yourList.get(i).getId());
        }

        mUser.setInterest(yourList);

        List<IdAndNameDetailed> newList;
        if (yourList.size() > 5) {

            newList = new ArrayList<>(yourList.subList(0, 5));
            newList.add(new IdAndNameDetailed("...", 6));

        } else {
            newList = yourList;
        }


        mInterestRecyclerRV.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new InterestsAdapter(this, newList, this, false);
        mInterestRecyclerRV.setAdapter(mAdapter);
        mInterestRecyclerRV.setNestedScrollingEnabled(false);


    }


    @Override
    public void onArrowBackClicked() {

        mFragmentHelper.removeFragment(mProfileInterestsFragment);
    }


    @Override
    public void onInterestAdd(IdAndNameDetailed interest) {

    }


    @Override
    public void onInterestRemoved(IdAndNameDetailed interest) {

    }
}
