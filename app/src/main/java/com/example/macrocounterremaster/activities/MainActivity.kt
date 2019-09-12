package com.example.macrocounterremaster.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.adapters.NotesRecyclerAdapter
import com.example.macrocounterremaster.helpers.MonthHelper
import com.example.macrocounterremaster.helpers.NoteDialogHelper
import com.example.macrocounterremaster.helpers.ProgressDialogHelper
import com.example.macrocounterremaster.helpers.SaveHelper
import com.example.macrocounterremaster.models.Holder
import com.example.macrocounterremaster.models.NoteModel
import com.example.macrocounterremaster.utils.Constants
import com.example.macrocounterremaster.utils.ErrorMapCreator
import com.example.macrocounterremaster.webServices.ServicePost
import com.example.macrocounterremaster.webServices.requests.FetchDailyRequestModel
import com.example.macrocounterremaster.webServices.responses.FetchDailyProgressResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.goal_dialog_layout.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.notes_dialog_layout.*
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showNoteInputDialog()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        setupTitleListeners()
        setupEmptyRecycler()

        val autoLogin = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.AUTO_LOGIN, "")
        val email = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.EMAIL, "")
        // wait 500 milliseconds before setting navigation header views, because it will result in view = null (not attached yet)
        val r = Runnable {
            if (autoLogin!!.isNotEmpty()) {
                val name = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.NAME, "")
                updateDrawerUI(name, email)
            }
        }
        Handler().postDelayed(r, 500)

        // fetch daily progress - the app has just opened and signed in automatically
        if(savedInstanceState == null && autoLogin!!.isNotEmpty()){
            val password = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PASSWORD, "")

            FetchDailyProgressAsyncTask(email.toString(), password.toString(), this).execute()
        }
    }

    private class FetchDailyProgressAsyncTask(private val email: String, private val password: String, mainActivity: MainActivity): AsyncTask<Void, Void, FetchDailyProgressResponse>() {
        private var weakReference: WeakReference<MainActivity> = WeakReference(mainActivity)
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            val mainActivity: MainActivity = weakReference.get()!!

            if(!mainActivity.isFinishing){
                progressDialog = ProgressDialogHelper.getProgressDialog(mainActivity, R.string.fetching)
                progressDialog!!.show()
            }
        }

        override fun doInBackground(vararg p0: Void?): FetchDailyProgressResponse {
            val mainActivity: MainActivity = weakReference.get()!!

            if(!mainActivity.isFinishing){
                return ServicePost.doPostDaily(FetchDailyRequestModel(email, password, mainActivity), mainActivity)
            }
            return FetchDailyProgressResponse(null, null, null, ErrorMapCreator.getHashMap(mainActivity)[Constants.ZERO].toString())
        }

        override fun onPostExecute(fetchDailyProgressResponse: FetchDailyProgressResponse?) {
            super.onPostExecute(fetchDailyProgressResponse)
            val mainActivity: MainActivity = weakReference.get()!!

            if(!mainActivity.isFinishing){
                progressDialog!!.cancel()
            }

            if(fetchDailyProgressResponse!!.getError() == null){
                mainActivity.updateUI(fetchDailyProgressResponse.getProteinProgress()!!, fetchDailyProgressResponse.getCarbsProgress()!!, fetchDailyProgressResponse.getFatsProgress()!!)
            }else{
                Snackbar.make(mainActivity.nsv_main, fetchDailyProgressResponse.getError().toString(), Snackbar.LENGTH_SHORT).show()
            }

        }

    }

    private fun updateDrawerUI(name: String?, email: String?){
        if(name!!.isNotEmpty() && email!!.isNotEmpty()){
            user_name.text = name
            user_email.text = email
            nav_view.menu.clear()
            nav_view.inflateMenu(R.menu.activity_main_drawer_logged_in)
        }else{
            Snackbar.make(nsv_main, Constants.UPDATE_UI_FAILED, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(proteinProgress: String, carbsProgress: String, fatProgress: String){
        tv_protein_current.text = proteinProgress
        tv_carbs_current.text = carbsProgress
        tv_fat_current.text = fatProgress

        tv_protein_remain.text = computeRemaining(proteinProgress, Constants.PROTEIN)
        tv_carbs_remain.text = computeRemaining(carbsProgress, Constants.CARBS)
        tv_fat_remain.text = computeRemaining(fatProgress, Constants.FATS)

        computeCalories(proteinProgress, carbsProgress, fatProgress)
    }

    private fun computeCalories(proteinProgress: String, carbsProgress: String, fatProgress: String){
        val protein = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PROTEIN_GOAL, "")
        val carbs = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CARBS_GOAL, "")
        val fats = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.FATS_GOAL, "")

        val totalCalories = (Integer.parseInt(protein!!)*4) + (Integer.parseInt(carbs!!)*4) + (Integer.parseInt(fats!!)*9)
        val currentCalories = ((Integer.parseInt(proteinProgress)*4) + (Integer.parseInt(carbsProgress)*4) + (Integer.parseInt(fatProgress)*9))

        tv_cal_remain.text = (totalCalories - currentCalories).toString()
        tv_cal_current.text = currentCalories.toString()
    }

    private fun computeRemaining(value: String, item: String): String{
        val progress = Integer.parseInt(value)
        when (item) {
            Constants.PROTEIN -> {
                val protein = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PROTEIN_GOAL, "")

                return if(protein!!.isNotEmpty()){
                    (Integer.parseInt(protein) - progress).toString()
                }else{
                    "ERROR"
                }
            }
            Constants.CARBS -> {
                val carbs = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CARBS_GOAL, "")

                return if(carbs!!.isNotEmpty()){
                    (Integer.parseInt(carbs) - progress).toString()
                }else{
                    "ERROR"
                }
            }
            else -> {
                val fats = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.FATS_GOAL, "")

                return if(fats!!.isNotEmpty()){
                    (Integer.parseInt(fats) - progress).toString()
                }else{
                    "ERROR"
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // first check if data is not null
        if(data != null) {
            // use data values
            if (data.getStringExtra(Constants.NAME) != null && data.getStringExtra(Constants.EMAIL) != null) {
                updateDrawerUI(data.getStringExtra(Constants.NAME), data.getStringExtra(Constants.EMAIL))
                updateUI(
                    data.getStringExtra(Constants.PROTEIN_PROGRESS)!!,
                    data.getStringExtra(Constants.CARBS_PROGRESS)!!,
                    data.getStringExtra(Constants.FATS_PROGRESS)!!)
            }
        }
    }

    private fun setupEmptyRecycler(){
        val noteList: ArrayList<NoteModel> = ArrayList()
        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.adapter = NotesRecyclerAdapter(noteList, this)
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
                onBackPressed()
            }
            R.id.nav_register -> {
                startActivityForResult(Intent(this@MainActivity, RegisterActivity::class.java), Constants.REGISTER_CODE)
            }
            R.id.nav_login -> {
                startActivityForResult(Intent(this@MainActivity, LoginActivity::class.java), Constants.LOGIN_CODE)
            }
            R.id.logout -> {
                SaveHelper.removeAutoLogin(this)
                resetUI()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun resetUI(){
        nav_view.menu.clear()
        nav_view.inflateMenu(R.menu.activity_main_drawer)

        user_name.text = ""
        user_email.text = ""

        tv_protein_current.text = ""
        tv_carbs_current.text = ""
        tv_fat_current.text = ""

        tv_protein_remain.text = ""
        tv_carbs_remain.text = ""
        tv_fat_remain.text = ""

        tv_cal_current.text = ""
        tv_cal_remain.text = ""

        val adapter: NotesRecyclerAdapter = rv_notes.adapter as NotesRecyclerAdapter
        adapter.getList().clear()
        adapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(Constants.VALUES, Holder(

            tv_protein_current.text.toString(),
            tv_protein_remain.text.toString(),

            tv_carbs_current.text.toString(),
            tv_carbs_remain.text.toString(),

            tv_fat_current.text.toString(),
            tv_fat_remain.text.toString(),

            tv_cal_current.text.toString(),
            tv_cal_remain.text.toString(),
            (rv_notes.adapter as NotesRecyclerAdapter).getList())
        )

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val holder: Holder = savedInstanceState.getParcelable<Holder>(Constants.VALUES) as Holder

        tv_protein_current.text = holder.getProteinCurrent()
        tv_protein_remain.text = holder.getProteinRemain()

        tv_carbs_current.text = holder.getCarbsCurrent()
        tv_carbs_remain.text = holder.getCarbsRemain()

        tv_fat_current.text = holder.getFatsCurrent()
        tv_fat_remain.text = holder.getFatsRemain()

        tv_cal_current.text = holder.getCalCurrent()
        tv_cal_remain.text = holder.getCalRemain()

        rv_notes.adapter = NotesRecyclerAdapter(holder.getList(), this)
    }

    private fun setupTitleListeners(){
        tv_protein_title.setOnClickListener { goalDisplay(tv_protein_title) }
        tv_carbs_title.setOnClickListener { goalDisplay(tv_carbs_title) }
        tv_fat_title.setOnClickListener { goalDisplay(tv_fat_title) }
    }

    private fun goalDisplay(view: TextView){
        val dialog = NoteDialogHelper.showInputDialog(this,
            R.layout.goal_dialog_layout
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create()
        }
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.category.text = view.tag.toString()
        dialog.show()
    }

    private fun showNoteInputDialog(){
        val dialog = NoteDialogHelper.showInputDialog(this,
            R.layout.notes_dialog_layout
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create()
        }
        dialog.show()

        setupDialogTextListener(dialog)
    }

    private fun setupDialogTextListener(dialog: Dialog){
        // defined flag to make sure afterTextChanged only executes once
        var flag = false

        dialog.msg_text.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(flag){
                    addNoteToRecycler(dialog.msg_text.text.toString())
                    flag = false
                    dialog.cancel()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // EMPTY MUST IMPLEMENT
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().takeLast(1) == "\n"){
                    // get the note text (for code quality)
                    val msg: String = dialog.msg_text.text.toString()
                    // re-set the note text and remove the newline
                    dialog.msg_text.setText(msg.substring(0, msg.length - 1))
                    // re-set the cursor position to the last index
                    dialog.msg_text.setSelection(dialog.msg_text.text.length)
                    flag = true
                }
            }
        })
    }

    private fun addNoteToRecycler(msg: String){
        val adapter: NotesRecyclerAdapter = rv_notes.adapter as NotesRecyclerAdapter

        adapter.getList().add(NoteModel(MonthHelper.getMonth(Calendar.getInstance().get(Calendar.MONTH)), Calendar.DAY_OF_MONTH.toString(), msg))
        adapter.notifyDataSetChanged()
    }
}