package com.example.macrocounterremaster.adapters

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.activities.MainActivity
import com.example.macrocounterremaster.helpers.ProgressDialogHelper
import com.example.macrocounterremaster.models.NoteModel
import com.example.macrocounterremaster.room.DatabaseInstance
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_register.*
import kotlinx.android.synthetic.main.notes_recycler_card.view.*
import java.lang.ref.WeakReference

class NotesRecyclerAdapter(private val items: MutableList<NoteModel>, private val activity: Activity) : RecyclerView.Adapter<NotesRecyclerAdapter.MyViewHolder>() {
    private var list: MutableList<NoteModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.notes_recycler_card, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.month.text = list[position].getMonth()
        holder.day.text = list[position].getDay()
        holder.msg.text = list[position].getDescription()

        holder.close.setOnClickListener { removeItem(position) }
    }

    private fun removeItem(position: Int){
        RemoveRoomItemAsyncTask(list, position, activity, this).execute()
    }

    class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val month: TextView = view.note_month
        val day: TextView = view.note_day
        val msg: TextView = view.note_content
        val close: ImageView = view.cross
    }

    fun getList(): MutableList<NoteModel>{
        return this.list
    }

    class RemoveRoomItemAsyncTask(private val list: MutableList<NoteModel>, private val position: Int, activity: Activity, context: NotesRecyclerAdapter): AsyncTask<Void, Void, Boolean>() {
        private var weakReference: WeakReference<Activity> = WeakReference(activity)
        private var weakReferenceNotes: WeakReference<NotesRecyclerAdapter> = WeakReference(context)

        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()

            val activity = weakReference.get()!!

            if(!activity.isFinishing){
                progressDialog = ProgressDialogHelper.getProgressDialog(activity, R.string.deleting_item)
                progressDialog!!.show()
            }
        }
        override fun doInBackground(vararg p0: Void?): Boolean {
            val activity = weakReference.get()!!

            if(!activity.isFinishing) {
                val databaseInstance: DatabaseInstance = DatabaseInstance.getInstance(activity)
                databaseInstance.recordDao().delete(list[position])
                return true
            }

            return false
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)

            val activity = weakReference.get()!!

            if(!activity.isFinishing){
                progressDialog!!.cancel()

                if(result){
                    list.removeAt(position)
                    weakReferenceNotes.get()!!.notifyDataSetChanged()
                }else{
                    Snackbar.make(activity.nsv_main, R.string.deletion_failed, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}