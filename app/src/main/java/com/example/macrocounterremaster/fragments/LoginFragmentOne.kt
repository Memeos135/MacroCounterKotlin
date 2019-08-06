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
import kotlinx.android.synthetic.main.fragment_stage_one.view.*

class LoginFragmentOne(): Fragment() {
    var layout : Int = 0
    private lateinit var listener: NextStage

    constructor (id : Int) : this() {
        layout = id
    }

    interface NextStage{
        fun proceed()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is NextStage) {
            listener = context
        } else {
            Log.i(Constants.IMPLEMENTATION_ERROR, getString(R.string.implement_interface))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(layout, container, false)

        view.btnNext.setOnClickListener { listener.proceed() }

        // Return the fragment view/layout
        return view
    }
}