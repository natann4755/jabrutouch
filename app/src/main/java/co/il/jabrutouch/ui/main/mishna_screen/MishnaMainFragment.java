package co.il.jabrutouch.ui.main.mishna_screen;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.model.model.masechet_list_model.MasechetItem;
import co.il.model.model.masechet_list_model.MasechetList;
import co.il.model.model.masechet_list_model.SederItem;
import co.il.jabrutouch.ui.main.mishna_screen.adapters.MishnaAdapter;


public class MishnaMainFragment extends Fragment implements View.OnClickListener, MishnaAdapter.MishnaAdapterListener {

    public static final String TAG = MishnaMainFragment.class.getSimpleName();
    private static final String MASECHTOT_LIST = "MASECHTOT_LIST_MISHNA";
    private RecyclerView mZeraimRecycler;
    private MishnaAdapter mMishnaZeraimAdapter;
    private MishnaAdapter mMishnaMoedAdapter;
    private MishnaAdapter mMishnaNoshimAdapter;
    private MishnaAdapter mMishnaNezikinAdapter;
    private MishnaAdapter mMishnaKodshimAdapter;
    private MishnaAdapter mMishnaTaharotAdapter;
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
    private OnMishnaMainFragmentListener mListener;
    private MasechetList mMasechetotList;



    public MishnaMainFragment() {

    }



