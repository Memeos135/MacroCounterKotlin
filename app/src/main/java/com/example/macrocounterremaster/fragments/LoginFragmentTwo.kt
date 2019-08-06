package com.example.macrocounterremaster.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_stage_two.view.*

class LoginFragmentTwo(): Fragment() {

    var layout : Int = 0
    private lateinit var listener: PreviousStage

    constructor(layout_id: Int): this(){
        layout = layout_id
    }

    interface PreviousStage{
        fun goBack()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PreviousStage) {
            listener = context
        } else {
            Log.i(Constants.IMPLEMENTATION_ERROR, getString(R.string.implement_interface))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(layout, container, false)

        view.btnBack.setOnClickListener { listener.goBack() }
        view.btnNext.setOnClickListener { Snackbar.make(view, "It works!", Snackbar.LENGTH_SHORT).show() }

        // Return the fragment view/layout
        return view
    }
}