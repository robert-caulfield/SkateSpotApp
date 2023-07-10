package com.example.skatespotapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    /*


    Firebase Login:
    hwk4@example.com
    abcdef

    my email: robert.w.caulfield@gmail.com

    About
    This is an app that allows users to share skateboarding spots.

    After signing in you are able to scroll through a list of uploaded spots.
    You can view a description of these spots and view the location in a mapview
    upon clicking the recycler view items.

    Users has the option of adding their own spots, using the mapview to assign
    the location, indicated by a marker. If location services are enabled, the user
    is able to zoom in on their current location by clicking the icon in the top
    right of the mapview.

    The settings menu allows users to change some of the features of the mapview.

     */


    //Login Views
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    // Firebase
    private lateinit var db : FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase init
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        // View init
        editTextUsername = findViewById<EditText>(R.id.editText_Username)
        editTextPassword = findViewById<EditText>(R.id.editText_Password)
        buttonLogin = findViewById<Button>(R.id.button_Login)

        buttonLogin.setOnClickListener {
            var username = editTextUsername.text.toString()
            var password = editTextPassword.text.toString()
            attemptLogin(username,password);
        };

    }

    fun attemptLogin(username:String, password:String){
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this) { task ->
            // Was the sign in successful?
            if (task.isSuccessful) {
                mAuth = FirebaseAuth.getInstance()
                val mCurrentUser = mAuth.currentUser
                val email = mCurrentUser!!.email

                Toast.makeText(this@MainActivity, R.string.login_success, Toast.LENGTH_SHORT).show()
                println("Login Success!")
                val intent = Intent(this, SkateSpotListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, R.string.login_fail, Toast.LENGTH_SHORT).show()
            }
        }

    }
}