    public static MishnaMainFragment newInstance(MasechetList mMasechtotList) {
        MishnaMainFragment fragment = new MishnaMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(MASECHTOT_LIST, mMasechtotList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mMasechetotList = (MasechetList) getArguments().getSerializable(MASECHTOT_LIST);
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
        return inflater.inflate(R.layout.fragment_mishna_main, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setZeraimRecyclerView(mMasechetotList.getSeder().get(0));
        setMoedRecyclerView(mMasechetotList.getSeder().get(1));
        setNoshimRecyclerView(mMasechetotList.getSeder().get(2));
        setNezikinRecyclerView(mMasechetotList.getSeder().get(3));
        setKodshimRecyclerView(mMasechetotList.getSeder().get(4));
        setTaharotRecyclerView(mMasechetotList.getSeder().get(5));

        initViews();
        setSerachView();
        setArrowViews();

    }




    /**
     * init views
     */
    private void initViews() {

        mLinearZeraim = Objects.requireNonNull(getView()).findViewById(R.id.MMF_linearZeraim_LL);
        mLinearMoed = Objects.requireNonNull(getView()).findViewById(R.id.MMF_linearMoed_LL);
        mLinearNoshim = Objects.requireNonNull(getView()).findViewById(R.id.MMF_linearNoshim_LL);
        mLinearNezikin = Objects.requireNonNull(getView()).findViewById(R.id.MMF_linearNezikin_LL);
        mLinearKodshim = Objects.requireNonNull(getView()).findViewById(R.id.MMF_linearKodshim_LL);
        mLinearTaharot = Objects.requireNonNull(getView()).findViewById(R.id.MMF_linearTaharot_LL);


    }


    /**
     * set mishna search view
     */
    private void setSerachView() {

        SearchView searchView = Objects.requireNonNull(getView()).findViewById(R.id.MMF_search_SV);

        final TextView title = Objects.requireNonNull(getView()).findViewById(R.id.MMF_title_TV);

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

                if (mMishnaZeraimAdapter != null) {

                    mMishnaZeraimAdapter.getFilter().filter(s);
                }

                if (mMishnaMoedAdapter != null) {

                    mMishnaMoedAdapter.getFilter().filter(s);
                }

                if (mMishnaNoshimAdapter != null) {

                    mMishnaNoshimAdapter.getFilter().filter(s);
                }

                if (mMishnaNezikinAdapter != null) {

                    mMishnaNezikinAdapter.getFilter().filter(s);
                }

                if (mMishnaKodshimAdapter != null) {

                    mMishnaKodshimAdapter.getFilter().filter(s);
                }

                if (mMishnaTaharotAdapter != null) {

                    mMishnaTaharotAdapter.getFilter().filter(s);
                }


                return true;
            }
        });


    }




    /**
     * read the file and convert the text to string
     *
     * @param is file location
     * @return String
     * @throws Exception e
     */
    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }






    /**
     * set mishna arrows
     */
    private void setArrowViews() {

        ImageView mZeraimArrow = Objects.requireNonNull(getView()).findViewById(R.id.MMF_zeraim_arrow_IV);
        ImageView mMoedArrow = getView().findViewById(R.id.MMF_moed_arrow_IV);
        ImageView mNoshimArrow = getView().findViewById(R.id.MMF_noshim_arrow_IV);
        ImageView mNezikinArrow = getView().findViewById(R.id.MMF_nezikin_arrow_IV);
        ImageView mKodshimArrow = getView().findViewById(R.id.MMF_kodshim_arrow_IV);
        ImageView mTaharotArrow = getView().findViewById(R.id.MMF_taharot_arrow_IV);

        mZeraimArrow.setOnClickListener(this);
        mMoedArrow.setOnClickListener(this);
        mNoshimArrow.setOnClickListener(this);
        mNezikinArrow.setOnClickListener(this);
        mKodshimArrow.setOnClickListener(this);
        mTaharotArrow.setOnClickListener(this);

    }



    private void setKodshimRecyclerView(SederItem sederItem) {

        mKodshimRecycler = Objects.requireNonNull(getView()).findViewById(R.id.MMF_kodshim_recycler_RV);
        mKodshimRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMishnaKodshimAdapter = new MishnaAdapter(getContext(),sederItem, this, mLinearKodshim);
        mKodshimRecycler.setAdapter(mMishnaKodshimAdapter);
    }

    private void setTaharotRecyclerView(SederItem sederItem) {

        mTaharotRecycler = Objects.requireNonNull(getView()).findViewById(R.id.MMF_taharot_recycler_RV);
        mTaharotRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMishnaTaharotAdapter = new MishnaAdapter(getContext(),sederItem, this, mLinearTaharot);
        mTaharotRecycler.setAdapter(mMishnaTaharotAdapter);
    }

    private void setNezikinRecyclerView(SederItem sederItem) {

        mNezikinRecycler = Objects.requireNonNull(getView()).findViewById(R.id.MMF_nezikin_recycler_RV);
        mNezikinRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMishnaNezikinAdapter = new MishnaAdapter(getContext(),sederItem, this, mLinearNezikin);
        mNezikinRecycler.setAdapter(mMishnaNezikinAdapter);
    }

    private void setNoshimRecyclerView(SederItem sederItem) {


        mNoshimRecycler = Objects.requireNonNull(getView()).findViewById(R.id.MMF_noshim_recycler_RV);
        mNoshimRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMishnaNoshimAdapter = new MishnaAdapter(getContext(),sederItem, this, mLinearNoshim);
        mNoshimRecycler.setAdapter(mMishnaNoshimAdapter);
    }



    private void setMoedRecyclerView(SederItem sederItem) {

        mMoedRecycler = Objects.requireNonNull(getView()).findViewById(R.id.MMF_moed_recycler_RV);
        mMoedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMishnaMoedAdapter = new MishnaAdapter(getContext(), sederItem, this, mLinearMoed);
        mMoedRecycler.setAdapter(mMishnaMoedAdapter);

    }


    private void setZeraimRecyclerView(SederItem sederItem) {

        mZeraimRecycler = Objects.requireNonNull(getView()).findViewById(R.id.MMF_zeraim_recycler_RV);
        mZeraimRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMishnaZeraimAdapter = new MishnaAdapter(getContext(), sederItem, this, mLinearZeraim);
        mZeraimRecycler.setAdapter(mMishnaZeraimAdapter);

    }





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMishnaMainFragmentListener) {
            mListener = (OnMishnaMainFragmentListener) context;

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

            case R.id.MMF_zeraim_arrow_IV:

                manageArrowToCloseAndOpenView(mZeraimRecycler, (ImageView) view);
                break;

            case R.id.MMF_moed_arrow_IV:

                manageArrowToCloseAndOpenView(mMoedRecycler, (ImageView) view);
                break;

            case R.id.MMF_noshim_arrow_IV:

                manageArrowToCloseAndOpenView(mNoshimRecycler, (ImageView) view);
                break;

            case R.id.MMF_nezikin_arrow_IV:

                manageArrowToCloseAndOpenView(mNezikinRecycler, (ImageView) view);
                break;

            case R.id.MMF_kodshim_arrow_IV:

                manageArrowToCloseAndOpenView(mKodshimRecycler, (ImageView) view);
                break;

            case R.id.MMF_taharot_arrow_IV:

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

        if (recyclerToManage != null) {

            if (recyclerToManage.getVisibility() == View.VISIBLE) {

                recyclerToManage.setVisibility(View.GONE);
                imageView.setRotation(180);

            } else {

                recyclerToManage.setVisibility(View.VISIBLE);
                imageView.setRotation(0);

            }

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
    public void onMasechetClicked(MasechetItem masechtotItem, int sederOrder) {

        mListener.onMasechetClicked(masechtotItem, sederOrder);

    }



    public interface OnMishnaMainFragmentListener {

        void onMasechetClicked(MasechetItem masechtotItem, int sederOrder);

        void hideToolBar();
    }
}
