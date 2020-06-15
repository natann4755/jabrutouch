package co.il.jabrutouch.ui.sign_up.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;


@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText {

    private CustomEditTextListener mListener;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addListener(CustomEditTextListener customEditTextListener){
        mListener = customEditTextListener;
    }
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mListener.onKeyPrime();
        }
        return false;
    }
    public interface CustomEditTextListener{
        void onKeyPrime();
    }
}
