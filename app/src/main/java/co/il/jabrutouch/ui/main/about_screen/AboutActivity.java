package co.il.jabrutouch.ui.main.about_screen;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import co.il.jabrutouch.R;



public class AboutActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initArrowBack();
        initScrollText();

    }




    /**
     * initialize arrow back button
     */
    private void initArrowBack() {

        final ImageView arrowBack = findViewById(R.id.AA_back_arrow_IV);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setImageClickedAnimation(arrowBack);
                finish();

            }
        });

    }




    /**
     * set animation to the button
     * @param Icon ImageView
     */
    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }




    /**
     * initialize text to scroll down
     */
    private void initScrollText() {

        TextView scrollText = findViewById(R.id.AA_text_scroll);
        scrollText.setMovementMethod(new ScrollingMovementMethod());



    }
}
