package co.il.jabrutouch.ui.main.gemara_screen.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.dinuscxj.progressbar.CircleProgressBar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.model.model.Result;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.GemaraPages;
import co.il.model.model.PagesItem;
import co.il.jabrutouch.ui.main.gemara_screen.adapters.GemaraMediaAdapter;
import co.il.model.model.masechet_list_model.MasechetItem;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;




public class GemaraMediaFragment extends Fragment implements GemaraMediaAdapter.GemaraMediaAdapterListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = GemaraMediaFragment.class.getSimpleName();
    private static final String PAGES = "PAGES";
    private static final String MASECHET_NAME = "MASECHET_NAME";
    private static final String SEDER_ORDER = "SEDER_ORDER";
    private OnGemaraDownloadFragmentListener mListener;
    private TextView mChapterTitle;
    private RecyclerView mChapterRecyclerView;
    private GemaraMediaAdapter mGemaraMediaAdapter;
    private ImageView mDownloadBtn;
    private TextView mDoneBtn;
    private GemaraPages mPagesList;
    private MasechetItem mMasechet;
    private int mSederOrder;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<PagesItem> mPagesListForAdapter;
    private CircleProgressBar mProgressBar;


    public GemaraMediaFragment() {

    }


    public static GemaraMediaFragment newInstance(GemaraPages gemaraPages, MasechetItem masechetItem, int sederOrder) {
        GemaraMediaFragment fragment = new GemaraMediaFragment();
        Bundle args = new Bundle();
        args.putSerializable(PAGES, gemaraPages);
        args.putInt(SEDER_ORDER, sederOrder);
        args.putSerializable(MASECHET_NAME, masechetItem);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPagesList = (GemaraPages) getArguments().getSerializable(PAGES);
            mMasechet = (MasechetItem) getArguments().getSerializable(MASECHET_NAME);
            mSederOrder = getArguments().getInt(SEDER_ORDER);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListener.hideToolBar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gemara_media, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initViews();
        initListeners();
        setTitle();
        setChapterDownloadRecylcer();

    }


    /**
     * init views
     */
    private void initViews() {

        mChapterTitle = Objects.requireNonNull(getView()).findViewById(R.id.GDF_title_TV);
        mChapterRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.GMF_recycler_RV);
        mDownloadBtn = getView().findViewById(R.id.GDF_download_IV);
        mDoneBtn = getView().findViewById(R.id.GDF_done_TV);
        mSwipeRefreshLayout = getView().findViewById(R.id.GDF_swipe_container_LL);
        mProgressBar = getView().findViewById(R.id.VCF_progress_bar_PB);


    }




    /**
     * init listeners
     */
    private void initListeners() {

        mDownloadBtn.setOnClickListener(this);
        mDoneBtn.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }




    /**
     * set page title
     */
    private void setTitle() {

        mChapterTitle.setText(mMasechet.getName());
    }





    /**
     * set chapter download recycler
     */
    private void setChapterDownloadRecylcer() {


        mChapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPagesListForAdapter = mPagesList.getPages();

        Collections.sort(mPagesListForAdapter, new Comparator<PagesItem>() {
            public int compare(PagesItem obj1, PagesItem obj2) {

                return Integer.compare(obj1.getPageNumber(), obj2.getPageNumber()); // To compare integer values

            }
        });

        mGemaraMediaAdapter = new GemaraMediaAdapter(getContext(), mPagesListForAdapter, mMasechet, mSederOrder, this);
        Objects.requireNonNull(mChapterRecyclerView.getItemAnimator()).setChangeDuration(0);

        mChapterRecyclerView.setAdapter(mGemaraMediaAdapter);


    }





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnGemaraDownloadFragmentListener) {
            mListener = (OnGemaraDownloadFragmentListener) context;
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




    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.GDF_download_IV:


                mGemaraMediaAdapter.onDownloadMode();
                mDownloadBtn.setVisibility(View.GONE);
                mDoneBtn.setVisibility(View.VISIBLE);
                break;


            case R.id.GDF_done_TV:

                mGemaraMediaAdapter.onDoneMode();
                mDownloadBtn.setVisibility(View.VISIBLE);
                mDoneBtn.setVisibility(View.GONE);
                break;

        }


    }


    private void setTextClickedAnimiation(TextView textView) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        textView.startAnimation(animation);


    }


    private void setImageClickedAnimation(ImageView icon) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        icon.startAnimation(animation);

    }


    @Override
    public void onAudioDownloadClicked(PagesItem pagesItem, int position) {

        mListener.onAudioDownloadClicked(pagesItem, position);

    }



    @Override
    public void onAudioClicked(PagesItem pagesItem) {

        mListener.onAudioClicked(pagesItem);
    }




    @Override
    public void onVideoClicked(PagesItem pagesItem) {
        mListener.onVideoClicked(pagesItem);
    }

    @Override
    public void onVideoDownloadClicked(PagesItem pagesItem, int position) {
        mListener.onVideoDownloadClicked(pagesItem, position);
    }





    public void stopAudioProgress(int position, PagesItem pagesItem) {


        if (mPagesListForAdapter.get(position).getId() == pagesItem.getId()) {

            mPagesListForAdapter.get(position).setAudioProgress(100);
            mGemaraMediaAdapter.notifyItemChanged(position);
        }
    }



   public void cancelAudioProgress(int position, PagesItem pagesItem) {


        if (mPagesListForAdapter.get(position).getId() == pagesItem.getId()) {

            mPagesListForAdapter.get(position).setAudioProgress(0);
            mGemaraMediaAdapter.notifyItemChanged(position);
        }
    }




    public void onAudioProgressChange(int percentDone, int position, PagesItem pagesItem) {

        if (mPagesListForAdapter.get(position).getId() == pagesItem.getId()) {

            mPagesListForAdapter.get(position).setAudioProgress(percentDone);
            mGemaraMediaAdapter.notifyItemChanged(position);
        }
    }





    /**
     * on swipe to refresh called
     */
    @Override
    public void onRefresh() {

        if (isNetworkAvailable()) {

            RequestManager.getGemara(UserManager.getToken(getContext()), String.valueOf(mMasechet.getId())).subscribe(new Observer<Result<GemaraPages>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result<GemaraPages> gemaraListResult) {

                    mPagesListForAdapter.clear();
                    mPagesListForAdapter.addAll(gemaraListResult.getData().getPages());
                    mGemaraMediaAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

        } else {
            mSwipeRefreshLayout.setRefreshing(false);

        }


    }





    /**
     * check if network available
     *
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }






    public void refreshMediaAdapter() {


        RequestManager.getGemara(UserManager.getToken(getContext()), String.valueOf(mMasechet.getId())).subscribe(new Observer<Result<GemaraPages>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<GemaraPages> gemaraListResult) {

                mPagesListForAdapter.clear();
                mPagesListForAdapter.addAll(gemaraListResult.getData().getPages());
                mGemaraMediaAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }





    public void onVideoProgressChange(long percentDone, int position, PagesItem pagesItem) {

        if (position != -1){


            if (mPagesListForAdapter.get(position).getId() == pagesItem.getId()) {

                mPagesListForAdapter.get(position).setVideoProgress((int) percentDone);
                mGemaraMediaAdapter.notifyItemChanged(position);
            }
        }


    }




    public void stopVideoProgress(int position, PagesItem pagesItem) {

        if (mPagesListForAdapter.get(position).getId() == pagesItem.getId()) {

            mPagesListForAdapter.get(position).setVideoProgress(100);
            mGemaraMediaAdapter.notifyItemChanged(position);
        }
    }






    public void notifyData(long currentPosition, int currentPageId) {


        for (int i = 0; i < mPagesListForAdapter.size(); i++) {
            if (mPagesListForAdapter.get(i).getId() == currentPageId){
                mPagesListForAdapter.get(i).setTimeLine(currentPosition);
            }
        }

        mGemaraMediaAdapter.notifyDataSetChanged();


    }






    public interface OnGemaraDownloadFragmentListener {

        void onAudioDownloadClicked(PagesItem pagesItem, int position);

        void onAudioClicked(PagesItem pagesItem);

        void hideToolBar();

        void onVideoClicked(PagesItem pagesItem);

        void onVideoDownloadClicked(PagesItem pagesItem, int position);
    }
}
