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
import co.il.jabrutouch.ui.main.downloads_screen.adapters.GemaraDownloadAdapter;
import co.il.model.model.PagesItem;
import co.il.model.model.GemaraDownloadObject;
import co.il.sqlcore.DBHandler;
import static co.il.sqlcore.DBKeys.GEMARA_TABLE;



public class GemaraDownloadsFragment extends Fragment implements GemaraDownloadAdapter.GemaraDownloadAdapterListener, View.OnClickListener {


    public static final String TAG = GemaraDownloadsFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private GemaraDownloadAdapter mGemaraDownloadAdapter;
    private LinearLayout mNoDownloadYetLL;
    private NestedScrollView mNestedScrollViewNSV;
    private Button mViewAllBtn;
    List<GemaraDownloadObject> masechtotList;


    public GemaraDownloadsFragment() {


    }


    public static GemaraDownloadsFragment newInstance() {
        GemaraDownloadsFragment fragment = new GemaraDownloadsFragment();
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
        return inflater.inflate(R.layout.fragment_gemara_in_downloads, container, false);
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

        mRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.GDF_recycler_RV);
        mNoDownloadYetLL = getView().findViewById(R.id.GDF_image_LL);
        mNestedScrollViewNSV = getView().findViewById(R.id.GDF_nested_NSV);
        mViewAllBtn = Objects.requireNonNull(getView()).findViewById(R.id.GDF_view_all_BTN);


    }


    /**
     * init listeners
     */
    private void initListeners() {

        mViewAllBtn.setOnClickListener(this);

    }




    private void getDownloadItemsListAndSetTheAdapter() {

        DBHandler dbHandler = new DBHandler(getContext());
        List<PagesItem> pagesItemDB = dbHandler.getAllGemaraDownloadTalmud(GEMARA_TABLE);
        mListener.itemsInGemaraDowloads(pagesItemDB.size());


        if (pagesItemDB.size() == 0) {

            mNestedScrollViewNSV.setVisibility(View.GONE);
            mNoDownloadYetLL.setVisibility(View.VISIBLE);

        } else {

            mNoDownloadYetLL.setVisibility(View.GONE);
            mNestedScrollViewNSV.setVisibility(View.VISIBLE);

            masechtotList = new ArrayList<>();

            String masechetName = "";
            int masechetLocation = -1;

            for (int i = 0; i < pagesItemDB.size(); i++) {

                if (!pagesItemDB.get(i).getMasechetName().equals(masechetName)) {

                    masechetName = pagesItemDB.get(i).getMasechetName();

                    GemaraDownloadObject testDownObject = new GemaraDownloadObject();
                    testDownObject.setMasechetName(pagesItemDB.get(i).getMasechetName());
                    testDownObject.getPagesItemsDB().add(pagesItemDB.get(i));
                    masechtotList.add(testDownObject);

                    masechetLocation++;

                } else {

                    masechtotList.get(masechetLocation).getPagesItemsDB().add(pagesItemDB.get(i));
                }


            }


            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mGemaraDownloadAdapter = new GemaraDownloadAdapter(getContext(), masechtotList, this);
            mRecyclerView.setAdapter(mGemaraDownloadAdapter);
        }

    }



    @Override
    public void onAttach(@NonNull Context context) {
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



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.GDF_view_all_BTN:

                mListener.onViewAllGemraClicked();


        }
    }



    @Override
    public void onVideoClicked(PagesItem pagesItem) {
        mListener.onVideoClicked(pagesItem);
    }



    @Override
    public void onAudioClicked(PagesItem pagesItem) {
        mListener.onAudioClicked(pagesItem);
    }



    @Override
    public void notifyDataInParentRecycler() {

        getDownloadItemsListAndSetTheAdapter();

    }



    @Override
    public void removeItemInLocalStorage(PagesItem pagesItem) {

        if (pagesItem.getAudio() != null && !pagesItem.getAudio().equals("")) {

            File file = new File(pagesItem.getAudio());
            boolean deleted = file.delete();
        }

        if (pagesItem.getVideo() != null && !pagesItem.getVideo().equals("")) {

            File file = new File(pagesItem.getVideo());
            boolean deleted = file.delete();
        }

        if (pagesItem.getPage() != null && !pagesItem.getPage().equals("")) {

            File file = new File(pagesItem.getPage());
            boolean deleted = file.delete();
        }


    }



    void notifyData(long currentPosition, int currentPageId) {

        if (mGemaraDownloadAdapter != null) {

            mGemaraDownloadAdapter.notifyData(currentPosition, currentPageId);

        }

    }




    public void setDeleteMode(boolean deleteMode) {

        if (mGemaraDownloadAdapter != null) {


            for (int i = 0; i < masechtotList.size(); i++) {

                for (int j = 0; j < masechtotList.get(i).getPagesItemsDB().size(); j++) {

                    masechtotList.get(i).getPagesItemsDB().get(j).setDeleteMode(deleteMode);

                }
            }

            mGemaraDownloadAdapter.notifyDataSetChanged();
        }


    }




    public interface OnFragmentInteractionListener {

        void onViewAllGemraClicked();

        void onVideoClicked(PagesItem pagesItem);

        void onAudioClicked(PagesItem pagesItem);

        void itemsInGemaraDowloads(int ListGemaraItems);
    }
}
