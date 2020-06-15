package co.il.jabrutouch.ui.main.video_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.rd.PageIndicatorView;
import java.util.List;
import co.il.jabrutouch.R;
import co.il.jabrutouch.ui.slide_screens.SlideFragments.Walkthrough1Fragment;
import co.il.model.model.Gallery;




public class GallerySlideActivity extends AppCompatActivity implements Walkthrough1Fragment.OnFragmentInteractionListener {


    private ViewPager mViewPager;
    private MyPagerAdapter mSlideScreensAdapter;
    private List<Gallery> myList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_slide);


        initViews();
        setTheIndicator();

    }


    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

    }

    /**
     * find views
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {

        mViewPager = findViewById(R.id.GSA_viewPager_VP);
        mSlideScreensAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myList = (List<Gallery>) getIntent().getSerializableExtra(VideoActivity.IMAGES_LIST);
    }


    /**
     * set the slide screens indicator
     */
    private void setTheIndicator() {

        final PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(myList.size()); // specify total count of indicators
        pageIndicatorView.setSelection(2);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });

        mViewPager.setAdapter(mSlideScreensAdapter);
//        LinePageIndicator linePageIndicator = findViewById(R.id.GSA_indicator_underline);
//        linePageIndicator.setViewPager(mViewPager);
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
         *
         * @param position int, position of the view pager
         * @return slide screen fragment
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {

                default:
                    return GalleryFragment.newInstance(myList.get(position));
            }
        }


        @Override
        public int getCount() {
            return myList.size();
        }
    }


}

