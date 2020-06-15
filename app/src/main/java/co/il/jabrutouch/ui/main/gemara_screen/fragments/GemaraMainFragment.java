package co.il.jabrutouch.ui.main.gemara_screen.fragments;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.model.model.masechet_list_model.MasechetItem;
import co.il.model.model.masechet_list_model.MasechetList;
import co.il.model.model.masechet_list_model.SederItem;
import co.il.jabrutouch.ui.main.gemara_screen.adapters.GemaraAdapter;



public class GemaraMainFragment extends Fragment implements View.OnClickListener, GemaraAdapter.GemaraAdapterListener {


    public static final String TAG = GemaraMainFragment.class.getSimpleName();
    private static final String MASECHTOT_LIST_GMARA = "MASECHTOT_LIST_GMARA";
    private RecyclerView mZeraimRecycler;
    private GemaraAdapter mGemaraZeraimAdapter;
    private GemaraAdapter mGemaraMoedAdapter;
    private GemaraAdapter mGemaraNoshimAdapter;
    private GemaraAdapter mGemaraNezikinAdapter;
    private GemaraAdapter mGemaraKodshimAdapter;
    private GemaraAdapter mMishnaTaharotAdapter;
    private RecyclerView mKodshimRecycler;
    private RecyclerView mTaharotRecycler;
    private RecyclerView mNezikinRecycler;
    private RecyclerView mNoshimRecycler;
    private RecyclerView mMoedRecycler;
    private LinearLayout mLinearZeraim;
    private LinearLayout mLinearMoed;
    private LinearLayout mLinearNoshim;
    private LinearLayout mLinearNezikin;
    private LinearLayout mLinearKodshim;
    private LinearLayout mLinearTaharot;
    private OnGemaraMainFragmentListener mListener;
    private MasechetList mMasechetotList;
    private ProgressBar mProgressBar;



    public GemaraMainFragment() {


    }


