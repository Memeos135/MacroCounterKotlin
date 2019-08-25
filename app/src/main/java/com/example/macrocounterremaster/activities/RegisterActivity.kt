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
import com.example.macrocounterremaster.helpers.SaveHelper
import com.example.macrocounterremaster.models.FullValues
import com.example.macrocounterremaster.models.StageOneValues
import com.example.macrocounterremaster.utils.Constants
import com.example.macrocounterremaster.webServices.ServicePost
import com.example.macrocounterremaster.webServices.requests.RegisterRequestModel
import com.example.macrocounterremaster.webServices.responses.RegisterResponseModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*
import kotlinx.android.synthetic.main.nav_header_main.*
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
            R.id.logout -> {
                nav_view.menu.clear()
                nav_view.inflateMenu(R.menu.activity_main_drawer)

                user_name.text = getString(R.string.nav_header_title)
                user_email.text = getString(R.string.nav_header_subtitle)

                SaveHelper.removeAutoLogin(this)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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

        override fun onPostExecute(result: RegisterResponseModel) {
            super.onPostExecute(result)
            val registerActivity: RegisterActivity = weakReference.get()!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!registerActivity.isDestroyed) {
                    progressDialog!!.cancel()
                }
            }

            if(result.getId().isNotEmpty()){
                // registration is successful > save user credentials for auto login
                SaveHelper.saveTokenAndCredentialsRegister(result.getId(), fullValues.email, fullValues.password, fullValues.name, registerActivity)

                val intent = Intent()
                intent.putExtra(Constants.NAME, fullValues.name)
                intent.putExtra(Constants.EMAIL, fullValues.email)

                registerActivity.setResult(Constants.REGISTER_SUCCESS_CODE, intent)
                registerActivity.finish()
            }else {
                // registration failed
                Snackbar.make(registerActivity.nsv, result.getCode(), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
