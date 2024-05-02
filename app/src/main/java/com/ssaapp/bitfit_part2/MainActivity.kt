package com.ssaapp.bitfit_part2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssaapp.bitfit_part2.fragment.LogFragment
import com.ssaapp.bitfit_part2.fragment.SummaryFragment

class MainActivity : AppCompatActivity() {
    lateinit var bottomNav:BottomNavigationView
    private val channelId = "com.humcomp.bitfitpart2"
    private val description = "You've Been Hit By A....Smooth Notification!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addFoodBtn :Button = findViewById(R.id.add_food_btn)

        addFoodBtn.setOnClickListener{
            val intent = Intent(this, UserInputActivity::class.java)
            startActivity(intent)
        }

        // bottom nav
        loadFragment(LogFragment())
        bottomNav = findViewById<View>(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.log -> {
                    loadFragment(LogFragment())
                    true
                }
                R.id.summary -> {
                    loadFragment(SummaryFragment())
                    true
                }
                else -> false
            }
        }

        createNotificationChannel()
        var builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time To Add That Food!")
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)


        var notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // this is such a nasty way to schedule notifications
        // I'm ashamed xD
        val notifyThread = Thread (Runnable {
            while (true) {
                notificationManager.notify(1234, builder.build())
                Thread.sleep(86400 * 24)
            }
        })
        notifyThread.start()

        //notificationManager.notify(1234, builder.build())
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}