    public static GemaraMainFragment newInstance(MasechetList mMasechtotList) {
        GemaraMainFragment fragment = new GemaraMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(MASECHTOT_LIST_GMARA, mMasechtotList);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mMasechetotList = (MasechetList) getArguments().getSerializable(MASECHTOT_LIST_GMARA);
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
        return inflater.inflate(R.layout.fragment_gemara_main, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();

        setZeraimRecyclerView(deleteIfPagesEqualsToZero(mMasechetotList.getSeder().get(0)));
        setMoedRecyclerView(mMasechetotList.getSeder().get(1));
        setNoshimRecyclerView(mMasechetotList.getSeder().get(2));
        setNezikinRecyclerView(deleteIfPagesEqualsToZero(mMasechetotList.getSeder().get(3)));
        setKodshimRecyclerView(mMasechetotList.getSeder().get(4));
        setTaharotRecyclerView(deleteIfPagesEqualsToZero(mMasechetotList.getSeder().get(5)));

        setSearchView();
        setArrowViews();

    }




    /**
     * delete masechtot that has only in mishna
     * @param sederItem SederItem
     * @return sederItem
     */
    private SederItem deleteIfPagesEqualsToZero(SederItem sederItem) {

        List<MasechetItem> masechetItems = new ArrayList<>();

        for (int i = 0; i < sederItem.getMasechet().size(); i++) {
            if (sederItem.getMasechet().get(i).getPages() != 0){
                masechetItems.add(sederItem.getMasechet().get(i));
            }
        }

        sederItem.setMasechet(masechetItems);
        return sederItem;

    }




    /**
     * init views
     */
    private void initViews() {

        mLinearZeraim = Objects.requireNonNull(getView()).findViewById(R.id.GMF_linearZeraim_LL);
        mLinearMoed = Objects.requireNonNull(getView()).findViewById(R.id.GMF_linearMoed_LL);
        mLinearNoshim = Objects.requireNonNull(getView()).findViewById(R.id.GMF_linearNoshim_LL);
        mLinearNezikin = Objects.requireNonNull(getView()).findViewById(R.id.GMF_linearNezikin_LL);
        mLinearKodshim = Objects.requireNonNull(getView()).findViewById(R.id.GMF_linearKodshim_LL);
        mLinearTaharot = Objects.requireNonNull(getView()).findViewById(R.id.GMF_linearTaharot_LL);
        mProgressBar = Objects.requireNonNull(getView()).findViewById(R.id.GMF_progress_bar);

    }





    /**
     * set search view
     */
    private void setSearchView() {

        SearchView searchView = Objects.requireNonNull(getView()).findViewById(R.id.GMF_search_SV);

        final TextView title = Objects.requireNonNull(getView()).findViewById(R.id.GMF_title_TV);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title.setVisibility(View.GONE);

            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                title.setVisibility(View.VISIBLE);

                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                if (mGemaraZeraimAdapter != null) {

                    mGemaraZeraimAdapter.getFilter().filter(s);
                }

                if (mGemaraMoedAdapter != null) {

                    mGemaraMoedAdapter.getFilter().filter(s);
                }

                if (mGemaraNoshimAdapter != null) {

                    mGemaraNoshimAdapter.getFilter().filter(s);
                }

                if (mGemaraNezikinAdapter != null) {

                    mGemaraNezikinAdapter.getFilter().filter(s);
                }

                if (mGemaraKodshimAdapter != null) {

                    mGemaraKodshimAdapter.getFilter().filter(s);
                }

                if (mMishnaTaharotAdapter != null) {

                    mMishnaTaharotAdapter.getFilter().filter(s);
                }



                return true;
            }
        });


    }




    /**
     * set arrow views to close and open recycler
     */
    private void setArrowViews() {

        ImageView mZeraimArrow = Objects.requireNonNull(getView()).findViewById(R.id.GMF_zeraim_arrow_IV);
        ImageView mMoedArrow = getView().findViewById(R.id.GMF_moed_arrow_IV);
        ImageView mNoshimArrow = getView().findViewById(R.id.GMF_noshim_arrow_IV);
        ImageView mNezikinArrow = getView().findViewById(R.id.GMF_nezikin_arrow_IV);
        ImageView mKodshimArrow = getView().findViewById(R.id.GMF_kodshim_arrow_IV);
        ImageView mTaharotArrow = getView().findViewById(R.id.GMF_taharot_arrow_IV);

        mZeraimArrow.setOnClickListener(this);
        mMoedArrow.setOnClickListener(this);
        mNoshimArrow.setOnClickListener(this);
        mNezikinArrow.setOnClickListener(this);
        mKodshimArrow.setOnClickListener(this);
        mTaharotArrow.setOnClickListener(this);

    }




    private void setKodshimRecyclerView(SederItem sederItem) {

        mKodshimRecycler = Objects.requireNonNull(getView()).findViewById(R.id.GMF_kodshim_recycler_RV);
        mKodshimRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mGemaraKodshimAdapter = new GemaraAdapter(getContext(), sederItem, this, mLinearKodshim);
        mKodshimRecycler.setAdapter(mGemaraKodshimAdapter);
    }



    private void setTaharotRecyclerView(SederItem sederItem) {

        mTaharotRecycler = Objects.requireNonNull(getView()).findViewById(R.id.GMF_taharot_recycler_RV);
        mTaharotRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMishnaTaharotAdapter = new GemaraAdapter(getContext(), sederItem, this, mLinearTaharot);
        mTaharotRecycler.setAdapter(mMishnaTaharotAdapter);
    }



    private void setNezikinRecyclerView(SederItem sederItem) {

        mNezikinRecycler = Objects.requireNonNull(getView()).findViewById(R.id.GMF_nezikin_recycler_RV);
        mNezikinRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mGemaraNezikinAdapter = new GemaraAdapter(getContext(), sederItem, this, mLinearNezikin);
        mNezikinRecycler.setAdapter(mGemaraNezikinAdapter);
    }



    private void setNoshimRecyclerView(SederItem sederItem) {


        mNoshimRecycler = Objects.requireNonNull(getView()).findViewById(R.id.GMF_noshim_recycler_RV);
        mNoshimRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mGemaraNoshimAdapter = new GemaraAdapter(getContext(), sederItem, this, mLinearNoshim);
        mNoshimRecycler.setAdapter(mGemaraNoshimAdapter);
    }



    private void setMoedRecyclerView(SederItem sederItem) {

        mMoedRecycler = Objects.requireNonNull(getView()).findViewById(R.id.GMF_moed_recycler_RV);
        mMoedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mGemaraMoedAdapter = new GemaraAdapter(getContext(), sederItem, this, mLinearMoed);
        mMoedRecycler.setAdapter(mGemaraMoedAdapter);

    }



    private void setZeraimRecyclerView(SederItem sederItem) {

        mZeraimRecycler = Objects.requireNonNull(getView()).findViewById(R.id.GMF_zeraim_recycler_RV);
        mZeraimRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mGemaraZeraimAdapter = new GemaraAdapter(getContext(), sederItem, this, mLinearZeraim);
        mZeraimRecycler.setAdapter(mGemaraZeraimAdapter);

    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGemaraMainFragmentListener) {
            mListener = (OnGemaraMainFragmentListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.GMF_zeraim_arrow_IV:

                manageArrowToCloseAndOpenView(mZeraimRecycler, (ImageView) view);
                break;

            case R.id.GMF_moed_arrow_IV:

                manageArrowToCloseAndOpenView(mMoedRecycler, (ImageView) view);
                break;

            case R.id.GMF_noshim_arrow_IV:

                manageArrowToCloseAndOpenView(mNoshimRecycler, (ImageView) view);
                break;

            case R.id.GMF_nezikin_arrow_IV:

                manageArrowToCloseAndOpenView(mNezikinRecycler, (ImageView) view);
                break;

            case R.id.GMF_kodshim_arrow_IV:

                manageArrowToCloseAndOpenView(mKodshimRecycler, (ImageView) view);
                break;

            case R.id.GMF_taharot_arrow_IV:

                manageArrowToCloseAndOpenView(mTaharotRecycler, (ImageView) view);
                break;


        }

    }




    /**
     * manage arrow to close and open view
     * @param recyclerToManage RecyclerView
     * @param imageView ImageView
     */
    private void manageArrowToCloseAndOpenView(RecyclerView recyclerToManage, ImageView imageView) {

        if (recyclerToManage.getVisibility() == View.VISIBLE) {

            recyclerToManage.setVisibility(View.GONE);
            imageView.setRotation(180);

        } else {

            recyclerToManage.setVisibility(View.VISIBLE);
            imageView.setRotation(0);
        }


    }




    @Override
    public void onFilterSizeZero(LinearLayout sederLinear) {
        sederLinear.setVisibility(View.GONE);
    }




    @Override
    public void onFilterNonSizeZero(LinearLayout sederLinear) {
        sederLinear.setVisibility(View.VISIBLE);

    }



    @Override
    public void onMasechetClicked(int sederOrder, MasechetItem masechtotItem) {

        mProgressBar.setVisibility(View.VISIBLE);

        mListener.onMasechetGemaraClicked(masechtotItem, sederOrder);


    }

    public void clearProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }



    public interface OnGemaraMainFragmentListener {

        void onMasechetGemaraClicked(MasechetItem masechtotItem, int sederOrder);

        void hideToolBar();
    }
}
