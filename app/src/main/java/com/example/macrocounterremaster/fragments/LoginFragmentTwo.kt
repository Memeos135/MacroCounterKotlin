package com.example.macrocounterremaster.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.models.FullValues
import com.example.macrocounterremaster.models.StageOneValues
import com.example.macrocounterremaster.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_stage_two.*
import kotlinx.android.synthetic.main.fragment_stage_two.view.*

class LoginFragmentTwo(): Fragment() {

    var layout : Int = 0
    lateinit var stageOneValues: StageOneValues
    private lateinit var listener: StageTwoInterface

    constructor(layout_id: Int, stage_one_values: StageOneValues): this(){
        layout = layout_id
        stageOneValues = stage_one_values
    }

    interface StageTwoInterface{
        fun goBack()
        fun register(fullValues: FullValues)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context) {
            is StageTwoInterface -> listener = context
            else -> Log.i(Constants.IMPLEMENTATION_ERROR, getString(R.string.implement_interface))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(layout, container, false)

        view.btnBack.setOnClickListener { listener.goBack() }
        view.btnNext.setOnClickListener {
            if(et_protein.text.toString().isNotEmpty() && et_carbs.text.toString().isNotEmpty() && et_fat.text.toString().isNotEmpty()){
                listener.register(FullValues(stageOneValues.name, stageOneValues.email, stageOneValues.password, et_protein.text.toString(), et_carbs.text.toString(), et_fat.text.toString()))
            }else{
                Snackbar.make(ll_two, getString(R.string.fill_empty_fields), Snackbar.LENGTH_SHORT).show()
            }
        }

        // Return the fragment view/layout
        return view
    }
}