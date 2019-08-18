package com.example.macrocounterremaster.helpers

class EmailHelper {
    companion object{
        fun regexEmail(email: String): Boolean{
            // using built in matcher for email regex
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}