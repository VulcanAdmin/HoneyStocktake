package com.example.honeystocktake

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.honeystocktake.Updates.Update
import com.example.honeystocktake.api.Datamodel
import com.example.honeystocktake.api.MainViewModel
import com.example.honeystocktake.api.Repository
import com.example.honeystocktake.api.spinnerListData
import com.example.retrofittest.MainViewModelFactory
import com.example.retrofittest.utils.Constants
import com.example.retrofittest.utils.Constants.Companion.uniqueId
import com.example.retrofittest.utils.Constants.Companion.web
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    companion object {
        const val ACTION_BARCODE_DATA = "com.honeywell.sample.action.BARCODE_DATA"
        const val ACTION_CLAIM_SCANNER = "com.honeywell.aidc.action.ACTION_CLAIM_SCANNER"
        const val ACTION_RELEASE_SCANNER = "com.honeywell.aidc.action.ACTION_RELEASE_SCANNER"
        const val EXTRA_SCANNER = "com.honeywell.aidc.extra.EXTRA_SCANNER"
        const val EXTRA_PROFILE = "com.honeywell.aidc.extra.EXTRA_PROFILE"
        const val EXTRA_PROPERTIES = "com.honeywell.aidc.extra.EXTRA_PROPERTIES"
    }

    private lateinit var tvUser: TextView
    private lateinit var tvHeader: TextView
    private lateinit var tvStatus: TextView
    private lateinit var locationTextView: TextView
    private lateinit var plateTextView: TextView
    private lateinit var lengthTextView: TextView
    private lateinit var widthTextView: TextView
    private lateinit var thicknessTextView: TextView
    private lateinit var tvGrade: TextView
    private lateinit var problemSpinner: Spinner
    private lateinit var imageView: ImageView
    private lateinit var manualButton: Button
    private lateinit var commitButton: Button
    private lateinit var infoButton: Button
    private lateinit var loginButton: Button
    private lateinit var btnBulk: Button
    private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Intent>

    private var locationNamesList: MutableList<String> = mutableListOf()

    private lateinit var viewModel: MainViewModel
    var sheetList: ArrayList<Datamodel> = ArrayList()
    private var lastCommitTime: Long = 0
    var sheetData: Datamodel? = null
    var data : Int = 0
    var name = "Not Identified"
    var level = 0

    private var locationValue: String? = null
    private var plateValue: String? = null
    var doubleClick: Boolean? = false
    var enteredValue = "123456"

    var imageUrl2 = "https://images.unsplash.com/photo-1689631282155-924d15b957e9?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=687&q=80"
    var stockBy = ""
    var stockDate = ""
    private var locationLast = ""
    var preLocation = ""
    var prePlate = "0"
    var query = 0
    var lastQuery = 0


    var spinnerList: ArrayList<spinnerListData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tvUser = findViewById(R.id.tvUser)
        tvHeader = findViewById(R.id.tvHeader)
        tvStatus = findViewById(R.id.tvStatus)
        locationTextView = findViewById(R.id.locationTextView)
        plateTextView = findViewById(R.id.plateTextView)
        lengthTextView = findViewById(R.id.lengthTextView)
        widthTextView = findViewById(R.id.widthTextView)
        thicknessTextView = findViewById(R.id.thicknessTextView)
        tvGrade = findViewById(R.id.tvGrade)
        problemSpinner = findViewById(R.id.problemSpinner)
        imageView = findViewById(R.id.imageView1)
        manualButton = findViewById(R.id.manualButton)
        commitButton = findViewById(R.id.commitButton)
        infoButton = findViewById(R.id.infoButton)
        loginButton = findViewById(R.id.loginButton)
        btnBulk = findViewById(R.id.btnBulk)




        imageView.setBackgroundResource(0)

        val filter = IntentFilter()
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        filter.addAction(resources.getString(R.string.activity_intent_filter_action))
        registerReceiver(myBroadcastReceiver, filter)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        fillQueryList()
        fillLocationsArray()


        btnBulk.setOnClickListener {
            if(!plateValue.isNullOrEmpty()){
                //        //broadcast start activity
                val bundle = Bundle()
                val intent = Intent(this, Bulk::class.java)
                intent.putStringArrayListExtra("locationNamesList", ArrayList(locationNamesList))
                intent.putExtra("plateValue", plateValue)
                intent.putExtra("name", name)
                intent.putExtra("level", level)
                startActivity(intent, bundle)}else {
                val bundle = Bundle()
                val intent = Intent(this, Bulk::class.java)
                intent.putStringArrayListExtra("locationNames", ArrayList(locationNamesList))
                intent.putExtra("name", name)
                intent.putExtra("level", level)
                startActivity(intent, bundle)
            }


        }

        infoButton.setOnClickListener {
            if(data == 1){
                //        //broadcast start activity
                val bundle = Bundle()
                val intent = Intent(this, Information::class.java)
                intent.putExtra("plateValue", plateValue)
                intent.putExtra("stockBy", sheetData?.stockBy)
                intent.putExtra("stockDate", sheetData?.stockDate)
                intent.putExtra("locationLast", locationLast)
                intent.putExtra("query", lastQuery)
                startActivity(intent, bundle)}
        }


        loginButton.setOnClickListener {
            //Go to login which then intents to activity with a result expeted.
            login()

        }



        // Set click listener for the manual input button
        manualButton.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.popup_manual_input, null)
            val plateButton = dialogView.findViewById<Button>(R.id.buttonPlate)
            val locationButton = dialogView.findViewById<Button>(R.id.buttonLocation)

            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .show()

            plateButton.setOnClickListener {
                // Handle plate button click
                dialog.dismiss()
                showPlateInputDialog()
            }

            locationButton.setOnClickListener {
                // Handle location button click
                dialog.dismiss()
                showLocationInputDialog()
            }

        }

        updateTextColor()

        // Set click listener for the commit button
        commitButton.setOnClickListener {

            if (name != "Not Identified"){
                if (locationValue.isNullOrEmpty() || plateValue.isNullOrEmpty()) {
                    // Both 'Location' and 'Plate' values are needed
                    Toast.makeText(this, "Both Location and Plate values are required", Toast.LENGTH_SHORT).show()
                } else {
                    lastCommitTime = System.currentTimeMillis()
                    updateTextColor()

//                    Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show()

                    // Perform database update with locationValue and plateValue

                    val bundle = Bundle()
                    val intent = Intent(this, Update::class.java)
                    intent.putExtra("plate", prePlate)
                    intent.putExtra("location", locationValue)
                    intent.putExtra("length", sheetData?.length)
                    intent.putExtra("width", sheetData?.width)
                    intent.putExtra("query", query)
                    intent.putExtra("stockBy", name)
                    ContextCompat.startActivity(this, intent, bundle)
                    reset()
                }
            }else{
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Login Required")
                alertDialogBuilder.setMessage("Please login to do Stock Take.")
                alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                    // Navigate to the login
                    login()
                }
                alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
                    // Navigate to the login screen
                    // finish()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show() }


        }
        //get the list of queries through API
        


        // Set the spinner item selection listener
        problemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                if(plateValue != "0") {
                    query = p2
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // Set click listener for the image view
        imageView.setOnClickListener {
            toggleImageSize()
        }

        tvHeader.setOnClickListener {
            if (doubleClick!!) {
                Toast.makeText(applicationContext, Constants.versionDisplay, Toast.LENGTH_SHORT).show()
            }
            doubleClick = true
            Handler().postDelayed({ doubleClick = false }, 500)
        }

        // Initialize the ActivityResultLauncher
        loginActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Handle the result here from Login.
                val data: Intent? = result.data
                // Process the data from the LoginActivity as required
                if (data != null) {
                    name = data.getStringExtra("name").toString()
                    level = data.getIntExtra("level", 0) // Use the default value (0) if the "level" extra is not found

                    tvUser.text = name
//                    Toast.makeText(this, "User level: $level", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun fillLocationsArray() {

        try {
            viewModel.getLocations(uniqueId)
            viewModel.locationsResponse.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    val locations = response.body()
                    if (locations != null && locations.isNotEmpty()) {
                        // Extract location names from the API response
                        locationNamesList = locations.mapNotNull { it.location }.toMutableList()
                    } else {
                        Toast.makeText(this, "Response body is empty", Toast.LENGTH_SHORT).show()
                        fillLocationDefault()
                    }
                } else {
                    Toast.makeText(this, "Failed to fetch locations", Toast.LENGTH_SHORT).show()
                    fillLocationDefault()
                }
            })
        }catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            fillLocationDefault()
        }
    }

    private fun fillLocationDefault(){
        val defaultLocationNames = resources.getStringArray(R.array.location_default_array)

        // Update the locationNamesList with the retrieved or default values
        locationNamesList.clear()
        locationNamesList.addAll(defaultLocationNames)
        Toast.makeText(baseContext, "Update done", Toast.LENGTH_SHORT).show()
    }

    private fun updateStringArrayResource(resourceId: Int, updatedArray: Array<String>) {
        val resources = resources
        val resField = resources.javaClass.getDeclaredField("mStringArray")
        resField.isAccessible = true
        val resArray = resField.get(resources) as Array<Array<String>>
        resArray[resourceId] = updatedArray
    }

    private fun showPlateInputDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_plate_input, null)
        val editTextPlate = dialogView.findViewById<EditText>(R.id.editTextPlate)

        // Set the input type to numerical keyboard
        editTextPlate.inputType = InputType.TYPE_CLASS_NUMBER

        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                val plateId = editTextPlate.text.toString()
                // Process the plate ID (You can define this function based on your requirements)
