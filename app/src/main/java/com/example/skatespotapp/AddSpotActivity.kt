package com.example.skatespotapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest;
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddSpotActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

    lateinit var nameEditText: EditText
    lateinit var securitySpinner: Spinner
    lateinit var descriptionEditText: EditText



    lateinit var setLocationButton: Button
    lateinit var addSpotButton: Button

    lateinit var dataActivityLauncher : ActivityResultLauncher<Intent>

    var spot_name = ""
    var spot_securitylevel = ""
    var spot_description = ""

    var spot_lat = -1.0
    var spot_lon = -1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spot)

        db = FirebaseFirestore.getInstance()

        nameEditText = findViewById<EditText>(R.id.editText_Add_Name)
        securitySpinner = findViewById<Spinner>(R.id.spinner_Add_SecurityLevel)
        descriptionEditText = findViewById<EditText>(R.id.editText_Add_Description)

        setLocationButton = findViewById<Button>(R.id.button_SetLocation)
        addSpotButton = findViewById<Button>(R.id.button_AddSpot)

        setLocationButton.setOnClickListener{
            val intent = Intent(this, MapViewActivity::class.java)
            dataActivityLauncher.launch(intent)
        }

        addSpotButton.setOnClickListener{
            spot_name = nameEditText.text.toString()
            spot_description = descriptionEditText.text.toString()
            spot_securitylevel = securitySpinner.selectedItem.toString()
            if(spot_name.isNotEmpty() && spot_description.isNotEmpty() && spot_lat != -1.0 && spot_lon != -1.0){
                //var spot = SkateSpot(spot_name, spot_description, spot_securitylevel, spot_lat, spot_lon)
                val spot = hashMapOf(
                    "name" to spot_name,
                    "description" to spot_description,
                    "securityLevel" to spot_securitylevel,
                    "lat" to spot_lat,
                    "lon" to spot_lon
                )
                db.collection("hwk4skatespots").add(spot).addOnSuccessListener {
                    finish()
                }.addOnFailureListener{
                    Toast.makeText(this,R.string.add_error,Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, R.string.add_bad, Toast.LENGTH_SHORT).show()
            }
        }

        dataActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result : ActivityResult ->
            var success = false
            if(result.resultCode == RESULT_OK){
                val resultIntent: Intent? = result.data
                if(resultIntent != null){
                    if(resultIntent.hasExtra("lat") && resultIntent.hasExtra("lon")){
                        val lat = resultIntent.getDoubleExtra("lat", -1.0)
                        val lon = resultIntent.getDoubleExtra("lon", -1.0)
                        spot_lat = lat
                        spot_lon = lon
                        Toast.makeText(this, R.string.add_location_good, Toast.LENGTH_SHORT).show()
                        success = true

                    }
                }
            }
            if(!success){
                Toast.makeText(this, R.string.add_location_bad, Toast.LENGTH_SHORT).show()
            }
        }

    }


}