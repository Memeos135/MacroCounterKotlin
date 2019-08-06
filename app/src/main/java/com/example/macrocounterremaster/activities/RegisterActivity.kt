package com.example.macrocounterremaster.activities

import android.content.Intent
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
import com.google.android.material.navigation.NavigationView

class RegisterActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, LoginFragmentOne.NextStage, LoginFragmentTwo.PreviousStage {

    // interface from LoginFragmentTwo
    override fun goBack() {
        fragmentController(fragmentOne = true, fragmentTwo = false)
    }

    // interface from LoginFragmentOne
    override fun proceed() {
        fragmentController(fragmentOne = false, fragmentTwo = true)
    }

    private fun fragmentController(fragmentOne: Boolean, fragmentTwo: Boolean){
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if(fragmentOne) {
            fragmentTransaction.replace(R.id.ll_fragment_container, LoginFragmentOne(R.layout.fragment_stage_one))
        }
        if(fragmentTwo){
            fragmentTransaction.replace(R.id.ll_fragment_container, LoginFragmentTwo(R.layout.fragment_stage_two))
        }

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

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
                startActivity(Intent(this@RegisterActivity, RegisterActivity::class.java))
                finish()
            }
            R.id.nav_login -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
