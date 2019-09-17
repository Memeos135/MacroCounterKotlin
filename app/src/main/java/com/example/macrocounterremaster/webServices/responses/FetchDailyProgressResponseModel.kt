package com.example.macrocounterremaster.webServices.responses

class FetchDailyProgressResponseModel(
    private val protein_progress: String?,
    private val carbs_progress: String?,
    private val fats_progress: String?,
    private val error: String?
)  {

    fun getFatsProgress(): String?{
        return fats_progress
    }

    fun getCarbsProgress(): String?{
        return carbs_progress
    }

    fun getProteinProgress(): String?{
        return protein_progress
    }

    fun getError(): String?{
        return error
    }
}