package com.example.skatespotapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner

class FilterActivity : AppCompatActivity() {

    lateinit var set_filter_button : Button
    lateinit var reset_filter_button : Button
    lateinit var filter_spinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        set_filter_button = findViewById<Button>(R.id.button_set_filter)
        reset_filter_button = findViewById<Button>(R.id.button_reset_filter)
        filter_spinner = findViewById<Spinner>(R.id.spinner_filter)


        set_filter_button.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("filter", filter_spinner.selectedItem.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        reset_filter_button.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("filter", "")
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }
}