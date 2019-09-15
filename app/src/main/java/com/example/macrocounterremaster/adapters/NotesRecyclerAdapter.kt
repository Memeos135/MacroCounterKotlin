package com.example.macrocounterremaster.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.models.NoteModel
import com.example.macrocounterremaster.room.DatabaseInstance
import kotlinx.android.synthetic.main.notes_recycler_card.view.*

class NotesRecyclerAdapter(private val items: MutableList<NoteModel>, private val context: Context) : RecyclerView.Adapter<NotesRecyclerAdapter.MyViewHolder>() {
    private var list: MutableList<NoteModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_recycler_card, parent, false))
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
        val databaseInstance: DatabaseInstance = DatabaseInstance.getInstance(context)
        databaseInstance.recordDao().delete(list[position])

        list.removeAt(position)
        notifyDataSetChanged()
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
}