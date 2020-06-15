package co.il.jabrutouch.ui.main.mishna_screen.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dinuscxj.progressbar.CircleProgressBar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.model.model.MishnaMishnaiot;
import co.il.model.model.MishnayotItem;
import co.il.model.model.masechet_list_model.MasechetItem;
import co.il.jabrutouch.ui.main.mishna_screen.adapters.MishnaMediaAdapter;




public class MishnaMediaFragment extends Fragment implements MishnaMediaAdapter.MishnaMediaAdapterListener, View.OnClickListener {

    public static final String TAG = MishnaMediaFragment.class.getSimpleName();
    private static final String MISHNAIOT = "MISHNAIOT";
    private static final String MASECHET_ITEM = "MASECHET_ITEM";
    private static final String CHAPTER_NUMBER = "CHAPTER_NUMBER";
    private static final String SEDER_ORDER = "SEDER_ORDER";
    private OnMishnaDownloadFragmentListener mListener;
    private TextView mChapterTitle;
    private RecyclerView mChapterRecyclerView;
    private MishnaMediaAdapter mMishnaMediaAdapter;
    private ImageView mDownloadBtn;
    private TextView mDoneBtn;
    private MishnaMishnaiot mMishnaiot;
    private MasechetItem mMasechetItem;
    private int mChapterNumber;
    private int mSederOrder;
    private CircleProgressBar mProgressBar;

    public MishnaMediaFragment() {

    }


    public static MishnaMediaFragment newInstance(MishnaMishnaiot mishnaMishnaiot, MasechetItem masechetItem, int sederOrder, int chapter) {
        MishnaMediaFragment fragment = new MishnaMediaFragment();
        Bundle args = new Bundle();
        args.putSerializable(MISHNAIOT, mishnaMishnaiot);
        args.putSerializable(MASECHET_ITEM, masechetItem);
        args.putInt(CHAPTER_NUMBER, chapter);
        args.putInt(SEDER_ORDER, sederOrder);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMishnaiot = (MishnaMishnaiot) getArguments().getSerializable(MISHNAIOT);
            mMasechetItem = (MasechetItem) getArguments().getSerializable(MASECHET_ITEM);
            mChapterNumber = getArguments().getInt(CHAPTER_NUMBER);
            mSederOrder =  getArguments().getInt(SEDER_ORDER);

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mListener.hideToolBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mishna_media, container, false);
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

        mChapterTitle = Objects.requireNonNull(getView()).findViewById(R.id.MDF_title_TV);
        mChapterRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.MDF_recycler_RV);
        mDownloadBtn = getView().findViewById(R.id.MDF_download_IV);
        mDoneBtn = getView().findViewById(R.id.MDF_done_TV);
        mProgressBar = getView().findViewById(R.id.MMF_progress_bar_PB);

    }




    /**
     * init listeners
     */
    private void initListeners() {

        mDownloadBtn.setOnClickListener(this);
        mDoneBtn.setOnClickListener(this);

    }





    /**
     * set title
     */
    @SuppressLint("SetTextI18n")
    private void setTitle() {

        mChapterTitle.setText(mMasechetItem.getName() + " "  + mChapterNumber);

    }




    /**
     * set mishna download recycler view
     */
    private void setChapterDownloadRecylcer() {

        mChapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Collections.sort(mMishnaiot.getMishnayot(), new Comparator<MishnayotItem>(){
            @Override
            public int compare(MishnayotItem obj1, MishnayotItem obj2) {
                return Integer.compare(obj1.getMishna(), obj2.getMishna()); // To compare integer values

            }

        });


        mMishnaMediaAdapter = new MishnaMediaAdapter(getContext(),mMasechetItem, mMishnaiot.getMishnayot(), mSederOrder,  this);
        Objects.requireNonNull(mChapterRecyclerView.getItemAnimator()).setChangeDuration(0);

        mChapterRecyclerView.setAdapter(mMishnaMediaAdapter);

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMishnaDownloadFragmentListener) {
            mListener = (OnMishnaDownloadFragmentListener) context;
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

            case R.id.MDF_download_IV:

                mMishnaMediaAdapter.onDownloadMode();
                mDownloadBtn.setVisibility(View.GONE);
                mDoneBtn.setVisibility(View.VISIBLE);
                break;

            case R.id.MDF_done_TV:

                mMishnaMediaAdapter.onDoneMode();
                mDownloadBtn.setVisibility(View.VISIBLE);
                mDoneBtn.setVisibility(View.GONE);
                break;

        }


    }




    @Override
    public void onMishnaAudioDownloadClicked(MishnayotItem mishnayotItem, int position) {

        mListener.onMishnaAudioDownloadClicked(mishnayotItem, position);

    }




    @Override
    public void startProgress() {

        mDownloadBtn.setVisibility(View.VISIBLE);
        mDoneBtn.setVisibility(View.GONE);

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);

    }

    @Override
    public void onAudioMishnaClicked(MishnayotItem mishnayotItem) {
        mListener.onAudioMishnaClicked(mishnayotItem);
    }

    @Override
    public void onVideoMishnaClicked(MishnayotItem mishnayotItem) {
            mListener.onVideoMishnaClicked(mishnayotItem);
    }

    @Override
    public void onMishnaVideoDownloadClicked(MishnayotItem mishnayotItem, int position) {
        mListener.onMishnaVideoDownloadClicked(mishnayotItem, position);
    }


    public void stopAudioProgress(int position, MishnayotItem mishnayotItem) {

        if (mMishnaiot.getMishnayot().get(position).getId() == mishnayotItem.getId()) {

            mMishnaiot.getMishnayot().get(position).setAudioProgress(100);
            mMishnaMediaAdapter.notifyItemChanged(position);
        }
    }


    public void onVideoProgressChange(long percentDone, int position, MishnayotItem mishnayotItem) {

        if (mMishnaiot.getMishnayot().get(position).getId() == mishnayotItem.getId()) {

            mMishnaiot.getMishnayot().get(position).setVideoProgress((int) percentDone);
            mMishnaMediaAdapter.notifyItemChanged(position);
        }
    }


    public void stopVideoProgress(int position, MishnayotItem mishnayotItem) {

        if (mMishnaiot.getMishnayot().get(position).getId() == mishnayotItem.getId()) {

            mMishnaiot.getMishnayot().get(position).setVideoProgress(100);
            mMishnaMediaAdapter.notifyItemChanged(position);
        }
    }


    public void onAudioProgressChange(int percentDone, int position, MishnayotItem mishnayotItem) {

        if (mMishnaiot.getMishnayot().get(position).getId() == mishnayotItem.getId()) {

            mMishnaiot.getMishnayot().get(position).setAudioProgress(percentDone);
            mMishnaMediaAdapter.notifyItemChanged(position);
        }
    }

    public void notifyData(long currentPosition, int currentPageId) {


        for (int i = 0; i < mMishnaiot.getMishnayot().size(); i++) {
            if (mMishnaiot.getMishnayot().get(i).getId() == currentPageId){
                mMishnaiot.getMishnayot().get(i).setTimeLine(currentPosition);
            }
        }

        mMishnaMediaAdapter.notifyDataSetChanged();


    }


    public interface OnMishnaDownloadFragmentListener {

        void onMishnaAudioDownloadClicked(MishnayotItem mishnayotItem, int position);

        void hideToolBar();

        void onAudioMishnaClicked(MishnayotItem mishnayotItem);

        void onVideoMishnaClicked(MishnayotItem mishnayotItem);

        void onMishnaVideoDownloadClicked(MishnayotItem mishnayotItem, int position);
    }
}
