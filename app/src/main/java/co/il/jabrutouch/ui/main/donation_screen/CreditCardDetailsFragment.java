package co.il.jabrutouch.ui.main.donation_screen;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.il.jabrutouch.R;


public class CreditCardDetailsFragment extends Fragment {


    public static final String TAG = CreditCardDetailsFragment.class.getSimpleName();
    private OnCreditCardDetailsFragmentListener mListener;
    private LinearLayout mAnonimousLL;
    private CheckBox mAnonimousCB;
    private TextView mAnonimousTV;
    private Button mSendPaymentBtnBTN;
    private EditText mCardNameET;
    private EditText mCardNumberET;
    private EditText mCardExpFirstET;
    private EditText mCardExpLastET;
    private EditText mCardCvvET;
    private RelativeLayout mCardNameRL;
    private RelativeLayout mCardNumberRL;
    private RelativeLayout mCardExpFirstRL;
    private RelativeLayout mCardExpLastRL;
    private RelativeLayout mCardCvvRL;
    private TextView mCardNameTV;
    private TextView mCardNumberTV;
    private TextView mCardExpFirstAndLastTV;
    private TextView mCardCvvTV;
    private ImageView mCardCompanyIV;
    private boolean mMoreThenSevenCreditDigits;
    private RelativeLayout mSendPaymentBtnRL;

    public CreditCardDetailsFragment() {
        // Required empty public constructor
    }

    public static CreditCardDetailsFragment newInstance() {
        CreditCardDetailsFragment fragment = new CreditCardDetailsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credir_card_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
        checkCreditCardCompany();
        initAllEditTextChangeListener();
    }


    private void initViews() {


        mAnonimousLL = Objects.requireNonNull(getView()).findViewById(R.id.DEDF_anunimous_LL);
        mAnonimousCB = getView().findViewById(R.id.DEDF_anonimous_CB);
        mAnonimousTV = getView().findViewById(R.id.DEDF_anonimous_TV);
        mSendPaymentBtnBTN = getView().findViewById(R.id.CCDF_send_payment_btn_BTN);
        mSendPaymentBtnRL = getView().findViewById(R.id.CCDF_send_payment_btn_RL);

        mCardNameET = getView().findViewById(R.id.CCDF_card_name_edit_text_ET);
        mCardNumberET = getView().findViewById(R.id.CCDF_card_number_edit_text_ET);
        mCardExpFirstET = getView().findViewById(R.id.CCDF_card_expiry_first_edit_text_ET);
        mCardExpLastET = getView().findViewById(R.id.CCDF_card_expiry_last_edit_text_ET);
        mCardCvvET = getView().findViewById(R.id.CCDF_card_cvv_edit_text_ET);

        mCardNameRL = getView().findViewById(R.id.CCDF_card_name_RL);
        mCardNumberRL = getView().findViewById(R.id.CCDF_card_number_RL);
        mCardExpFirstRL = getView().findViewById(R.id.CCDF_card_expiry_first_RL);
        mCardExpLastRL = getView().findViewById(R.id.CCDF_card_expiry_last_RL);
        mCardCvvRL = getView().findViewById(R.id.CCDF_card_cvv_RL);

        mCardNameTV = getView().findViewById(R.id.CCDF_card_name_TV);
        mCardNumberTV = getView().findViewById(R.id.CCDF_card_number_TV);
        mCardExpFirstAndLastTV = getView().findViewById(R.id.CCDF_card_expiry_first_and_last_TV);
        mCardCvvTV = getView().findViewById(R.id.CCDF_card_cvv_TV);

        mCardCompanyIV = getView().findViewById(R.id.FCCD_credit_compeny_IV);

    }


