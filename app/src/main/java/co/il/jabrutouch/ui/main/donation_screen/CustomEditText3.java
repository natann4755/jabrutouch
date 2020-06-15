package co.il.jabrutouch.ui.main.donation_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;



@SuppressLint("AppCompatCustomView")
public class CustomEditText3 extends EditText {


    private CustomEditText2Listener mListener;

    public CustomEditText3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addListener(CustomEditText2Listener customEditTextListener){
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
    public interface CustomEditText2Listener{
        void onKeyPrime();
    }
}
