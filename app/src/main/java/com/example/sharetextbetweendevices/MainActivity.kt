package com.example.sharetextbetweendevices

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.example.sharetextbetweendevices.activities.DenxiikBankActivity
import com.example.sharetextbetweendevices.activities.MyBankActivity
import com.example.sharetextbetweendevices.activities.StoreListActivity
import com.example.sharetextbetweendevices.activities.TaskListActivity

const val family = "boriX"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val privateTaskListPreview = findViewById<CardView>(R.id.private_task_list_preview)
        privateTaskListPreview.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("dbRef", "$family/${(Build.MODEL + Build.FINGERPRINT).replace("[^A-Za-z0-9]".toRegex(), "_")}/private_task_list")
            startActivity(intent)
        }

        val storeListPreview = findViewById<CardView>(R.id.store_list_preview)
        storeListPreview.setOnClickListener {
            val intent = Intent(this, StoreListActivity::class.java)
            startActivity(intent)
        }

        val denxiikBankPreview = findViewById<CardView>(R.id.denxiik_bank_preview)
        denxiikBankPreview.setOnClickListener {
            val intent = Intent(this, DenxiikBankActivity::class.java)
            startActivity(intent)
        }

        val taskListPreview = findViewById<CardView>(R.id.task_list_preview)
        taskListPreview.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("dbRef", "$family/task_list")
            startActivity(intent)
        }

        val goalsPreview = findViewById<CardView>(R.id.denxiik_goals_preview)
        goalsPreview.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("dbRef", "$family/${(Build.MODEL + Build.FINGERPRINT).replace("[^A-Za-z0-9]".toRegex(), "_")}/goals_list")
            startActivity(intent)

        }

        val ideasPreview = findViewById<CardView>(R.id.denxiik_ideas_preview)
        ideasPreview.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("dbRef", "$family/${(Build.MODEL + Build.FINGERPRINT).replace("[^A-Za-z0-9]".toRegex(), "_")}/ideas_list")
            startActivity(intent)
        }

        val myBankPreview = findViewById<CardView>(R.id.my_bank_preview)
        myBankPreview.setOnClickListener {
            val intent = Intent(this, MyBankActivity::class.java)
            startActivity(intent)
        }
    }
}