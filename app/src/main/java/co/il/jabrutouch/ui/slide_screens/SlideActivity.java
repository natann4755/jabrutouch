package co.il.jabrutouch.ui.slide_screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.viewpagerindicator.LinePageIndicator;
import co.il.jabrutouch.R;
import co.il.jabrutouch.ui.sign_in.SignInActivity;
import co.il.jabrutouch.ui.slide_screens.SlideFragments.Walkthrough1Fragment;
import co.il.jabrutouch.ui.slide_screens.SlideFragments.Walkthrough2Fragment;
import co.il.jabrutouch.ui.slide_screens.SlideFragments.Walkthrough3Fragment;
import co.il.jabrutouch.ui.slide_screens.SlideFragments.Walkthrough4Fragment;
import co.il.jabrutouch.user_manager.UserManager;



public class SlideActivity extends FragmentActivity implements
        Walkthrough1Fragment.OnFragmentInteractionListener,
        Walkthrough2Fragment.OnFragmentInteractionListener,
        Walkthrough3Fragment.OnFragmentInteractionListener,
        Walkthrough4Fragment.OnFragmentInteractionListener{


    private ViewPager mViewPager;
    private MyPagerAdapter mSlideScreensAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        initViews();
        setTheIndicator();
        initOnPageChangeListener();
    }




    /**
     * find views
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {

        mViewPager = findViewById(R.id.SA_viewPager_VP);
        mSlideScreensAdapter = new MyPagerAdapter(getSupportFragmentManager());




    }





    /**
     * set the slide screens indicator
     */
    private void setTheIndicator() {

        mViewPager.setAdapter(mSlideScreensAdapter);
        LinePageIndicator linePageIndicator = findViewById(R.id.activity_view_pager_indicator_underline);
        linePageIndicator.setViewPager(mViewPager);

    }




    /**
     * listener to swipe in last page of the view pager
     */
    private void initOnPageChangeListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private boolean isLastPageSwiped;
            private int counterPageScroll;

            @Override
            public void onPageScrolled(int position, float positionOffset, int i1) {

                if (position == 3 && positionOffset == 0 && !isLastPageSwiped){
                    if(counterPageScroll != 0){
                        isLastPageSwiped = true;
                        startSignInActivity();

                    }
                    counterPageScroll++;
                }else{
                    counterPageScroll = 0;
                }

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }



    /**
     * start SignInActivity when user swipe the last page in view pager
     */
    private void startSignInActivity() {

        UserManager.setNotFirstTime(true, this);

        Intent signInIntent = new Intent(this, SignInActivity.class);
        startActivity(signInIntent);
        finish();

    }





    /**
     * class adapter for the slide screens
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * get the item of the screen position to call the slide screens
         * @param position int, position of the view pager
         * @return slide screen fragment
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return Walkthrough1Fragment.newInstance();
                case 1:
                    return Walkthrough2Fragment.newInstance();
                case 2:
                    return Walkthrough3Fragment.newInstance();
                case 3:
                    return Walkthrough4Fragment.newInstance();

                default:
                    return Walkthrough1Fragment.newInstance();
            }
        }


        @Override
        public int getCount() {
            return 4;
        }
    }
}
