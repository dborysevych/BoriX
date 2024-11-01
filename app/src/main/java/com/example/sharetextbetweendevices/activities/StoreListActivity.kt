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
import com.google.firebase.database.*

class StoreListActivity : ComponentActivity() {
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
        setContentView(R.layout.store_list_activity)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("boriX/store_list")

        listView = findViewById(R.id.store_string_list)
        inputEditText = findViewById(R.id.store_input_edit_text)
        addButton = findViewById(R.id.store_add_button)
        deleteButton = findViewById(R.id.store_delete_button)
        deleteAllButton = findViewById(R.id.store_delete_all_button)

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

                    val itemLayout = layoutInflater.inflate(R.layout.store_list_item, listView, false)
                    val textView = itemLayout.findViewById<TextView>(R.id.store_string_text)
                    val checkBox = itemLayout.findViewById<CheckBox>(R.id.store_delete_checkbox)

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