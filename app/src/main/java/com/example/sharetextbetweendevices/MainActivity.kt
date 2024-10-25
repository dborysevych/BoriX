package com.example.sharetextbetweendevices

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {
    @SuppressLint("CommitPrefEdits")
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database
        val databaseReference = database.getReference("message")

        val textView = findViewById<TextView>(R.id.textViews)
        val editText = findViewById<EditText>(R.id.editText)

        // Load the shared text from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sharedText = dataSnapshot.getValue(String::class.java) ?: ""
                editText.setText(sharedText)
                textView.setText(sharedText)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the shared text in Firebase
                databaseReference.setValue(s.toString())
            }
        })
    }
}