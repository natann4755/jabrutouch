package co.il.jabrutouch;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import java.util.Objects;
import co.il.jabrutouch.daf_yomi_manager.DafYomiCalculator;
import co.il.jabrutouch.server.RequestManager;
import co.il.model.model.DonationData;
import co.il.model.model.ErrorResponse;
import co.il.model.model.Result;
import co.il.jabrutouch.ui.sign_in.SignInActivity;
import co.il.jabrutouch.ui.slide_screens.SlideActivity;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.TodayDafYomiDetailes;
import co.il.model.model.PagesItem;
import co.il.model.model.User;
import co.il.model.model.masechet_list_model.MasechetList;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import static co.il.jabrutouch.ui.main.wall_screen.WallFragment.DAF_YOMI_DETAILES;




public class SplashActivity extends AppCompatActivity implements Runnable {

    private ProgressBar mProgressBar;
    private PagesItem mDafYomiDetailes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_splash_screen);

        findViews();
        setTheProgressBar();
        getTodayDafYomi();
        getDonationData();

    }




    /**
     * find class views
     */
    private void findViews() {

        mProgressBar = findViewById(R.id.SSA_progressbar_PB);

    }





    /**
     * set splash progress bar
     */
    private void setTheProgressBar() {

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 1000);
        progressAnimator.setDuration(3000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();

        progressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startNextActivity();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });



    }




    /**
     * get the daf yomi for today using the dafYomiCalculator;
     */
    private void getTodayDafYomi() {

        DafYomiCalculator calculator = new DafYomiCalculator();
        TodayDafYomiDetailes todayDaf = calculator.getTodayDafYomi(this);


        Gson gson = new Gson();
        String jsonTodayDaf = gson.toJson(todayDaf);

        UserManager.setTodayDafYomi(jsonTodayDaf, this);

        if (UserManager.getToken(this) != null) {

            getGemaraDafYomiDetailes();
            getUserAndSaveInLocal();
        }

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

        if (isNetworkAvailable()){

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

                    if (e instanceof HttpException) {

                        HttpException exception = (HttpException) e;
                        ErrorResponse response = null;
                        try {
                            response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                            if (Objects.requireNonNull(response).getErrors().get(0).getMessage().equals("Invalid token.")) {

                                UserManager.setToken(null, SplashActivity.this);
                                UserManager.setUser(null, SplashActivity.this);

                            }
                            Toast.makeText(SplashActivity.this, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }


                }

                @Override
                public void onComplete() {

                }
            });

        }else {


            Toast.makeText(SplashActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }

    }








    private void getUserAndSaveInLocal() {

        if (isNetworkAvailable()){

            Gson gson = new Gson();
            User userFromJson = gson.fromJson(UserManager.getUser(this), User.class);

            RequestManager.getUser(UserManager.getToken(this), String.valueOf(userFromJson.getId())).subscribe(new Observer<Result<User>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result<User> userResult) {

                    saveUser(userResult.getData());
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
                    UserManager.setDonationData(donationDataGson, SplashActivity.this);

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







    /**
     * run new thread to change progressBar line position
     */
    @Override
    public void run() {
        int currentPosition = 0;
        int total = mProgressBar.getMax();
        while (currentPosition < total) {
            try {
                Thread.sleep(100);
                currentPosition = mProgressBar.getProgress() + 10;

            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            mProgressBar.setProgress(currentPosition);
            if (mProgressBar.getProgress() == mProgressBar.getMax()) {
                startNextActivity();
            }
        }
    }





    /**
     * start slide activity when splash finished
     */
    private void startNextActivity() {


        if (UserManager.getToken(this) != null) {

            if (UserManager.getFcmToken(this) == null){

                UserManager.setToken(null, SplashActivity.this);
                UserManager.setUser(null, SplashActivity.this);

                Intent signInActivityIntent = new Intent(this, SignInActivity.class);
                startActivity(signInActivityIntent);
                finish();

            }else {

                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra(DAF_YOMI_DETAILES, mDafYomiDetailes);
                startActivity(mainIntent);
                finish();
            }


        } else {

            if (UserManager.getNotFirstTime(this)) {
                Intent signInActivityIntent = new Intent(this, SignInActivity.class);
                startActivity(signInActivityIntent);
                finish();
            } else {

                Intent slideActivityIntent = new Intent(this, SlideActivity.class);
                startActivity(slideActivityIntent);
                finish();
            }

        }

    }
}