//                processPlateInput(plateId) //if you wanted to do something special with each
                processManualInput(plateId)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLocationInputDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_location_input, null)
        val autoCompleteTextViewLocation = dialogView.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewLocation)
        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locationNamesList)

        autoCompleteTextViewLocation.setAdapter(locationAdapter)
        autoCompleteTextViewLocation.threshold = 1 // Show suggestions after typing one character

        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                val location = autoCompleteTextViewLocation.text.toString()
//                processLocationInput(location) //if you wanted to do something special with each
                processManualInput(location)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateTextColor() {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - lastCommitTime

        when {
            timeDifference <= 5000L -> tvStatus.setTextColor(Color.GREEN) // Green if less than 10 seconds
            timeDifference <= 30000L -> tvStatus.setTextColor(Color.parseColor("#FFA500")) // Orange if less than 45 seconds
            else -> tvStatus.setTextColor(Color.RED) // Red for longer than 45 seconds
        }
    }

    private fun fillQueryList() {

        try {
        //Retrieve
            val adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item)

            viewModel.getSpinnerOptions(uniqueId)
            viewModel.spinnerResponse.observe(this, Observer { response ->

                Log.d("API", response.body().toString())
                Log.d("API", response.code().toString())
                Log.d("API", response.headers().toString())

                if (response.isSuccessful) {
                    val options = response.body()
                    if (options != null && options.isNotEmpty()) {
                        // Update spinner adapter with fetched options
                        for (i in 0 until options.size) {
                            // Add values to the spinner list
                            val spinnerReasons = options[i].reason ?: "N/A"
                            val spinnerNumber = options[i].value ?: 0
                            val list = spinnerListData(spinnerReasons, spinnerNumber)
                            spinnerList.add(list)

                            // Add values to the spinner adapter
                            adapter.add(options[i].reason)
                        }

                        // Set the adapter for the problemSpinner
                        problemSpinner.adapter = adapter
                    } else {
//                        Toast.makeText(this@MainActivity, "No options available", Toast.LENGTH_SHORT).show()
                        fillQueryDefault()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch options", Toast.LENGTH_SHORT).show()
                    fillQueryDefault()
                }
            })

        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            fillQueryDefault()
        }


    }

    private fun fillQueryDefault() {
        val spinnerOptions = arrayOf("Nothing is wrong", "Damaged plate", "Picture wrong", "Missing nesting", "Size wrong", "PVC scratched", "Small plate") // This needs to go to String.xml and referenced
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions)
        problemSpinner.adapter = adapter
    }


    private fun login() {
        //Change user
        val intent = Intent(this, LoginActivity::class.java)
        loginActivityResultLauncher.launch(intent)


    }

    private fun reset() {//return values to default.
        preLocation = locationValue.toString()
        stockBy = "" //has to be a better way to do it.
        query = 0
        plateValue = ""
        locationValue = ""
        plateTextView.setText(plateValue)
        locationTextView.setText(locationValue)
        lengthTextView.setText("")
        widthTextView.setText("")
        thicknessTextView.setText("")
        tvGrade.setText("")
        problemSpinner.setSelection(0)
        imageView.setImageResource(R.drawable.logoshort_removebg_preview)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myBroadcastReceiver)
    }


    private val myBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (ACTION_BARCODE_DATA == intent.action) {
                val version = intent.getIntExtra("version", 0)
                if (version >= 1) {
//                    String aimId = intent.getStringExtra("aimId");
//                    String charset = intent.getStringExtra("charset");
//                    String codeId = intent.getStringExtra("codeId");
                    val data = intent.getStringExtra("data")
                    //                    byte[] dataBytes = intent.getByteArrayExtra("dataBytes");
//                    String dataBytesStr = bytesToHexString(dataBytes);
//                    String timestamp = intent.getStringExtra("timestamp");
//                    String text = String.format(
//                            "Data:%s\n" +
//                                    "Charset:%s\n" +
//                                    "Bytes:%s\n" +
//                                    "AimId:%s\n" +
//                                    "CodeId:%s\n" +
//                                    "Timestamp:%s\n",
//                            data, charset, dataBytesStr, aimId, codeId, timestamp); // more data           Same as zebra just look
                    scanBarcode(data) //only send the scan data

                    //to check what data is scanned
                    // Toast.makeText(baseContext, data, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun scanBarcode(decodedData: String?) {
        var scannedBarcode = "0"
        if (decodedData != null) {
            scannedBarcode = decodedData
        } // Replaced with actual scanned barcode value

        val matchedLocation = findMatchingLocationScan(scannedBarcode, locationNamesList)
        when {
            matchedLocation != null -> {
                // It is a 'Location'
                locationValue = matchedLocation
                updateLocationTextView()
            }
            scannedBarcode.all { it.isDigit() } && scannedBarcode.length in 4..8 -> {
                // It is a 'Plate'
                plateValue = scannedBarcode
                retrieveDataFromDatabase(plateValue!!)
            }
            else -> {
                // Not a valid 'Location' or 'Plate'
                Toast.makeText(this, "Invalid barcode", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun toggleImageSize() {

        // Launch the ImageDialogFragment to display the enlarged image
        val fragment = ImageDialogFragment.newInstance(imageUrl2)
        fragment.show(supportFragmentManager, "ImageDialogFragment")
    }


    private fun processManualInput(enteredValue: String) {
        val matchedLocation = findMatchingLocation(enteredValue, locationNamesList)
        when {
            matchedLocation != null -> {
                // It is a 'Location'
                locationValue = matchedLocation
                updateLocationTextView()
            }
            enteredValue.all { it.isDigit() } && enteredValue.length in 4..8 -> {
                // It is a 'Plate'
                plateValue = enteredValue
                retrieveDataFromDatabase(plateValue!!)
            }
            else -> {
                // Not a valid 'Location' or 'Plate'
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun findMatchingLocation(enteredValue: String, locationNames: MutableList<String>): String? {
        for (locationName in locationNames) {
            if (enteredValue.equals(locationName, ignoreCase = true)) {
                return locationName
            }
        }
        return null
    }

    private fun findMatchingLocationScan(scannedBarcode: String, locationNames: MutableList<String>): String? {
        for (locationName in locationNames) {
            if (scannedBarcode.equals(locationName, ignoreCase = true)) {
                return locationName
            }
        }
        return null
    }

    private fun retrieveImageFromDatabase(sheetData: Datamodel) {
        //change sheetData to plate S name.
        var plate = sheetData.plateSname

        imageUrl2 = "$web/$plate.jpg"

        //picasso online image
        Picasso.get()
            .load(imageUrl2)
            .placeholder(R.drawable.loading)
            .error(R.drawable.logoshort_removebg_preview)
            .into(imageView)
    }

    private fun defaultImage() {
        //Use Logo when it's a full plate.
        Picasso.get()
            .load(R.drawable.logoshort_removebg_preview)
            .placeholder(R.drawable.loading)
            .error(R.drawable.logoshort_removebg_preview)
            .into(imageView)
    }



    private fun updateLocationTextView() {
        locationTextView.text = locationValue  //Look if this is not optimal
    }


    private fun retrieveDataFromDatabase(plateValue: String) {



        viewModel.getSheet(uniqueId, plateValue)


        Log.d("API", "Connecting")


        viewModel.myResponse.observe(this, Observer { response ->
            if(response.isSuccessful){

                Log.d("Main Body", response.body().toString())
                Log.d("Main Code", response.code().toString())
                Log.d("Main Headers", response.headers().toString())

                val sheetData = response.body()

                if (sheetData != null) {
                    // Location name
                    val location = sheetData.locationValue ?: ""

                    // Plate ID
                    val plateValue = plateValue

                    // Length of plate
                    val length = sheetData.length ?: ""

                    // Width of plate
                    val width = sheetData.width ?: ""

                    // Thickness of plate
                    val thickness = sheetData.thickness ?: ""

                    // Grade of plate
                    val grade = sheetData.grade ?: ""

                    // Name of the sheet for picture retrieval use only
                    val plateSname = sheetData.plateSname ?: ""

                    // What is known to be wrong with the plate
                    val query = sheetData.query ?: 0

                    // What is known to be wrong with the plate
                    val stBy = sheetData.stockBy ?: ""

                    // What is known to be wrong with the plate
                    val stDate = sheetData.stockDate ?: ""

                    // What is known to be wrong with the plate
                    val fullPlate = sheetData.fullPlate ?: 0

                    // Update the sheetData variable with the new Datamodel instance
                    this.sheetData = Datamodel(location, plateValue, length, width, thickness, grade, plateSname, query, stBy, stDate, fullPlate)
                    //this.sheetData = Datamodel(location, plateValue, length, width, thickness, grade, plateSname, query)

                    locationLast = sheetData.locationValue
                    prePlate = plateValue
                    lastQuery = sheetData.query
                    data = 1
                    plateTextView.text = plateValue
                    useData(sheetData)
                    if(fullPlate != 1){ //Can change into when{} if fullPlate has more values.
                    retrieveImageFromDatabase(sheetData)}else{
                        defaultImage()
                    }
                    locationValue = location
                    locationTextView.text = location
                    stockBy = stBy
                    stockDate = stockDate

                    // Check if stock has been done
                    if (stockBy.isNullOrEmpty()) {
                        // Remove border
                        imageView.setBackgroundResource(0) // or set it to another drawable resource if needed
                    } else {
                        // Set green border
                        imageView.setBackgroundResource(R.drawable.stocktakeborder)
                    }

                    if (preLocation != location ) {
                        locationTextView.apply {
                            textSize = 20f // 20sp
                            setTextColor(ContextCompat.getColor(context, R.color.red))
                            setTypeface(null, Typeface.BOLD)
                        }
                    } else {
                        // Reset the style to default if the location is the same
                        locationTextView.apply {
                            textSize = 16f // Reset to default text size
                            setTextColor(ContextCompat.getColor(context, R.color.grey)) // Reset to default color
                            setTypeface(null, Typeface.NORMAL) // Reset to normal typeface
                        }
                    }

                    //take the stock take by data. Global identifier and intent it to the activity

                }else {
                    // Handle the case where the response body is empty
                    Toast.makeText(baseContext, "Plate does not exist. Please retry.", Toast.LENGTH_SHORT).show()
                    // You may take appropriate actions here, such as showing an error message or redirecting the user.
                }


            }else {
                Toast.makeText(baseContext, "ERROR", Toast.LENGTH_SHORT).show()
                //decide if you want a fail page and create one.
//                val intent = Intent(baseContext, ConnFailed::class.java)
//                startActivity(intent)
//                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
//                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        })



        // Perform database retrieval for 'Plate' using the plateValue
        // Retrieve Length, Width, Thickness, and Picture data




        // Update the UI with retrieved data

    }

    private fun useData(sheetData: Datamodel) {

        lengthTextView.setText(sheetData.length)
        widthTextView.setText(sheetData.width)
        thicknessTextView.setText(sheetData.thickness)
        tvGrade.setText(sheetData.grade)
    }

//    fun isInputValid(userInput: String): Boolean {
//        val pattern = """^([a-zA-Z].{0,3}|[0-9].{3,7})$""".toRegex()
//        return userInput.matches(pattern)
//    }


    //Below services to make scanner work


    override fun onResume() {
        super.onResume()
        registerReceiver(myBroadcastReceiver, IntentFilter(ACTION_BARCODE_DATA))
        claimScanner()
        updateTextColor()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(myBroadcastReceiver)
        releaseScanner()
    }

    private fun claimScanner() {
        val properties = Bundle()
        properties.putBoolean("DPR_DATA_INTENT", true)
        properties.putString("DPR_DATA_INTENT_ACTION", ACTION_BARCODE_DATA)
        //change profile properties here,  properties define are in the Constant field value html
//        properties.putBoolean("DEC_CODE39_ENABLED", true);
//        properties.putInt("DPR_DATA_INTENT", 5);
//        properties.putBoolean("DEC_EAN13_ENABLED", true);
        sendBroadcast(
            Intent(ACTION_CLAIM_SCANNER)
                .setPackage("com.intermec.datacollectionservice")
                .putExtra(EXTRA_SCANNER, "dcs.scanner.imager")
                .putExtra(EXTRA_PROFILE, "Default") // try Default if it doesn't work.
                .putExtra(EXTRA_PROPERTIES, properties)
        )
    }

    private fun releaseScanner() {
        sendBroadcast(
            Intent(ACTION_RELEASE_SCANNER)
                .setPackage("com.intermec.datacollectionservice")
        )
    }

}