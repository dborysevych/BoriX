package com.example.sharetextbetweendevices.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.sharetextbetweendevices.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DenxiikBankActivity : ComponentActivity() {
    @SuppressLint("CommitPrefEdits")
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var listView: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bank_list_activity)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("bank_list")

        listView = findViewById(R.id.bank_string_list)
        inputEditText = findViewById(R.id.bank_input_edit_text)
        addButton = findViewById(R.id.bank_add_button)

        addButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotEmpty()) {
                databaseReference.push().setValue(inputText)
                inputEditText.text.clear()
            }
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listView.removeAllViews()

                for (snapshot in dataSnapshot.children) {
                    val string = snapshot.getValue(String::class.java) ?: ""

                    val itemLayout = layoutInflater.inflate(R.layout.bank_list_item, listView, false)
                    val textView = itemLayout.findViewById<TextView>(R.id.bank_string_amount)

                    textView.text = string

                    listView.addView(itemLayout)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
}