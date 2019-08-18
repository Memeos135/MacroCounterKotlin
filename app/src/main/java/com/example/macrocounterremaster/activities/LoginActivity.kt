package com.example.macrocounterremaster.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.helpers.EmailHelper
import com.example.macrocounterremaster.helpers.ProgressDialogHelper
import com.example.macrocounterremaster.webServices.ServicePost
import com.example.macrocounterremaster.webServices.requests.LoginRequestModel
import com.example.macrocounterremaster.webServices.responses.LoginResponseModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
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

    private fun processLogin() {
        if (et_email_address.text.toString().isNotEmpty() && et_fat.text.toString().isNotEmpty()) {
            if(EmailHelper.regexEmail(et_email_address.text.toString())) {
                LoginAsyncTask(
                    et_email_address.text.toString(),
                    et_fat.text.toString(),
                    getString(R.string.token_url),
                    this
                ).execute()
            }else{
                Snackbar.make(scrollView, getString(R.string.uncorrect_email), Snackbar.LENGTH_SHORT).show()
            }
        }else{
            Snackbar.make(scrollView, getString(R.string.fill_empty_fields), Snackbar.LENGTH_SHORT).show()
        }
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

    // if result code is empty >> success
    // if result code is NOT empty >> fail
    fun showMessage(result: LoginResponseModel){
        if(result.getCode().isNotEmpty()){
            Snackbar.make(scrollView, result.getCode(), Snackbar.LENGTH_SHORT).show()
        }else{
            // save token/username/password into sharedPreferences

            // finish() + update navigationView UI and MainActivity UI
            Snackbar.make(scrollView, "it works!", Snackbar.LENGTH_SHORT).show()
        }
    }

    class LoginAsyncTask(private val username: String, private val password: String, private val url: String, loginActivity: LoginActivity): AsyncTask<Void, Void, LoginResponseModel>() {
        private var weakReference: WeakReference<LoginActivity> = WeakReference(loginActivity)
        private var progressDialog: ProgressDialog? = null

        // show dialog before execution
        override fun onPreExecute() {
            super.onPreExecute()
            val loginActivity: LoginActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!loginActivity.isDestroyed){
                    progressDialog = ProgressDialogHelper.getProgressDialog(loginActivity, R.string.logging)
                    progressDialog!!.show()
                }
            }
        }

        // attempt to fetch a token
        override fun doInBackground(vararg p0: Void?): LoginResponseModel {
            val loginActivity: LoginActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!loginActivity.isDestroyed){
                    return ServicePost.doPostToken(LoginRequestModel(username, password, url), false, loginActivity)
                }
            }
            return LoginResponseModel()
        }

        // cancel dialog and check result
        override fun onPostExecute(result: LoginResponseModel?) {
            super.onPostExecute(result)
            val loginActivity: LoginActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!loginActivity.isDestroyed){
                    progressDialog!!.cancel()

                    loginActivity.showMessage(result!!)
                }
            }
        }
    }
}
