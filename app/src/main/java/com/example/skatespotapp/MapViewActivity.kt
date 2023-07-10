package com.example.skatespotapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var myMap : GoogleMap

    val REQUEST_CODE_GOOD = 1
    var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)


        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_GOOD)
        }


        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(p0: GoogleMap) {
        myMap = p0
        updateLocationUI()
        val spot = intent.getSerializableExtra("spot") as SkateSpot?
        if(spot != null){
            val sp = PreferenceManager.getDefaultSharedPreferences(this)
            val zoom_pref = sp.getBoolean("preference_zoom", true)
            val marker_color = sp.getString("preference_marker_color", "red")

            println(zoom_pref)
            println(marker_color)


            if(zoom_pref){
                myMap.setMinZoomPreference(10.0f)
                myMap.setMaxZoomPreference(18.0f)
            }

            addMarker(spot.lat,spot.lon,spot.name)

            var loc = LatLng(spot.lat,spot.lon)
            myMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
        }else{
            myMap.setOnMapClickListener(OnMapClickListener { point ->
                //Log.d("DEBUG", "Map clicked [" + point.latitude + " / " + point.longitude + "]")

                myMap.clear()
                addMarker(point.latitude, point.longitude, "New Location")

                val resultIntent = Intent()
                resultIntent.putExtra("lat", point.latitude)
                resultIntent.putExtra("lon", point.longitude)
                setResult(RESULT_OK, resultIntent)
                //finish()
            })
        }

    }

    fun addMarker(lat : Double, lon : Double, title : String){
        var loc = LatLng(lat,lon)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val marker_color = sp.getString("preference_marker_color", "red")
        var color : Float
        color = when(marker_color){
            resources.getString(R.string.preferences_marker_color_green) -> BitmapDescriptorFactory.HUE_GREEN
            resources.getString(R.string.preferences_marker_color_blue) -> BitmapDescriptorFactory.HUE_BLUE
            else -> BitmapDescriptorFactory.HUE_RED
        }
        var options = MarkerOptions().position(loc).title(title)
        options.icon(BitmapDescriptorFactory.defaultMarker(color))

        myMap.addMarker(options)
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            REQUEST_CODE_GOOD -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        println("Made it this far")
        if (myMap == null) {
            return
        }

        try {
            if (locationPermissionGranted) {
                myMap?.isMyLocationEnabled = true
                myMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                myMap?.isMyLocationEnabled = false
                myMap?.uiSettings?.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}