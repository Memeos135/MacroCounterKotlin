package com.example.macrocounterremaster.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.models.NoteModel
import kotlinx.android.synthetic.main.notes_recycler_card.view.*

class NotesRecyclerAdapter(private val items: ArrayList<NoteModel>, private val context: Context) : RecyclerView.Adapter<NotesRecyclerAdapter.MyViewHolder>() {
    private var list: ArrayList<NoteModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_recycler_card, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.month.text = list[position].month
        holder.day.text = list[position].day
        holder.msg.text = list[position].description

        holder.close.setOnClickListener { removeItem(position) }
    }

    private fun removeItem(position: Int){
        list.removeAt(position)
        notifyDataSetChanged()
    }

    class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
//        val tvAnimalType = view.tv_animal_type
        val month: TextView = view.note_month
        val day: TextView = view.note_day
        val msg: TextView = view.note_content
        val close: ImageView = view.cross
    }

    fun getList(): ArrayList<NoteModel>{
        return this.list
    }
}