package co.il.jabrutouch.ui.main.donation_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import co.il.jabrutouch.R;
import co.il.model.model.DedicationItem;
import co.il.model.model.User;

import static co.il.jabrutouch.ui.main.audio_screen.AudioActivity.hideKeyboard;


public class OthersDedicationViewPagerFragment extends Fragment implements CustomEditText4.CustomEditText4Listener {

    private static final String USER = "USER";
    private static final String DEDICATION_ITEM = "DEDICATION_ITEM";

    private OnOthersDedicationViewPagerFragmentListener mListener;
    private TextView mOthersPagerTextTV;
    private User mUser;
    private AppCompatTextView mUserNameACTV;
    private AppCompatTextView mUserCounteryACTV;
    private RelativeLayout mLinearEditNameRL;
    private RelativeLayout mLinearNameLL;
    private ImageView mEditEditTextIV;
    private RelativeLayout mEditNameIconRL;
    private EditText mEditNameET;
    private DedicationItem mDedicationItem;
    private CustomEditText4 mDedicationEditNameET;
    private TextView mUserCountryTV;
    private LinearLayout mAllViewLL;


    public OthersDedicationViewPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userFromJson
     * @return A new instance of fragment OthersDedicationViewPagerFragment.
     */
    public static OthersDedicationViewPagerFragment newInstance(DedicationItem dedicationItem, User userFromJson) {
        OthersDedicationViewPagerFragment fragment = new OthersDedicationViewPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(DEDICATION_ITEM, dedicationItem);
        args.putSerializable(USER, userFromJson);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDedicationItem = (DedicationItem) getArguments().getSerializable(DEDICATION_ITEM);
            mUser = (User) getArguments().getSerializable(USER);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_others_dedication_view_pager, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initViews();
        setUserDetailes();
        initListeners();
        initTextWatcher();
        setOnKeyBackListener();
        setImageSizeInEditText();
    }


    private void initViews() {

        mOthersPagerTextTV = Objects.requireNonNull(getView()).findViewById(R.id.FODVP_text_TV);
        mUserNameACTV = getView().findViewById(R.id.FODVP_user_nae_ACTV);
//        mUserCounteryACTV = getView().findViewById(R.id.FODVP_user_country_ACTV);
        mLinearEditNameRL = Objects.requireNonNull(getView()).findViewById(R.id.FODVP_linear_edit_name_RL);
        mLinearNameLL = Objects.requireNonNull(getView()).findViewById(R.id.FODVP_linear_name_LL);
        mEditEditTextIV = Objects.requireNonNull(getView()).findViewById(R.id.FODVP_edit_edittext_IV);
        mEditNameIconRL = Objects.requireNonNull(getView()).findViewById(R.id.OFDVP_v_name_RL);
        mEditNameET = Objects.requireNonNull(getView()).findViewById(R.id.FODVP_edittext_name_ET);
        mDedicationEditNameET = Objects.requireNonNull(getView()).findViewById(R.id.FODVP_edittext_ET);
        mUserCountryTV = Objects.requireNonNull(getView()).findViewById(R.id.FODVP_User_location_CCP);
        mAllViewLL = Objects.requireNonNull(getView()).findViewById(R.id.FODVP_all_view);


    }


    @SuppressLint("SetTextI18n")
    private void setUserDetailes() {

        mOthersPagerTextTV.setText(mDedicationItem.getTemplate());

        mUserNameACTV.setText(mUser.getFirstName() + " " + mUser.getLastName());
//        mUserCounteryACTV.setText(mUser.getCountry());
        mUserCountryTV.setText(mUser.getCountry());

    }


    private void setOnKeyBackListener() {

        mDedicationEditNameET.addListener(this);

    }


    private void setImageSizeInEditText() {

//        mAllViewLL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//
//                ViewGroup.LayoutParams params =  mEditNameIconRL.getLayoutParams();
//                params.width = mLinearEditNameRL.getHeight();
//                params.height =  mLinearEditNameRL.getHeight();
//
//                mEditNameIconRL.setLayoutParams(params);
//
//                mAllViewLL.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//            }
//        });


    }


    private void initListeners() {

        mEditEditTextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLinearNameLL.setVisibility(View.GONE);
                mLinearEditNameRL.setVisibility(View.VISIBLE);

            }
        });


        mEditNameIconRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLinearNameLL.setVisibility(View.VISIBLE);
                mLinearEditNameRL.setVisibility(View.GONE);

                mUserNameACTV.setText(mEditNameET.getText().toString());
                mUser.setFirstName(mEditNameET.getText().toString());
                mUser.setLastName("");


                mListener.saveEditNameForAllPager(mEditNameET.getText().toString());

            }
        });


        mDedicationEditNameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDedicationEditNameET.setFocusableInTouchMode(true);
                mDedicationEditNameET.requestFocus();
                openKeyboard(mDedicationEditNameET);
            }
        });

    }


    /**
     * open the Keyboard When Fragment Start
     */
    private void openKeyboard(EditText editText) {

        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().INPUT_METHOD_SERVICE);
        editText.requestFocus();
        Objects.requireNonNull(inputMethodManager).showSoftInput(editText, 0);


    }


    private void initTextWatcher() {


        mDedicationEditNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                mListener.onDedicationNameChanges(editable.toString());
            }
        });

        mDedicationEditNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    mListener.hideTopView();
                }
            }
        });

        mDedicationEditNameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onKeyPrime();
                }
                return false;
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOthersDedicationViewPagerFragmentListener) {
            mListener = (OnOthersDedicationViewPagerFragmentListener) context;
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


    public void saveEditNameForAllPager(String userName) {

        mUserNameACTV.setText(userName);

    }

    @Override
    public void onKeyPrime() {

        hideKeyboard(Objects.requireNonNull(getActivity()));
        mListener.showTopView();

//        mDedicationEditNameET.clearFocus();
        mDedicationEditNameET.setFocusableInTouchMode(false);
        mDedicationEditNameET.setFocusable(false);
//        mDedicationEditNameET.setFocusableInTouchMode(true);
        mDedicationEditNameET.setFocusable(true);
    }


    public interface OnOthersDedicationViewPagerFragmentListener {

        void saveEditNameForAllPager(String s);

        void onDedicationNameChanges(String dedicationName);

        void hideTopView();

        void showTopView();
    }
}
