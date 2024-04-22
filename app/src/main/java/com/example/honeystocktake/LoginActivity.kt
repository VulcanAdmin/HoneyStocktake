package com.example.honeystocktake

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.honeystocktake.api.MainViewModel
import com.example.honeystocktake.api.Repository
import com.example.retrofittest.MainViewModelFactory
import com.example.retrofittest.utils.Constants

class LoginActivity : AppCompatActivity() {

    private lateinit var etPin: EditText
    private lateinit var viewModel: MainViewModel
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find views by their IDs
        etPin = findViewById(R.id.etPin)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        loadingIndicator.visibility = View.GONE
        val btnLogin: Button = findViewById(R.id.btnLogin)

        // Request focus on the EditText
        etPin.requestFocus()

        // Use a Handler to show the soft keyboard with a delay
        Handler().postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etPin, InputMethodManager.SHOW_IMPLICIT)
        }, 150) // Adjust the delay time as needed

        // Set click listener for the "Login" button
        btnLogin.setOnClickListener {
            var pin = etPin.text.toString().trim()

            if (pin.isEmpty()) {
                Toast.makeText(this, "Please enter your PIN", Toast.LENGTH_SHORT).show()
            } else {
                loadingIndicator.visibility = View.VISIBLE

                val repository = Repository()
                val viewModelFactory = MainViewModelFactory(repository)
                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
                viewModel.getUser(Constants.uniqueId, pin)


                Log.d("API", "Connecting")


                viewModel.myResponse2.observe(this, Observer { response ->
                    loadingIndicator.visibility = View.GONE
                    if(response.isSuccessful){

//                        Log.d("Main", response.body().toString())
//                        Log.d("Main", response.code().toString())
//                        Log.d("Main", response.headers().toString())

                        val body = response.body()

                        if (body != null && !body.username.isNullOrEmpty()) {
                            val name = body.username ?: "N/A"
                            val level = body.levelNo ?: 0

                            val resultIntent = Intent()
                            resultIntent.putExtra("name", name)
                            resultIntent.putExtra("level", level) //level is not used yet but is there to be implemnted in the future.
                            // Put any data you want to pass back to the MainActivity in the resultIntent

                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                            // Send user name to main

                        }else {
                            // Handle the case where the response body is empty
                            Thread.sleep(1500)
                            Toast.makeText(baseContext, "User not found", Toast.LENGTH_SHORT).show()
                            // You may take appropriate actions here, such as showing an error message or redirecting the user.
                        }


                    }else {
                        Toast.makeText(baseContext, "ERROR", Toast.LENGTH_SHORT).show()

                    }
                })

            }
        }
    }
}
