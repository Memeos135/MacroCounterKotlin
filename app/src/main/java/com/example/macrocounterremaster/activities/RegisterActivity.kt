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
import com.example.macrocounterremaster.fragments.LoginFragmentOne
import com.example.macrocounterremaster.fragments.LoginFragmentTwo
import com.example.macrocounterremaster.helpers.ProgressDialogHelper
import com.example.macrocounterremaster.models.FullValues
import com.example.macrocounterremaster.models.StageOneValues
import com.example.macrocounterremaster.webServices.ServicePost
import com.example.macrocounterremaster.webServices.requests.RegisterRequestModel
import com.example.macrocounterremaster.webServices.responses.RegisterResponseModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_register.*
import java.lang.ref.WeakReference

class RegisterActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, LoginFragmentOne.NextStage, LoginFragmentTwo.StageTwoInterface {
    override fun register(fullValues: FullValues) {
        RegisterAsyncTask(fullValues, this).execute()
    }

    // interface from LoginFragmentTwo
    override fun goBack() {
        fragmentController(fragmentOne = true, fragmentTwo = false, values = null)
    }

    // interface from LoginFragmentOne
    override fun proceed(stageOneValues: StageOneValues) {
        fragmentController(fragmentOne = false, fragmentTwo = true, values = stageOneValues)
    }

    private fun fragmentController(fragmentOne: Boolean, fragmentTwo: Boolean, values: StageOneValues?){
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if(fragmentOne) {
            fragmentTransaction.replace(R.id.ll_fragment_container, LoginFragmentOne(R.layout.fragment_stage_one))
        }
        if(fragmentTwo){
            fragmentTransaction.replace(R.id.ll_fragment_container, LoginFragmentTwo(R.layout.fragment_stage_two, values!!
            ))
        }

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().add(R.id.ll_fragment_container, LoginFragmentOne(R.layout.fragment_stage_one)).commit()
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            finish()
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
                onBackPressed()
            }
            R.id.nav_login -> {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // if result code is empty >> success
    // if result code is NOT empty >> fail
    fun showMessage(result: RegisterResponseModel){
        if(result.getCode().isNotEmpty()){
            Snackbar.make(nsv, result.getCode(), Snackbar.LENGTH_SHORT).show()
        }else{
            // save token/username/password into sharedPreferences

            // finish() + update navigationView UI and MainActivity UI
            Snackbar.make(nsv, "it works!", Snackbar.LENGTH_SHORT).show()
        }
    }

    class RegisterAsyncTask(private val fullValues: FullValues, registerActivity: RegisterActivity): AsyncTask<Void, Void, RegisterResponseModel>() {
        private var weakReference: WeakReference<RegisterActivity> = WeakReference(registerActivity)
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            val registerActivity: RegisterActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!registerActivity.isDestroyed){
                    progressDialog = ProgressDialogHelper.getProgressDialog(registerActivity, R.string.logging)
                    progressDialog!!.show()
                }
            }
        }

        override fun doInBackground(vararg p0: Void?): RegisterResponseModel {
            val registerActivity: RegisterActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!registerActivity.isDestroyed){
                    return ServicePost.doPostRegister(RegisterRequestModel(fullValues, registerActivity.getString(R.string.signup_url)), registerActivity)
                }
            }
            return RegisterResponseModel()
        }

        override fun onPostExecute(result: RegisterResponseModel?) {
            super.onPostExecute(result)
            val registerActivity: RegisterActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!registerActivity.isDestroyed){
                    progressDialog!!.cancel()

                    registerActivity.showMessage(result!!)
                }
            }
        }
    }
}
