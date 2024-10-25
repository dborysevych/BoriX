package com.example.sharetextbetweendevices

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    @SuppressLint("CommitPrefEdits")
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var listView: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("strings")

        listView = findViewById(R.id.string_list)
        inputEditText = findViewById(R.id.input_edit_text)
        addButton = findViewById(R.id.add_button)

        addButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotEmpty()) {
                databaseReference.push().setValue(inputText)
                inputEditText.text.clear()
            }
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                listView.removeAllViews()

                for (snapshot in dataSnapshot.children) {
                    val string = snapshot.getValue(String::class.java) ?: ""
                    val key = snapshot.key ?: ""

                    val itemLayout = layoutInflater.inflate(R.layout.list_item, listView, false)
                    val textView = itemLayout.findViewById<TextView>(R.id.string_text)
                    val checkBox = itemLayout.findViewById<CheckBox>(R.id.delete_checkbox)

                    textView.text = string
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            databaseReference.child(key).removeValue()
                        }
                    }

                    listView.addView(itemLayout)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
}