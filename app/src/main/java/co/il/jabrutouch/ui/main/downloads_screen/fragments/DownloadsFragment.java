package co.il.jabrutouch.ui.main.downloads_screen.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import java.util.Objects;
import co.il.jabrutouch.R;


public class DownloadsFragment extends Fragment {
    public static final String TAG = DownloadsFragment.class.getSimpleName();


    private OnDownloadsFragmentListener mListener;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DownloadsAdapter mPagerAdapter;
    private TextView mDeleteBtnTV;
    private int gemaraDownloadList;
    private int mishnaDownloadList;
    private boolean mishnaIsOpen;
    private boolean gemaraIsOpen;

    public DownloadsFragment() {


    }


    public static DownloadsFragment newInstance() {
        DownloadsFragment fragment = new DownloadsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListener.hideToolBar();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloads, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initTabLayout();
        initListeners();

    }




    /**
     * init views
     */
    private void initViews() {

        mTabLayout = Objects.requireNonNull(getView()).findViewById(R.id.DF_tabLayout_TL);
        mViewPager = Objects.requireNonNull(getView()).findViewById(R.id.DF_viewpager_VP);
        mDeleteBtnTV = Objects.requireNonNull(getView()).findViewById(R.id.DF_delete_btn_TV);


    }




    /**
     * init tab layout
     */
    private void initTabLayout() {

        mPagerAdapter = new DownloadsAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }



    /**
     * init listeners
     */
    private void initListeners() {

        mDeleteBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setViewClickedAnimation(mDeleteBtnTV);

                if (mDeleteBtnTV.getText().toString().equals(getResources().getString(R.string.delete))) {

                    setDeleteMode(true);
                    mDeleteBtnTV.setText(getResources().getString(R.string.done));

                }else {

                    setDeleteMode(false);
                    mDeleteBtnTV.setText(getResources().getString(R.string.delete));

                }

            }
        });



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 0){

                    gemaraIsOpen = true;
                    mishnaIsOpen = false;

                    if (gemaraDownloadList == 0){

                        mDeleteBtnTV.setVisibility(View.GONE);
                    }else {

                        mDeleteBtnTV.setVisibility(View.VISIBLE);

                    }

                }else if (position == 1){

                    mishnaIsOpen = true;
                    gemaraIsOpen = false;

                    if (mishnaDownloadList == 0){

                        mDeleteBtnTV.setVisibility(View.GONE);
                    }else {

                        mDeleteBtnTV.setVisibility(View.VISIBLE);

                    }


                }


            }

            @Override
            public void onPageSelected(int position) {

//                if (position == 0){
//
//                    gemaraIsOpen = true;
//                    mishnaIsOpen = false;
//
//                    if (gemaraDownloadList == 0){
//
//                        mDeleteBtnTV.setVisibility(View.GONE);
//                    }else {
//
//                        mDeleteBtnTV.setVisibility(View.VISIBLE);
//
//                    }
//
//                }else if (position == 1){
//
//                    mishnaIsOpen = true;
//                    gemaraIsOpen = false;
//
//                    if (mishnaDownloadList == 0){
//
//                        mDeleteBtnTV.setVisibility(View.GONE);
//                    }else {
//
//                        mDeleteBtnTV.setVisibility(View.VISIBLE);
//
//                    }


//                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    /**
     * init delete button
     */
    private void initDeleteBtn() {

        if (gemaraIsOpen){

            if (gemaraDownloadList == 0){

                mDeleteBtnTV.setVisibility(View.GONE);
            }else {

                mDeleteBtnTV.setVisibility(View.VISIBLE);

            }
        }



    }



    private void setViewClickedAnimation(View mView) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        mView.startAnimation(animation);

    }




    private void setDeleteMode(boolean deleteMode) {


        mPagerAdapter.setDeleteMode(deleteMode);


    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDownloadsFragmentListener) {
            mListener = (OnDownloadsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public void notifyData(long currentPosition, int currentPageId) {

        mPagerAdapter.notifyData(currentPosition, currentPageId);

    }



    public void itemsInGemaraDownloads(int listGemaraItems) {

        gemaraDownloadList = listGemaraItems;
        initDeleteBtn();
    }



    public void itemsInMishnaDowloads(int mishnaListItems) {

        mishnaDownloadList = mishnaListItems;

        if (mViewPager.getCurrentItem() == 1){

            mDeleteBtnTV.post(new Runnable() {
                @Override
                public void run() {
                    if (mishnaDownloadList == 0){

                        mDeleteBtnTV.setVisibility(View.GONE);
                    }else {

                        mDeleteBtnTV.setVisibility(View.VISIBLE);

                    }
            }});




        }

    }



    public interface OnDownloadsFragmentListener {

        void hideToolBar();
    }




    /**
     * adapter for the view pager in download screen
     */
    private class DownloadsAdapter extends FragmentPagerAdapter {

        public DownloadsAdapter(FragmentManager fm) {
            super(fm);
        }

        GemaraDownloadsFragment gemaraDownloadsFragment;
        MishnaDownloadsFragment mishnaDownloadsFragment;

        @Override
        public Fragment getItem(int position) {


            if (gemaraIsOpen){

                if (gemaraDownloadList == 0){

                    mDeleteBtnTV.setVisibility(View.GONE);
                }else {

                    mDeleteBtnTV.setVisibility(View.VISIBLE);

                }
            }else if (mishnaIsOpen){

                if (mishnaDownloadList == 0){

                    mDeleteBtnTV.setVisibility(View.GONE);
                }else {

                    mDeleteBtnTV.setVisibility(View.VISIBLE);

                }

            }
            else {

                if (gemaraDownloadList == 0){

                    mDeleteBtnTV.setVisibility(View.GONE);
                }else {

                    mDeleteBtnTV.setVisibility(View.VISIBLE);

                }
            }

            switch (position) {

                case 0:

                    return gemaraDownloadsFragment = GemaraDownloadsFragment.newInstance();
                case 1:

                    return mishnaDownloadsFragment = MishnaDownloadsFragment.newInstance();
                default:
                    return gemaraDownloadsFragment = GemaraDownloadsFragment.newInstance();
            }
        }


        @Override
        public int getCount() {
            return 2;
        }



        /**
         * set page title in tab layout
         *
         * @param position int
         * @return CharSequence, title
         */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.gemara);
                case 1:
                    return getString(R.string.mishna);
                default:
                    return getString(R.string.gemara);
            }
        }



        void notifyData(long currentPosition, int currentPageId) {


            if (gemaraDownloadsFragment != null) {

                gemaraDownloadsFragment.notifyData(currentPosition, currentPageId);
            }

            if (mishnaDownloadsFragment != null) {

                mishnaDownloadsFragment.notifyData(currentPosition, currentPageId);
            }


        }



        void setDeleteMode(boolean deleteMode) {

            if (gemaraDownloadsFragment != null) {

                gemaraDownloadsFragment.setDeleteMode(deleteMode);
            }

            if (mishnaDownloadsFragment != null) {

                mishnaDownloadsFragment.setDeleteMode(deleteMode);
            }

        }



    }
}
