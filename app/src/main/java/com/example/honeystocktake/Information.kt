package com.example.honeystocktake

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Information : AppCompatActivity(){

    private lateinit var btnReturn: Button
    private lateinit var tvID: TextView
    private lateinit var tvStockBy: TextView
    private lateinit var tvStockDate: TextView
    private lateinit var tvLocationLast: TextView
    private lateinit var tvKnownQuery: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        btnReturn = findViewById(R.id.btnReturn)
        tvID = findViewById(R.id.tvID)
        tvStockBy = findViewById(R.id.tvStockBy)
        tvStockDate = findViewById(R.id.tvStockDate)
        tvLocationLast = findViewById(R.id.tvLocationLast)
        tvKnownQuery = findViewById(R.id.tvKnownQuery)



        val bundle = intent.extras

        var plateValue: String? = null
        plateValue = bundle!!.getString("plateValue")
        var stockBy: String? = null
        stockBy = bundle!!.getString("stockBy").toString()
        var stockDate: String? = null
        stockDate = bundle!!.getString("stockDate")
        var locationLast: String? = null
        locationLast = bundle!!.getString("locationLast")
        var query: Int? = null
        query = bundle!!.getInt("query")
        var knownQuery = ""

        when (query) {
                //linked to main where it is turned into integer.
                0 -> knownQuery = "Nothing is noted"
                1 -> knownQuery = "Damaged plate"
                2 -> knownQuery = "Nested wrong"
                3 -> knownQuery = "Missing nesting"
                4 -> knownQuery = "Size wrong"
        }

        tvID.text = plateValue
        tvStockBy.text = stockBy
        tvStockDate.text = stockDate
        tvLocationLast.text = locationLast
        tvKnownQuery.text = knownQuery

        btnReturn.setOnClickListener {
            finish()
        }
    }


//    //below we attempt to go back to main activity on scan button press
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        // Check if the key event is a key down event
//        Toast.makeText(baseContext, "1", Toast.LENGTH_SHORT).show()
//        if (event != null) {
//            if (event.action == KeyEvent.ACTION_DOWN) {
//
//                Toast.makeText(baseContext, "2", Toast.LENGTH_SHORT).show()
//                // Check the keycode to identify the specific key
//                when (event.keyCode) {
//                    261 -> {
//                        // Perform the action you want when the scan button is pressed
//                        // For example, trigger a scan or any other action
//                        finish()
//                        return true // Return true to indicate that the key event was handled
//                    }
//                    // Add more cases for other key codes if needed
//                }
//            }
//        }
//        // Call super method to handle other key events
//        return super.dispatchKeyEvent(event)
//    }

}