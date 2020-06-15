package co.il.jabrutouch.ui.main.downloads_screen.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.jabrutouch.ui.main.downloads_screen.adapters.MishnaDownloadAdapter;
import co.il.model.model.MishnaDownloadObject;
import co.il.model.model.MishnayotItem;
import co.il.sqlcore.DBHandler;
import static co.il.sqlcore.DBKeys.MISHNA_TABLE;



public class MishnaDownloadsFragment extends Fragment implements MishnaDownloadAdapter.MishnaDownloadAdapterListener {


    public static final String TAG = MishnaDownloadsFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private MishnaDownloadAdapter mMishnaDownloadAdapter;
    private Button mViewAllBtn;

    private NestedScrollView mNestedScrollViewNSV;
    private LinearLayout mNoDownloadYetLL;
    List<MishnaDownloadObject> masechtotList;

    public MishnaDownloadsFragment() {

    }


    public static MishnaDownloadsFragment newInstance() {
        MishnaDownloadsFragment fragment = new MishnaDownloadsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mishna_in_downloads, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
        getDownloadItemsListAndSetTheAdapter();

    }



    /**
     * init views
     */
    private void initViews() {

        mRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.MDF_recycler_RV);
        mNoDownloadYetLL = getView().findViewById(R.id.MIDF_image_LL);
        mNestedScrollViewNSV = getView().findViewById(R.id.MIDF_nested_NSV);
        mViewAllBtn = Objects.requireNonNull(getView()).findViewById(R.id.MIDF_view_all_BTN);

    }




    /**
     * init listeners
     */
    private void initListeners() {

        mViewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onViewAllMishnaClicked();

            }
        });

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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




    private void getDownloadItemsListAndSetTheAdapter() {

        DBHandler dbHandler = new DBHandler(getContext());
        List<MishnayotItem> mishnayotItemsDB = dbHandler.getAllMishnaDownloadTalmud(MISHNA_TABLE);
        mListener.itemsInMishnaDowloads(mishnayotItemsDB.size());

        if (mishnayotItemsDB.size() == 0) {

            mNestedScrollViewNSV.setVisibility(View.GONE);
            mNoDownloadYetLL.setVisibility(View.VISIBLE);


        } else {

            mNoDownloadYetLL.setVisibility(View.GONE);
            mNestedScrollViewNSV.setVisibility(View.VISIBLE);

            masechtotList = new ArrayList<>();

            String masechetName = "";
            int masechetLocation = -1;

            for (int i = 0; i < mishnayotItemsDB.size(); i++) {

                if (!mishnayotItemsDB.get(i).getMasechetName().equals(masechetName)) {

                    masechetName = mishnayotItemsDB.get(i).getMasechetName();

                    MishnaDownloadObject testDownObject = new MishnaDownloadObject();
                    testDownObject.setMasechetName(mishnayotItemsDB.get(i).getMasechetName());
                    testDownObject.getMishnayotItemsDB().add(mishnayotItemsDB.get(i));
                    masechtotList.add(testDownObject);

                    masechetLocation++;

                } else {

                    masechtotList.get(masechetLocation).getMishnayotItemsDB().add(mishnayotItemsDB.get(i));
                }


            }


            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mMishnaDownloadAdapter = new MishnaDownloadAdapter(getContext(), masechtotList, this);
            mRecyclerView.setAdapter(mMishnaDownloadAdapter);
        }

    }




    @Override
    public void onVideoClicked(MishnayotItem mishnayotItem) {
        mListener.onVideoMishnaClicked(mishnayotItem);
    }




    @Override
    public void onAudioClicked(MishnayotItem mishnayotItem) {
        mListener.onAudioMishnaClicked(mishnayotItem);
    }



    @Override
    public void notifyDataInParentRecycler() {

        getDownloadItemsListAndSetTheAdapter();
    }



    @Override
    public void removeItemInLocalStorage(MishnayotItem mishnayotItem) {

        if (mishnayotItem.getAudio() != null && !mishnayotItem.getAudio().equals("")) {

            File file = new File(mishnayotItem.getAudio());
            boolean deleted = file.delete();
        }

        if (mishnayotItem.getVideo() != null && !mishnayotItem.getVideo().equals("")) {

            File file = new File(mishnayotItem.getVideo());
            boolean deleted = file.delete();
        }

        if (mishnayotItem.getPage() != null && !mishnayotItem.getPage().equals("")) {

            File file = new File(mishnayotItem.getPage());
            boolean deleted = file.delete();
        }

    }




    public void notifyData(long currentPosition, int currentPageId) {

        if (mMishnaDownloadAdapter != null) {


            mMishnaDownloadAdapter.notifyData(currentPosition, currentPageId);


        }
    }




    public void setDeleteMode(boolean deleteMode) {


        if (mMishnaDownloadAdapter != null){

            for (int i = 0; i < masechtotList.size(); i++) {

                for (int j = 0; j < masechtotList.get(i).getMishnayotItemsDB().size(); j++) {
                    masechtotList.get(i).getMishnayotItemsDB().get(j).setDeleteMode(deleteMode);
                }
            }

            mMishnaDownloadAdapter.notifyDataSetChanged();

        }
    }




    public interface OnFragmentInteractionListener {

        void onViewAllMishnaClicked();

        void onVideoMishnaClicked(MishnayotItem mishnayotItem);

        void onAudioMishnaClicked(MishnayotItem mishnayotItem);

        void itemsInMishnaDowloads(int mishnaListItems);
    }
}
