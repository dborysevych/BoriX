package com.example.sharetextbetweendevices

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("strings")

        listView = findViewById(R.id.string_list)

        val inputEditText = findViewById<EditText>(R.id.input_edit_text)

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    databaseReference.push().setValue(s.toString())
                }
            }
        })

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