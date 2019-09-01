package com.example.macrocounterremaster.webServices.responses

class RegisterResponseModel(
    private var id: String?,
    private var code: String?,
    private var email: String?,
    private var protein_goal: String?,
    private var carbs_goal: String?,
    private var fats_goal: String?,
    private var protein_progress: String?,
    private var carbs_progress: String?,
    private var fats_progress: String?
) {

    fun getId(): String? {
        return id
    }

    fun getCode(): String? {
        return code
    }

    fun getEmail(): String? {
        return email
    }

    fun getProteinGoal(): String? {
        return protein_goal
    }

    fun getProteinProgress(): String? {
        return protein_progress
    }

    fun getCarbsGoal(): String? {
        return carbs_goal
    }

    fun getCarbsProgress(): String? {
        return carbs_progress
    }

    fun getFatsGoal(): String? {
        return fats_goal
    }

    fun getFatsProgress(): String? {
        return fats_progress
    }

}