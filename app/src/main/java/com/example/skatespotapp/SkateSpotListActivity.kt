package com.example.skatespotapp

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import layout.SkateSpotAdapter

class SkateSpotListActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private lateinit var db : FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

    private lateinit var skateSpotRecyclerView: RecyclerView
    private lateinit var data : ArrayList<SkateSpot>
    private lateinit var skateSpotAdapter: SkateSpotAdapter

    lateinit var dataActivityLauncher : ActivityResultLauncher<Intent>

    var filter = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skate_spot_list)

        db = FirebaseFirestore.getInstance()

        skateSpotRecyclerView = findViewById<RecyclerView>(R.id.recyclerView_SkateSpotList)
        skateSpotRecyclerView.layoutManager = LinearLayoutManager(this)
        data = ArrayList<SkateSpot>()
        skateSpotAdapter = SkateSpotAdapter(data)
        var curcontext = this
        skateSpotAdapter.setOnItemClickListener(object: SkateSpotAdapter.SkateSpotAdapterListener {
            override fun onClick(position: Int) {
                var spot = data[position]
                val intent = Intent(curcontext, SkateSpotDetailActivity::class.java)
                intent.putExtra("spot",spot)
                startActivity(intent)
            }
        })

        skateSpotRecyclerView.adapter = skateSpotAdapter

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.registerOnSharedPreferenceChangeListener(this)

        dataActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result : ActivityResult ->
            if(result.resultCode == RESULT_OK){
                val resultIntent: Intent? = result.data
                if(resultIntent != null){
                    val return_filter = resultIntent.getStringExtra("filter")
                    if(return_filter != null){
                        filter = return_filter
                    }


                }
                refreshList()
            }
        }

    }
    override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String) {
        // Not Used Because Changes don't effect current activity
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    fun refreshList(){
        var query1 = db.collection("hwk4skatespots")
        if(filter != ""){
            query1.whereEqualTo("securityLevel",filter).get().addOnSuccessListener { task->
                if(task!=null){//success
                    data.clear()
                    for(doc in task.documents){
                        var skatespot = doc.toObject(SkateSpot::class.java)
                        if(skatespot != null){
                            data.add(skatespot)
                            println(skatespot.name)
                        }
                    }
                    refreshRecycleView()
                }
            }
        }else{
            query1.get().addOnSuccessListener { task->
                if(task!=null){//success
                    data.clear()
                    for(doc in task.documents){
                        var skatespot = doc.toObject(SkateSpot::class.java)
                        if(skatespot != null){
                            data.add(skatespot)
                            println(skatespot.name)

                        }
                    }
                    refreshRecycleView()
                }
            }
        }


    }
    fun refreshRecycleView(){
        skateSpotAdapter.setData(data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_skatespotlist, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menuitem_add_entry -> {
                val i = Intent(this, AddSpotActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.menuitem_set_filter -> {
                val i = Intent(this, FilterActivity::class.java)
                dataActivityLauncher.launch(i)
                return true
            }
            R.id.menuitem_settings -> {
                val i = Intent(this, UserPreferenceActivity::class.java)
                startActivity(i)
                //star_filter = 0.0
                //refreshList()
                return true
            }
            else -> {false}
        }


    }

}