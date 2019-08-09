package com.example.macrocounterremaster

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.example.macrocounterremaster.activities.LoginActivity
import com.example.macrocounterremaster.activities.RegisterActivity
import com.example.macrocounterremaster.adapters.NotesRecyclerAdapter
import com.example.macrocounterremaster.helpers.MonthHelper
import com.example.macrocounterremaster.helpers.NoteDialogHelper
import com.example.macrocounterremaster.models.NoteModel
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.goal_dialog_layout.*
import kotlinx.android.synthetic.main.notes_dialog_layout.*
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
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        setupTitleListeners()
        setupEmptyRecycler()
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
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            }
            R.id.nav_login -> {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupTitleListeners(){
        tv_protein_title.setOnClickListener { goalDisplay(tv_protein_title) }
        tv_carbs_title.setOnClickListener { goalDisplay(tv_carbs_title) }
        tv_fat_title.setOnClickListener { goalDisplay(tv_fat_title) }
        tv_cal_title.setOnClickListener { goalDisplay(tv_cal_title) }
    }

    private fun goalDisplay(view: TextView){
        val dialog = NoteDialogHelper.showInputDialog(this, R.layout.goal_dialog_layout)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create()
        }
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.category.text = view.tag.toString()
        dialog.show()
    }

    private fun showNoteInputDialog(){
        val dialog = NoteDialogHelper.showInputDialog(this, R.layout.notes_dialog_layout)
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
