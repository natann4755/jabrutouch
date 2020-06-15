package co.il.jabrutouch.ui.main.donation_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

import co.il.jabrutouch.R;
import co.il.model.model.DonationData;
import co.il.model.model.Payment;

import static co.il.jabrutouch.ui.main.audio_screen.AudioActivity.hideKeyboard;


public class DonationPaymentFragment extends Fragment implements View.OnClickListener, CustomEditText3.CustomEditText2Listener {

    public static final String TAG = DonationPaymentFragment.class.getSimpleName();
    private static final String DONATION_DATA = "DONATION_DATA";
    private int DEFAULT_AMOUNT = 54;
    private int MAX_AMOUNT = 200;
    private OnDonationPaymentFragmentListener mListener;
    private SeekBar mSeekBar;
    private CustomEditText3 mMoneyFeildET;
    private Button mContinueBtnBTN;
    private TextView mKetarimNumbersTV;
    private Button mSubButtonBTN;
    private Button mSingleButtonBTN;
    private TextView mDonationValueTV;
    private TextView mEligebleTV;
    private TextView mCancelTV;
    private boolean higherThanMaxSeekBar;
    private boolean inEditTextMode;
    private boolean inSingleMode = false;
    private DonationData mDonationData;
    private Payment payment;
    private TextView mMountlyTextTV;
    private TextView mPaymentTextTV;
    private RelativeLayout mContinueBtnRL;


    public DonationPaymentFragment() {
        // Required empty public constructor
    }


    public static DonationPaymentFragment newInstance(DonationData mDonationData) {
        DonationPaymentFragment fragment = new DonationPaymentFragment();
        Bundle args = new Bundle();
        args.putSerializable(DONATION_DATA, mDonationData);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            mDonationData = (DonationData) getArguments().getSerializable(DONATION_DATA);


        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_donation_payment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initSeekBarListener();
        initEditTextListener();
        setDefaultDonationAmount();
        initListeners();
        setOnKeyBackListener();
        checkUserState();
        showVideoForFirstTime();
    }


    @Override
    public void onResume() {
        super.onResume();

        mListener.changeTitle();
    }

    private void initViews() {

        mSeekBar = Objects.requireNonNull(getView()).findViewById(R.id.DPF_seek_bar_SB);
        mMoneyFeildET = Objects.requireNonNull(getView()).findViewById(R.id.DPF_money_edit_text_ET);
        mContinueBtnBTN = Objects.requireNonNull(getView()).findViewById(R.id.DPF_continue_btn_BTN);
        mKetarimNumbersTV = Objects.requireNonNull(getView()).findViewById(R.id.DPF_ketarim_num_TV);
        mSubButtonBTN = Objects.requireNonNull(getView()).findViewById(R.id.DPF_sub_btn_BTN);
        mSingleButtonBTN = Objects.requireNonNull(getView()).findViewById(R.id.DPF_single_btn_BTN);
        mDonationValueTV = Objects.requireNonNull(getView()).findViewById(R.id.DPF_donation_value_TV);
        mEligebleTV = Objects.requireNonNull(getView()).findViewById(R.id.DPF_eligeble_TV);
        mCancelTV = Objects.requireNonNull(getView()).findViewById(R.id.DPF_cancel_TV);
        mMountlyTextTV = Objects.requireNonNull(getView()).findViewById(R.id.DPF_mountly_text_TV);
        mPaymentTextTV = Objects.requireNonNull(getView()).findViewById(R.id.DFP_payment_text_TV);
        mContinueBtnRL = Objects.requireNonNull(getView()).findViewById(R.id.DPF_continue_btn_RL);

    }


