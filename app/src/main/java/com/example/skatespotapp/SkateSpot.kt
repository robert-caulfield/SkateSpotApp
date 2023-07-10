package com.example.skatespotapp

import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

class SkateSpot (var name:String, var description : String, var securityLevel : String, var lat: Double, var lon : Double) :
    Serializable {
        constructor() : this("","","",0.0,0.0)

}