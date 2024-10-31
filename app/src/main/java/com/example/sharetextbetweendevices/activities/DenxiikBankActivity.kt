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
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var listView: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var addButton: Button
    private lateinit var subtractButton: Button
    private var sum = 0 // Variable to store the running total

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bank_list_activity)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("bank_list")

        listView = findViewById(R.id.bank_string_list)
        inputEditText = findViewById(R.id.bank_input_edit_text)
        addButton = findViewById(R.id.bank_add_button)
        subtractButton = findViewById(R.id.bank_subtract_button)

        // Update onClickListeners for both buttons
        addButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotEmpty() && isNumeric(inputText)) {
                val value = inputText.toInt()
                databaseReference.push().setValue(value)
                sum += value
                updateListAndSum() // Call to update list and sum
                inputEditText.text.clear()
            }
        }

        subtractButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotEmpty() && isNumeric(inputText)) {
                val value = -inputText.toInt() // Convert to negative for subtraction
                databaseReference.push().setValue(value)
                sum += value
                updateListAndSum()
                inputEditText.text.clear()
            }
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listView.removeAllViews()

                for (snapshot in dataSnapshot.children) {
                    val value = snapshot.getValue(Int::class.java) ?: 0

                    val itemLayout = layoutInflater.inflate(R.layout.bank_list_item, listView, false)
                    val textView = itemLayout.findViewById<TextView>(R.id.bank_string_amount)

                    textView.text = if (value > 0) "+$value" else "$value"

                    listView.addView(itemLayout)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    // Function to check if input is a valid number
    private fun isNumeric(str: String): Boolean {
        return str.matches("[-+]?[0-9]+".toRegex())
    }

    // Function to update list and sum display
    private fun updateListAndSum() {
        val sumTextView = findViewById<TextView>(R.id.bank_total_sum)
        sumTextView.text = "Total: $sum"
    }
}