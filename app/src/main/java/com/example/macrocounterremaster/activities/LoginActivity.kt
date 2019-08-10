package com.example.macrocounterremaster.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.webServices.ServicePost
import com.example.macrocounterremaster.webServices.requests.LoginRequestModel
import com.example.macrocounterremaster.webServices.responses.LoginResponseModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_login.*
import java.lang.ref.WeakReference

class LoginActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        btnLogin.setOnClickListener { processLogin() }
    }

    private fun processLogin(){
//        Snackbar.make(scrollView, "It works", Snackbar.LENGTH_SHORT).show()
        LoginAsyncTask(et_email_address.text.toString(), et_password.text.toString(), getString(R.string.token_url), this).execute()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                finish()
            }
            R.id.nav_register -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }
            R.id.nav_login -> {
                onBackPressed()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    class LoginAsyncTask(private val username: String, private val password: String, private val url: String, loginActivity: LoginActivity): AsyncTask<Void, Void, LoginResponseModel>() {
        private var weakReference: WeakReference<LoginActivity> = WeakReference(loginActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            val loginActivity: LoginActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                // do progress dialog
            }
        }

        override fun doInBackground(vararg p0: Void?): LoginResponseModel {
            val loginActivity: LoginActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!loginActivity.isDestroyed){
                    return ServicePost.doPostToken(LoginRequestModel(username, password, url), false, loginActivity)
                }
            }
            return LoginResponseModel()
        }

        override fun onPostExecute(result: LoginResponseModel?) {
            super.onPostExecute(result)
            val loginActivity: LoginActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!loginActivity.isDestroyed){
                    Log.i("XXX", result!!.getCode())
                }
            }
        }
    }
}
