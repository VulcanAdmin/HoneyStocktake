package com.example.honeystocktake.Updates

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.honeystocktake.R
import com.example.honeystocktake.api.MainViewModel
import com.example.honeystocktake.api.Repository
import com.example.retrofittest.MainViewModelFactory
import com.example.retrofittest.utils.Constants.Companion.uniqueId


class Update() : AppCompatActivity(){

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras

        var plate: String? = null
        plate = bundle!!.getString("plate").toString()
        var plate2: String? = null
        plate2 = bundle!!.getString("plate2").toString()
        var length: Double? = null
        length = bundle!!.getString("length")?.toDouble()
        var width: Double? = null
        width = bundle!!.getString("width")?.toDouble()
        var location: String? = null
        location = bundle!!.getString("location").toString()
        var query: Int = 0
        query = bundle!!.getInt("query")
        var stockBy: String? = null
        stockBy = bundle!!.getString("stockBy").toString()
        var bulk: Int = 0
        bulk = bundle!!.getInt("bulk")
        var bLength: Double = 0.0
        bLength = bundle!!.getDouble("blength")
        var bWidth: Double = 0.0
        bWidth = bundle!!.getDouble("bwidth")



        if (bulk == 1){
            updateBulk(plate, plate2, location, stockBy, bLength, bWidth)
        }else {
            updatePlate(plate, length, width, location, stockBy, query)
        }

    }

    private fun updateBulk(
        plate: String,
        plate2: String,
        location: String,
        stockBy: String,
        bLength: Double,
        bWidth: Double
    ) {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.pushBulk(uniqueId, plate, plate2, location, stockBy, bLength, bWidth)

        viewModel. bulkPost.observe(this, Observer { response ->
            if(response.isSuccessful){
                try {
                    // Get the default notification sound URI
                    val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    // Create a Ringtone object
                    val ringtone: Ringtone = RingtoneManager.getRingtone(applicationContext, defaultSoundUri)
                    // Play the default notification sound
                    ringtone.play()
                } catch (e: Exception) { e.printStackTrace() }
                finish()
            }else {
                setContentView(R.layout.scary_screen)
                val tvError = findViewById<View>(R.id.tvError) as TextView
                tvError.text = response.code().toString()
            }
        })
    }

    private fun updatePlate(plate: String, length: Double?, width: Double?, location: String, stockBy: String, query: Int) {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.pushQStock(uniqueId, plate, location, stockBy , query, length, width )

        viewModel. myPost.observe(this, Observer { response ->
            if(response.isSuccessful){
                try {
                    // Get the default notification sound URI
                    val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    // Create a Ringtone object
                    val ringtone: Ringtone = RingtoneManager.getRingtone(applicationContext, defaultSoundUri)
                    // Play the default notification sound
                    ringtone.play()
                } catch (e: Exception) { e.printStackTrace() }
                finish()
            }else {
                setContentView(R.layout.scary_screen)
                val tvError = findViewById<View>(R.id.tvError) as TextView
                tvError.text = response.code().toString()
            }
        })
    }

}