    private void initSeekBarListener() {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint({"SetTextI18n", "StringFormatInvalid"})
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (!inEditTextMode) {
                    int MIN_VALUE = 18;
                    if (seekBar.getProgress() < MIN_VALUE) {
                        seekBar.setProgress(MIN_VALUE);

                    } else {
                        MIN_VALUE = i;
                    }

//                    if (!higherThanMaxSeekBar) {

                        mMoneyFeildET.setText(String.valueOf(MIN_VALUE));
                        mContinueBtnBTN.setText(getResources().getString(R.string.dolar) + MIN_VALUE);

                        if (inSingleMode) {
                            mEligebleTV.setText(getResources().getString(R.string.eligeable_for_sin, MIN_VALUE / mDonationData.getCrowns().get(0).getDollarPerCrown()));
                            mKetarimNumbersTV.setText(String.valueOf(MIN_VALUE / mDonationData.getCrowns().get(0).getDollarPerCrown()));

                        } else {
                            mEligebleTV.setText(getResources().getString(R.string.eligeable_for_sub, MIN_VALUE / mDonationData.getCrowns().get(1).getDollarPerCrown()));
                            mKetarimNumbersTV.setText(String.valueOf(MIN_VALUE / mDonationData.getCrowns().get(1).getDollarPerCrown()));
                        }
//                    }
                } else {

                    if (!higherThanMaxSeekBar) {

                        mMoneyFeildET.setText(String.valueOf(i));
                        mContinueBtnBTN.setText(getResources().getString(R.string.dolar) + i);

                        if (inSingleMode) {
                            mEligebleTV.setText(getResources().getString(R.string.eligeable_for_sin, i / mDonationData.getCrowns().get(0).getDollarPerCrown()));
                            mKetarimNumbersTV.setText(String.valueOf(i / mDonationData.getCrowns().get(0).getDollarPerCrown()));

                        } else {
                            mEligebleTV.setText(getResources().getString(R.string.eligeable_for_sub, i / mDonationData.getCrowns().get(0).getDollarPerCrown()));
                            mKetarimNumbersTV.setText(String.valueOf(i / mDonationData.getCrowns().get(1).getDollarPerCrown()));
                        }
                    }


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                higherThanMaxSeekBar = false;
                mMoneyFeildET.setCursorVisible(false);
                inEditTextMode = false;

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


    }


    private void initEditTextListener() {


        mMoneyFeildET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMoneyFeildET.setCursorVisible(true);
                mMoneyFeildET.setFocusableInTouchMode(true);
                mMoneyFeildET.requestFocus();
                openKeyboard(mMoneyFeildET);
                inEditTextMode = true;

            }
        });


        mMoneyFeildET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mMoneyFeildET.setSelection(mMoneyFeildET.getText().length());

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equals("")) {

                    mSeekBar.setProgress(0);

                } else {

                    mContinueBtnBTN.setText(getResources().getString(R.string.dolar) + Integer.valueOf(editable.toString()));


                    if (inSingleMode) {
                        mEligebleTV.setText(getResources().getString(R.string.eligeable_for_sin, Integer.valueOf(editable.toString())  / mDonationData.getCrowns().get(0).getDollarPerCrown()));

                        mKetarimNumbersTV.setText(String.valueOf(Integer.valueOf(editable.toString()) / mDonationData.getCrowns().get(0).getDollarPerCrown()));

                    } else {
                        mEligebleTV.setText(getResources().getString(R.string.eligeable_for_sub, Integer.valueOf(editable.toString())  / mDonationData.getCrowns().get(1).getDollarPerCrown()));

                        mKetarimNumbersTV.setText(String.valueOf(Integer.valueOf(editable.toString()) / mDonationData.getCrowns().get(1).getDollarPerCrown()));
                    }

                    if (Integer.valueOf(editable.toString()) > MAX_AMOUNT) {

                        higherThanMaxSeekBar = true;
                    }
                    mSeekBar.setProgress(Integer.valueOf(editable.toString()));
                }
            }
        });


    }


    private void setDefaultDonationAmount() {

        mSeekBar.setMax(MAX_AMOUNT);
        mSeekBar.setProgress(DEFAULT_AMOUNT);
        mMoneyFeildET.setCursorVisible(false);

    }


    private void initListeners() {

        mSingleButtonBTN.setOnClickListener(this);
        mSubButtonBTN.setOnClickListener(this);
        mContinueBtnBTN.setOnClickListener(this);
        mContinueBtnRL.setOnClickListener(this);


    }


    private void setOnKeyBackListener() {

        mMoneyFeildET.addListener(this);

    }


    /**
     * open the Keyboard When Fragment Start
     */
    private void openKeyboard(EditText editText) {

        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().INPUT_METHOD_SERVICE);
        editText.requestFocus();
        Objects.requireNonNull(inputMethodManager).showSoftInput(editText, 0);


    }

    private void checkUserState() {

        if (payment != null) {

            if (payment.getPaymentType() == mDonationData.getCrowns().get(0).getId()) {

                openSingleMode();

            } else if (payment.getPaymentType() == mDonationData.getCrowns().get(1).getId()) {

                openSubscriptionMode();
            }

        }
    }


    private void showVideoForFirstTime() {


//        DonationVideoDialog donationVideoDialog = new DonationVideoDialog();
//        donationVideoDialog.showDialog(getContext(), this);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDonationPaymentFragmentListener) {
            mListener = (OnDonationPaymentFragmentListener) context;
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


            case R.id.DPF_single_btn_BTN:

                inSingleMode = true;
                openSingleMode();
                mMoneyFeildET.setCursorVisible(false);

                break;


            case R.id.DPF_sub_btn_BTN:

                inSingleMode = false;
                openSubscriptionMode();
                mMoneyFeildET.setCursorVisible(false);

                break;


            case R.id.DPF_continue_btn_RL:

                if (mMoneyFeildET.getText().length() > 0) {

                    payment = new Payment();
                    payment.setSum(Integer.parseInt(mMoneyFeildET.getText().toString()));

                    if (inSingleMode) {

                        payment.setPaymentType(mDonationData.getCrowns().get(0).getId());
                    } else {
                        payment.setPaymentType(mDonationData.getCrowns().get(1).getId());
                    }

                    mListener.onContinueBtnPressed(payment, mDonationData);
                }

                break;


        }


    }


    private void openSingleMode() {

        mSingleButtonBTN.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blue_button));
        mSingleButtonBTN.setTextColor(getResources().getColor(R.color.color_white));
        mSingleButtonBTN.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

        mSubButtonBTN.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.white_button));
        mSubButtonBTN.setTextColor(getResources().getColor(R.color.color_blue_gray));
        mSubButtonBTN.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));


        mDonationValueTV.setText("");
        mEligebleTV.setText(getResources().getString(R.string.eligeable_for_sin));
        mCancelTV.setText("");
        mMountlyTextTV.setVisibility(View.GONE);
        mPaymentTextTV.setText(getResources().getString(R.string.Pago_único));

        MAX_AMOUNT = 2000;
        DEFAULT_AMOUNT = 100;

        mSeekBar.setMax(MAX_AMOUNT);
        mSeekBar.setProgress(DEFAULT_AMOUNT);

        mMoneyFeildET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});


    }


    private void openSubscriptionMode() {

        mSubButtonBTN.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blue_button));
        mSubButtonBTN.setTextColor(getResources().getColor(R.color.color_white));
        mSubButtonBTN.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

        mSingleButtonBTN.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.white_button));
        mSingleButtonBTN.setTextColor(getResources().getColor(R.color.color_blue_gray));
        mSingleButtonBTN.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));


        mDonationValueTV.setText(getResources().getString(R.string.value));
        mEligebleTV.setText(getResources().getString(R.string.eligeable_for_sub));
        mCancelTV.setText(getResources().getString(R.string.cancel_subs));
        mMountlyTextTV.setVisibility(View.VISIBLE);
        mPaymentTextTV.setText(getResources().getString(R.string.paid_monthly));

        MAX_AMOUNT = 200;
        DEFAULT_AMOUNT = 54;

        mSeekBar.setMax(MAX_AMOUNT);
        mSeekBar.setProgress(DEFAULT_AMOUNT);

        mMoneyFeildET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

    }

    @Override
    public void onKeyPrime() {

        if (!inEditTextMode) {

            mListener.onBackPressed();

        } else {
            if (mMoneyFeildET.getText().toString().length() > 0) {

                if (mMoneyFeildET.getText().toString().substring(0, 1).equals("0")) {
                    mMoneyFeildET.setText("1");
                }

            }
            hideKeyboard(Objects.requireNonNull(getActivity()));
            inEditTextMode = false;
        }


    }

//    @Override
//    public void onOnBackClicked() {
//        mListener.onBackPressed();
//
//    }


    public interface OnDonationPaymentFragmentListener {

        void onContinueBtnPressed(Payment payment, DonationData mDonationData);

        void changeTitle();

        void onBackPressed();
    }
}