    private void initListeners() {


        mSendPaymentBtnBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText[] allFields = new EditText[]{mCardNameET, mCardNumberET, mCardExpFirstET, mCardExpLastET, mCardCvvET};
                RelativeLayout[] allFieldsRl = new RelativeLayout[]{mCardNameRL, mCardNumberRL, mCardExpFirstRL, mCardExpLastRL, mCardCvvRL};
                TextView[] allFieldsTv = new TextView[]{mCardNameTV, mCardNumberTV, mCardExpFirstAndLastTV, mCardExpFirstAndLastTV, mCardCvvTV};

                boolean allFilled = true;


                for (int i = 0; i < allFields.length; i++) {


                    if (!checkIfFieldsAreFilled(allFields[i], allFieldsRl[i], allFieldsTv[i], i)) {

                        allFilled = false;
                    }

                }


                if (allFilled && mMoreThenSevenCreditDigits) {

                    mListener.onSendPaymentClicked();

                }


            }
        });


        mAnonimousCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAnonimousCB.isChecked()) {

                    mAnonimousCB.setButtonDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ellipse_icon));
                    mAnonimousTV.setTextColor(getResources().getColor(R.color.check_box_color));
                } else {
                    mAnonimousCB.setButtonDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.check_box_marked));
                    mAnonimousTV.setTextColor(getResources().getColor(R.color.orange_btn));

                }

            }
        });

    }







    private void checkCreditCardCompany() {


        final ArrayList<String> listOfPattern = new ArrayList<String>();

        String ptVisa = "^4[0-9]$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5]$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47]$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68])$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5)$";
        listOfPattern.add(ptDiscover);
//        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
//        listOfPattern.add(ptJcb);


        final Integer[] imageArray = {R.drawable.visa_icon, R.drawable.mastercard_icon, R.drawable.amex_icon, R.drawable.diners_club_icon, R.drawable.discover_icon};


        mCardNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() <= 1) {

                    mCardCompanyIV.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.flag_transparent));

                } else {

                    mMoreThenSevenCreditDigits = true;

                    for (int i = 0; i < listOfPattern.size(); i++) {

                        if (editable.toString().matches(listOfPattern.get(i))) {

                            mCardCompanyIV.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), imageArray[i]));

                        }
                    }
                }

            }
        });


    }







    private void initAllEditTextChangeListener() {

        EditText[] allFields = new EditText[]{mCardNameET, mCardNumberET, mCardExpFirstET, mCardExpLastET, mCardCvvET};
        RelativeLayout[] allFieldsRl = new RelativeLayout[]{mCardNameRL, mCardNumberRL, mCardExpFirstRL, mCardExpLastRL, mCardCvvRL};
        TextView[] allFieldsTv = new TextView[]{mCardNameTV, mCardNumberTV, mCardExpFirstAndLastTV, mCardExpFirstAndLastTV, mCardCvvTV};

        changeEditTextBackgroundBach(allFields, allFieldsRl, allFieldsTv);


    }








    private void changeEditTextBackgroundBach(final EditText[] editTexts, final RelativeLayout[] relativeLayouts, final TextView[] textViews) {


        for (int i = 0; i < editTexts.length; i++) {


            final int finalI = i;
            final int finalI1 = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.length() > 0) {

                        relativeLayouts[finalI].setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_edittext_top_shadow));
                        textViews[finalI1].setTextColor(getResources().getColor(R.color.bottom_nevi_blue));


                        boolean allFilled = true;

                        for (int i = 0; i < editTexts.length; i++) {


                            if (!checkIfFieldsAreFilledForSendBtn(editTexts[i], relativeLayouts[i], textViews[i])) {

                                allFilled = false;
                            }

                        }


                        if (allFilled){

                            mSendPaymentBtnRL.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.blue_button_rounded));

                        }

                        else {

                            mSendPaymentBtnRL.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.send_shadow));

                        }
                    }


                }
            });

        }

    }






    /**
     * check if all fields are filled and if not change background color
     *
     * @return boolean allFilled
     */
    private boolean checkIfFieldsAreFilled(EditText editText, RelativeLayout relativeLayout, TextView textView, int i) {

        int atLeastInLength = 0;

        switch (i){

            case 0:
            case 2:
            case 3:

                atLeastInLength = 2;
                break;

            case 1:

                atLeastInLength = 7;
                break;

            case 4:

                atLeastInLength = 3;
        }


        if (editText.getText().toString().length() < atLeastInLength) {
            relativeLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_edittext_top_shadow_orange));
            textView.setTextColor(getResources().getColor(R.color.orange_btn));
            return false;
        }


        return true;


    }








    private boolean checkIfFieldsAreFilledForSendBtn(EditText editText, RelativeLayout relativeLayout, TextView textView) {


        if (editText.getText().toString().length() <= 0) {
            return false;
        }


        return true;



    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreditCardDetailsFragmentListener) {
            mListener = (OnCreditCardDetailsFragmentListener) context;
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








    public interface OnCreditCardDetailsFragmentListener {

        void onSendPaymentClicked();
    }
}
