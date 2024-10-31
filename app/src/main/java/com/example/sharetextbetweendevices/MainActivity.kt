package com.example.sharetextbetweendevices

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.example.sharetextbetweendevices.activities.StoreListActivity

class MainActivity : ComponentActivity() {

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
            val intent = Intent(this, StoreListActivity::class.java)
            startActivity(intent)
        }
    }
}