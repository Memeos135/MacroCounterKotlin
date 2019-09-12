package com.example.macrocounterremaster.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.macrocounterremaster.R
import com.example.macrocounterremaster.helpers.EmailHelper
import com.example.macrocounterremaster.models.StageOneValues
import com.example.macrocounterremaster.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_stage_one.*
import kotlinx.android.synthetic.main.fragment_stage_one.view.*

class RegisterFragmentOne(): Fragment() {
    var layout : Int = 0
    private lateinit var listener: NextStage

    constructor (id : Int) : this() {
        layout = id
    }

    interface NextStage{
        fun proceed(stageOneValues: StageOneValues)
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

        view.btnNext.setOnClickListener {
            if(et_name.text.toString().isNotEmpty() && et_email.text.toString().isNotEmpty() && et_password.text.toString().isNotEmpty()){
                if(EmailHelper.regexEmail(et_email.text.toString())){
                    listener.proceed(StageOneValues(et_name.text.toString(), et_email.text.toString(), et_password.text.toString()))
                }else{
                    Snackbar.make(ll_one, getString(R.string.uncorrect_email), Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(ll_one, getString(R.string.fill_empty_fields), Snackbar.LENGTH_SHORT).show()
            }
        }

        // Return the fragment view/layout
        return view
    }
}