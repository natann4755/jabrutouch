package co.il.jabrutouch.ui.main.audio_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;



@SuppressLint("AppCompatCustomView")
public class CustomEditText2 extends EditText {


    private CustomEditText2Listener mListener;

    public CustomEditText2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addListener(CustomEditText2Listener customEditTextListener){
        mListener = customEditTextListener;
    }
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mListener.onKeyPrime();
        }
        return true;
    }
    public interface CustomEditText2Listener{
        void onKeyPrime();
    }
}
