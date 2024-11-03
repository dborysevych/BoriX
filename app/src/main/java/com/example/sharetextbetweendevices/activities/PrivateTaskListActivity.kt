package com.example.sharetextbetweendevices.activities

import android.annotation.SuppressLint
import android.os.Build
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
import com.example.sharetextbetweendevices.family
import com.google.firebase.database.*

class PrivateTaskListActivity : ComponentActivity() {
    @SuppressLint("CommitPrefEdits")
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var listView: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var addButton: Button
    private lateinit var deleteButton: Button
    private lateinit var deleteAllButton: Button
    private val selectedItems = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.private_task_list_activity)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("$family/${(Build.MODEL + Build.FINGERPRINT).replace("[^A-Za-z0-9]".toRegex(), "_")}/private_task_list")

        listView = findViewById(R.id.private_task_string_list)
        inputEditText = findViewById(R.id.private_task_input_edit_text)
        addButton = findViewById(R.id.private_task_add_button)
        deleteButton = findViewById(R.id.private_task_delete_button)
        deleteAllButton = findViewById(R.id.private_task_delete_all_button)

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

        deleteAllButton.setOnClickListener {
            databaseReference.removeValue()
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listView.removeAllViews()
                selectedItems.clear()

                for (snapshot in dataSnapshot.children) {
                    val string = snapshot.getValue(String::class.java) ?: ""
                    val key = snapshot.key ?: ""

                    val itemLayout = layoutInflater.inflate(R.layout.private_task_list_item, listView, false)
                    val textView = itemLayout.findViewById<TextView>(R.id.private_task_string_text)
                    val checkBox = itemLayout.findViewById<CheckBox>(R.id.private_task_delete_checkbox)

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