package com.example.sharetextbetweendevices

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.example.sharetextbetweendevices.activities.DenxiikBankActivity
import com.example.sharetextbetweendevices.activities.StoreListActivity
import com.example.sharetextbetweendevices.activities.TaskListActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    private  var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private  var databaseReference: DatabaseReference = database.getReference("errors")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

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
            try {
                val intent = Intent(this, TaskListActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                databaseReference.child("error").setValue(e.toString())
            }
        }
    }
}