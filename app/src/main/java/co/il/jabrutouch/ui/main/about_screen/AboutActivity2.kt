package co.il.jabrutouch.ui.main.about_screen

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import co.il.jabrutouch.R

class AboutActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about2)


        initArrowBack();
        initScrollText();

    }




    /**
     * initialize arrow back button
     */
    private fun initArrowBack() {

        val arrowBack = findViewById<ImageView>(R.id.AA_back_arrow_IV)
        arrowBack.setOnClickListener {

            setImageClickedAnimation(arrowBack)
            finish()

        }


    }




    /**
     * initialize text to scroll down
     */
    private fun initScrollText() {
        val scrollText = findViewById<TextView>(R.id.AA_text_scroll)
        scrollText.movementMethod = ScrollingMovementMethod()
    }





    /**
     * set animation to the button
     * @param Icon ImageView
     */
    private fun setImageClickedAnimation(icon: ImageView) {

        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        icon.startAnimation(animation)
    }



}
