package com.example.sharetextbetweendevices.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.sharetextbetweendevices.R
import com.example.sharetextbetweendevices.family
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat

class DenxiikBankActivity : ComponentActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseTotalReference: DatabaseReference

    private lateinit var listView: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var inputInfoEditText: EditText
    private lateinit var addButton: Button
    private lateinit var subtractButton: Button
    private var total_sum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bank_list_activity)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("$family/bank_list")
        databaseTotalReference = database.getReference("$family/total_sum")

        listView = findViewById(R.id.bank_string_list)
        inputEditText = findViewById(R.id.bank_input_edit_text)
        inputInfoEditText = findViewById(R.id.bank_input_info_edit_text)
        addButton = findViewById(R.id.bank_add_button)
        subtractButton = findViewById(R.id.bank_subtract_button)

        // Update onClickListeners for both buttons
        addButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotEmpty() && isNumeric(inputText)) {
                val value = inputText.toInt()
                val info = inputInfoEditText.text.toString()
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
                val item = hashMapOf("value" to value, "info" to info, "date" to date)
                databaseReference.push().setValue(item)
                databaseTotalReference.setValue(total_sum + value)
                inputEditText.text.clear()
            }
        }

        subtractButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotEmpty() && isNumeric(inputText)) {
                val value = -inputText.toInt()
                val info = inputInfoEditText.text.toString()
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
                val item = hashMapOf("value" to value, "info" to info, "date" to date)
                databaseReference.push().setValue(item)
                databaseTotalReference.setValue(total_sum + value)
                inputEditText.text.clear()
            }
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listView.removeAllViews()


                for (snapshot in dataSnapshot.children) {
                    val value = snapshot.child("value").getValue(Int::class.java) ?: 0
                    val info = snapshot.child("info").getValue(String::class.java) ?: "N/A"
                    val dateAdded = snapshot.child("date").getValue(String::class.java) ?: "N/A"

                    val itemLayout = layoutInflater.inflate(R.layout.bank_list_item, listView, false)
                    val textViewValue = itemLayout.findViewById<TextView>(R.id.bank_string_amount)
                    val textViewInfo = itemLayout.findViewById<TextView>(R.id.bank_string_info)
                    val textViewDate = itemLayout.findViewById<TextView>(R.id.bank_string_date)

                    if (value < 0) {
                        textViewValue.text = "$value"
                        textViewValue.setTextColor(Color.Red.toArgb())
                    } else {
                        textViewValue.text = "+$value"
                        textViewValue.setTextColor(Color.Green.toArgb())
                    }
                    textViewDate.text = dateAdded
                    textViewInfo.text = info

                    listView.addView(itemLayout)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })

        // Listen for changes in total_sum
        databaseTotalReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total_sum = dataSnapshot.getValue(Int::class.java) ?: 0
                val sumTextView = findViewById<TextView>(R.id.bank_total_sum)
                if (total_sum < 0) {
                    sumTextView.setTextColor(Color.Red.toArgb())
                } else {
                    sumTextView.setTextColor(Color.Green.toArgb())
                }
                sumTextView.text = "Всього: $total_sum"
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
}