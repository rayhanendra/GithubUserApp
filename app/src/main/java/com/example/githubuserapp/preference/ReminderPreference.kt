package com.example.githubuserapp.preference

import android.content.Context

data class Reminder (
    var isReminded: Boolean = false
)

class ReminderPreference(context: Context) {
    companion object{
        private const val PREF_NAME = "ReminderPreference"
        private const val REMINDER = "isReminder"
    }
    private val preference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setReminder(value: Reminder){
        val editor = preference.edit()
        editor.putBoolean(REMINDER, value.isReminded)
        editor.apply()
    }

    fun getReminder(): Reminder {
        val reminder = Reminder()
        reminder.isReminded = preference.getBoolean(REMINDER, false)
        return reminder
    }
}