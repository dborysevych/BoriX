package com.example.sharetextbetweendevices.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.sharetextbetweendevices.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaskListActivity: ComponentActivity() {
    @SuppressLint("CommitPrefEdits")
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var listView: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var addButton: Button
    private lateinit var deleteButton: Button
    private val selectedItems = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        database = FirebaseDatabase.getInstance()
        databaseReference = intent.getStringExtra("dbRef")?.let { database.getReference(it) }!!

        listView = findViewById(R.id.task_string_list)
        inputEditText = findViewById(R.id.task_input_edit_text)
        addButton = findViewById(R.id.task_add_button)
        deleteButton = findViewById(R.id.task_delete_button)

        addButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotEmpty()) {
                databaseReference.push().setValue(inputText)
                inputEditText.text.clear()
            }
        }

        deleteButton.setOnClickListener {
            if (selectedItems.isNotEmpty()) {
                for (key in selectedItems) {
                    databaseReference.child(key).removeValue()
                }
                selectedItems.clear()
            }
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listView.removeAllViews()
                selectedItems.clear()

                for (snapshot in dataSnapshot.children) {
                    val string = snapshot.getValue(String::class.java) ?: ""
                    val key = snapshot.key ?: ""

                    val itemLayout = layoutInflater.inflate(R.layout.list_item, listView, false)
                    val textView = itemLayout.findViewById<TextView>(R.id.task_string_text)
                    val checkBox = itemLayout.findViewById<CheckBox>(R.id.task_delete_checkbox)

                    textView.text = string

                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            selectedItems.add(key)
                            textView.setTextColor(Color.Green.toArgb())
                        } else {
                            selectedItems.remove(key)
                            textView.setTextColor(Color.White.toArgb())
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
