package com.example.futurefarm

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val SPLASH_TIME_OUT:Long = 2000 // 1 sec
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        p_bar.max = 10
        val currentProgress = 10
        ObjectAnimator.ofInt(p_bar,"progress",currentProgress)
            .setDuration(1500)
            .start()


        Handler().postDelayed({
                startActivity(Intent(this,Login::class.java))
                finish()

        }, SPLASH_TIME_OUT)



    }
}