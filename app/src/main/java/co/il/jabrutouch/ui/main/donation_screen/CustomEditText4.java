package co.il.jabrutouch.ui.main.donation_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;



@SuppressLint("AppCompatCustomView")
public class CustomEditText4 extends EditText {


    private CustomEditText4Listener mListener;

    public CustomEditText4(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addListener(CustomEditText4Listener customEditTextListener){
        mListener = customEditTextListener;
    }
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if (event.getAction()!=KeyEvent.ACTION_DOWN)
            return true;

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mListener.onKeyPrime();
        }
        return true;
    }
    public interface CustomEditText4Listener{
        void onKeyPrime();
    }
}
