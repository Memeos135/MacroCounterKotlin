package com.example.macrocounterremaster.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.example.macrocounterremaster.helpers.SaveHelper
import com.example.macrocounterremaster.utils.Constants
import com.example.macrocounterremaster.utils.ErrorMapCreator
import com.example.macrocounterremaster.webServices.ServicePost
import com.example.macrocounterremaster.webServices.requests.LoginRequestModel
import com.example.macrocounterremaster.webServices.responses.AuthenticationResponseModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.nav_header_main.*
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data != null) {
            // use data values
            if (data.getStringExtra(Constants.NAME) != null) {
                setResult(Constants.REGISTER_SUCCESS_CODE, data)
                finish()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                finish()
            }
            R.id.nav_register -> {
                startActivityForResult(Intent(this@LoginActivity, RegisterActivity::class.java), Constants.REGISTER_CODE)
            }
            R.id.nav_login -> {
                onBackPressed()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    class LoginAsyncTask(private val username: String, private val password: String, private val url: String, loginActivity: LoginActivity): AsyncTask<Void, Void, AuthenticationResponseModel>() {
        private var weakReference: WeakReference<LoginActivity> = WeakReference(loginActivity)
        private var progressDialog: ProgressDialog? = null

        // show dialog before execution
        override fun onPreExecute() {
            super.onPreExecute()
            val loginActivity: LoginActivity = weakReference.get()!!

            if(!loginActivity.isFinishing){
                progressDialog = ProgressDialogHelper.getProgressDialog(loginActivity, R.string.logging)
                progressDialog!!.show()
            }
        }

        // attempt to fetch a token
        override fun doInBackground(vararg p0: Void?): AuthenticationResponseModel {
            val loginActivity: LoginActivity = weakReference.get()!!

            if(!loginActivity.isFinishing){
                return ServicePost.doPostToken(LoginRequestModel(username, password, url), false, loginActivity)
            }
            return AuthenticationResponseModel(null, ErrorMapCreator.getHashMap(loginActivity)[Constants.ZERO].toString(), null, null, null, null, null, null, null)
        }

        // cancel dialog and check result
        override fun onPostExecute(result: AuthenticationResponseModel) {
            super.onPostExecute(result)
            val loginActivity: LoginActivity = weakReference.get()!!

            if(!loginActivity.isFinishing){
                progressDialog!!.cancel()
            }

            if(result.getId() != null){
                // save the new token for future use
                val id = result.getId()

                SaveHelper.saveTokenAndCredentialsLogin(id.toString(), username, password, loginActivity)
                SaveHelper.saveGoalValues(loginActivity, result.getProteinGoal()!!, result.getCarbsGoal()!!,
                    result.getFatsGoal()!!
                )

                // get NAME from preferences (saved when user registers)
                val name = PreferenceManager.getDefaultSharedPreferences(loginActivity).getString(Constants.NAME, "")

                val intent = Intent()
                intent.putExtra(Constants.NAME, name!!)
                intent.putExtra(Constants.EMAIL, result.getEmail())
                intent.putExtra(Constants.PROTEIN_GOAL, result.getProteinGoal())
                intent.putExtra(Constants.PROTEIN_PROGRESS, result.getProteinProgress())
                intent.putExtra(Constants.CARBS_GOAL, result.getCarbsGoal())
                intent.putExtra(Constants.CARBS_PROGRESS, result.getCarbsProgress())
                intent.putExtra(Constants.FATS_GOAL, result.getFatsGoal())
                intent.putExtra(Constants.FATS_PROGRESS, result.getFatsProgress())
                loginActivity.setResult(Constants.LOGIN_SUCCESS_CODE, intent)
                loginActivity.finish()
            }else{
                Snackbar.make(loginActivity.scrollView, result.getCode().toString(), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
