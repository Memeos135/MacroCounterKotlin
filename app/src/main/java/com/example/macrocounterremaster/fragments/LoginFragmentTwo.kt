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
import kotlinx.android.synthetic.main.fragment_stage_two.view.*

class LoginFragmentTwo(layout_id: Int): Fragment() {
    var layout : Int = layout_id
    private lateinit var listener: PreviousStage

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

        view.button.setOnClickListener { listener.goBack() }

        // Return the fragment view/layout
        return view
    }
}