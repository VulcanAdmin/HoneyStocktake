package com.example.honeystocktake

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.honeystocktake.Updates.Update
import com.example.honeystocktake.databinding.ActivityBulkBinding

class Bulk: AppCompatActivity() {
    private lateinit var binding: ActivityBulkBinding
    private var plate1: String? = null
    private var plate2: String? = null
    private var location: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBulkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve extras from intent
        val plateValue = intent.getStringExtra("plateValue")
        val name = intent.getStringExtra("name")
        val level = intent.getIntExtra("level", 0)
        val locationNames = intent.getStringArrayListExtra("locationNames")
        val locationNamesMultipleList = locationNames?.toMutableList() ?: mutableListOf()

        val autotextView = findViewById<AutoCompleteTextView>(R.id.autoLocation)
        autotextView.threshold = 1

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            locationNamesMultipleList
        )
        autotextView.setAdapter(adapter)

//        val locationNamesMultipleList = locationNames?.toMutableList()
//
//        val autotextView
//                = findViewById<AutoCompleteTextView>(R.id.autoLocation)
//        // Get the array of languages
//        val locationsArray
//                = resources.getStringArray(R.array.location_default_array)
//
//        //threshold for typing
//        autotextView.threshold = 1
//
//        // Create adapter and add in AutoCompleteTextView
//        val adapter
//                = ArrayAdapter(this,
//            android.R.layout.simple_list_item_1, locationNamesMultipleList)
//        autotextView.setAdapter(adapter)


        // Set onClickListener for commitButton
        binding.commitButton.setOnClickListener {

                //Still need to put if to make sure all values are filled in.

            // Get the values entered in firstID and lastID
            val plateValue1 = binding.firstID.text.toString().toIntOrNull() ?: 0
            val plateValue2 = binding.lastID.text.toString().toIntOrNull() ?: 0



            // Determine the smallest plate value
            plate1 = if (plateValue2 < plateValue1) {
                plateValue2.toString()
            } else {
                plateValue1.toString()
            }

            // Assign the other plate value to plate2
            plate2 = if (plate1 == plateValue1.toString()) {
                plateValue2.toString()
            } else {
                plateValue1.toString()
            }

            // Retrieve the value entered in txtInLocation and save it
            location = binding.autoLocation.text.toString()

            val bundle = Bundle()
            val intent = Intent(this, Update::class.java)
            intent.putExtra("plate", plate1)
            intent.putExtra("plate2", plate2)
            intent.putExtra("location", location)
            intent.putExtra("stockBy", name)
            intent.putExtra("bulk", 1)
            ContextCompat.startActivity(this, intent, bundle)
            reset()
        }

        //might not want this
//        binding.firstID.doAfterTextChanged { editable ->
//            updateIds()
//        }
//        binding.lastID.doAfterTextChanged { editable ->
//            updateIds()
//        }


        // Display name and fill plateValue if provided
        binding.tvUser.text = name ?: "Not Identified"
        binding.txtInPlate.editText?.setText(plateValue)

        // Check user level and disable commitButton if level is less than 5
        if (level < 5) {
            binding.commitButton.isEnabled = false
            binding.commitButton.text = "Insufficient Permissions"
        }

        // Set onClickListener for backButton
        binding.backButton.setOnClickListener {
            // Navigate back to main screen
            onBackPressed()
        }
    }

    private fun reset() {//return values to default.
        plate1 = ""
        plate2 = ""
        location = ""
        binding.firstID.setText(plate1)
        binding.lastID.setText(plate2)
        binding.autoLocation.setText(location)
    }

//    private fun updateIds() {
//        val firstIdText = binding.firstID.text.toString()
//        val lastIdText = binding.lastID.text.toString()
//
//        if (firstIdText.isNotEmpty() && lastIdText.isNotEmpty()) {
//            val firstId = firstIdText.toInt()
//            val lastId = lastIdText.toInt()
//
//            if (firstId > lastId) {
//                binding.firstID.setText(lastId.toString())
//                binding.lastID.setText(firstId.toString())
//            }
//        }    }


}