package com.example.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.example.githubuserapp.alarm.AlarmReceiver
import com.example.githubuserapp.databinding.ActivitySettingsBinding
import com.example.githubuserapp.preference.Reminder
import com.example.githubuserapp.preference.ReminderPreference

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var reminder: Reminder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reminderPreference = ReminderPreference(this)
        binding.btnAlarm.isChecked = reminderPreference.getReminder().isReminded
        alarmReceiver = AlarmReceiver()

        alarm()
        language()
    }

    private fun alarm(){
        binding.btnAlarm.setOnCheckedChangeListener {
            _, isChecked ->
            if (isChecked){
                alarmReminder(true)
                alarmReceiver.setRepeatingAlarm(this,getString(R.string.alarm_type), getString(R.string.time_alarm), getString(R.string.notif_message))
            } else {
                alarmReminder(false)
                alarmReceiver.cancelAlarm(this)
            }
        }
    }
    private fun alarmReminder(state: Boolean){
        val reminderPref = ReminderPreference(this)
        reminder = Reminder()
        reminder.isReminded = state
        reminderPref.setReminder(reminder)
    }

    private fun language(){
        binding.btnLanguange.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.btn_languange -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }
}