package com.example.skatespotapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SkateSpotDetailActivity : AppCompatActivity() {
    private lateinit var textViewName: TextView
    private lateinit var textViewSecurityLevel: TextView
    private lateinit var textViewDescription: TextView

    private lateinit var button_ShowLocation: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skate_spot_detail)

        textViewName = findViewById<TextView>(R.id.textView_Description_Name)
        textViewSecurityLevel = findViewById<TextView>(R.id.textView_Description_SecurityLevel)
        textViewDescription = findViewById<TextView>(R.id.textView_Description_Description)
        button_ShowLocation = findViewById<Button>(R.id.button_ShowLocation)

        val spot = intent.getSerializableExtra("spot") as SkateSpot
        textViewName.text = spot.name
        textViewSecurityLevel.text = resources.getString(R.string.security_level_pre) + spot.securityLevel
        textViewDescription.text = spot.description

        button_ShowLocation.setOnClickListener{
            val intent = Intent(this, MapViewActivity::class.java)
            intent.putExtra("spot",spot)
            startActivity(intent)
        }




    }
}