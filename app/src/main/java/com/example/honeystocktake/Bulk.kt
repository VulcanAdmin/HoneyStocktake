package com.example.honeystocktake

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.honeystocktake.Updates.Update
import com.example.honeystocktake.databinding.ActivityBulkBinding
import com.example.retrofittest.utils.Constants.Companion.tLevel

class Bulk: AppCompatActivity() {
    private lateinit var binding: ActivityBulkBinding
    private var plate1: String? = null
    private var plate2: String? = null
    private var location: String? = null
    private var length: Double? = null
    private var width: Double? = null
    private lateinit var lengthEditText: EditText
    private lateinit var widthEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBulkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        // Retrieve extras from intent
        val plateValue = intent.getStringExtra("plateValue")
        val name = intent.getStringExtra("name")
        val level = intent.getIntExtra("level", 0)
        val locationNames = intent.getStringArrayListExtra("locationNames")
        length = bundle?.getString("length")?.toDoubleOrNull()
        width = bundle?.getString("width")?.toDoubleOrNull()
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

            val enteredLocation = binding.autoLocation.text.toString()
            val validLocation = findMatchingLocation(enteredLocation, locationNamesMultipleList)

            if (validLocation == null) {
                showError("Invalid location. Please enter a valid location.")
            } else {
                location = validLocation // Now 'location' holds a validated entry from 'locationNames'

                // Additional logic to proceed with commit
                commitData(name)
            }

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

        // Check user level and disable commitButton if level is less than  level 6
        if (level < tLevel) {
            binding.commitButton.isEnabled = false
            binding.commitButton.text = "Insufficient Permissions"
        }

        // Set onClickListener for backButton
        binding.backButton.setOnClickListener {
            // Navigate back to main screen
            onBackPressed()
        }
    }

    private fun commitData(name: String?) {
        val plateValue1 = binding.firstID.text.toString().toIntOrNull()
        val plateValue2 = binding.lastID.text.toString().toIntOrNull()

        // Retrieve the value entered in txtInLocation and save it
        location = binding.autoLocation.text.toString()

        // Check if plateValue1, plateValue2, or location is null (empty)
        if (plateValue1 == null || plateValue2 == null ) {
            // Display an error message indicating that the plate values are empty
            showError("Please enter all required fields.")
        } else {
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

            // Calculate the difference between the plate values
            val difference = kotlin.math.abs(plateValue2 - plateValue1)

            // Check if the difference is greater than 50
            if (difference > 50) {
                // Display an error message
                showError("Plate values should not differ by more than 50.")
            } else {
                // Perform commit action
                showPopupDialog(name, length, width)
            }
        }
    }

    private fun showPopupDialog(name: String?, length: Double?, width: Double?) {
        // Inflate the layout for the dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null)

        // Find the EditText fields and buttons in the dialog layout
        lengthEditText = dialogView.findViewById(R.id.editTextLength)
        widthEditText = dialogView.findViewById(R.id.editTextWidth)

        // Prefill the EditText fields if values are available
        this.length?.let {
            lengthEditText.setText(it.toString())
        }
        this.width?.let {
            widthEditText.setText(it.toString())
        }

        val confirmButton = dialogView.findViewById<Button>(R.id.buttonConfirm)
        val cancelButton = dialogView.findViewById<Button>(R.id.buttonCancel)

        // Create AlertDialog Builder
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Enter Length and Width")

        // Create AlertDialog
        val dialog = builder.create()

        // Set click listener for the confirm button
        confirmButton.setOnClickListener {
            // Retrieve entered length and width
            val enteredLength = lengthEditText.text.toString().toDoubleOrNull()
            val enteredWidth = widthEditText.text.toString().toDoubleOrNull()

            // Check if length and width are valid
            if (enteredLength != null && enteredWidth != null) {
                // Call update function with length and width
                update(name, enteredLength, enteredWidth)
                // Dismiss the dialog
                dialog.dismiss()
            } else {
                // Display error message if input is invalid
                Toast.makeText(this, "Please enter valid length and width", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for the cancel button
        cancelButton.setOnClickListener {
            // Dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    private fun update(name: String?, enteredLength: Double, enteredWidth: Double) {




        val bundle = Bundle()
        val intent = Intent(this, Update::class.java)
        intent.putExtra("plate", plate1)
        intent.putExtra("plate2", plate2)
        intent.putExtra("location", location)
        intent.putExtra("stockBy", name)
        intent.putExtra("bulk", 1)
        intent.putExtra("blength", enteredLength)
        intent.putExtra("bwidth", enteredWidth)
        ContextCompat.startActivity(this, intent, bundle)
        reset()
    }

    fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

private fun findMatchingLocation(enteredValue: String, locationNames: MutableList<String>): String? {
    return locationNames.firstOrNull { it.equals(enteredValue, ignoreCase = true) }
}


}