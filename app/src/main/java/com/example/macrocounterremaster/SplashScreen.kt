package com.example.macrocounterremaster

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import kotlinx.android.synthetic.main.splash_screen.*
import android.content.Intent
import android.preference.PreferenceManager
import com.example.macrocounterremaster.activities.LoginActivity
import com.example.macrocounterremaster.activities.MainActivity
import com.example.macrocounterremaster.utils.Constants


class SplashScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val slideAnimation = loadAnimation(this, android.R.anim.slide_in_left)
        slideAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                checkIfUserExists()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        progressBar.animation = slideAnimation
    }

    fun checkIfUserExists() {
        // check if token exists (user previously logged in)
        val token = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.TOKEN, "")
        val autoLogin = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.AUTO_LOGIN, "")
        if (token!!.isEmpty() && autoLogin!!.isNotEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java).putExtra(Constants.FLAG, Constants.FLAG))
        }
    }
}