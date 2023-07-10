package layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skatespotapp.R
import com.example.skatespotapp.SkateSpot
import android.content.Intent

class SkateSpotAdapter (private var skateSpotList: List<SkateSpot>) : RecyclerView.Adapter<SkateSpotAdapter.ItemViewHolder>(){

    interface SkateSpotAdapterListener {
        fun onClick(position: Int)
    }
    private lateinit var listener : SkateSpotAdapterListener
    fun setOnItemClickListener(_listener : SkateSpotAdapterListener)
    {
        listener = _listener
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textView_Name)
        val textViewSecurityLevel: TextView = itemView.findViewById(R.id.textView_securityLevel)

        init {
            itemView.setOnClickListener {
                if (listener != null) {
                    var position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position)
                    }
                }
            } // end setOnClickListener
        } // end init
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.skatespot_recyclerview_item, parent, false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val spot = skateSpotList[position]
        holder.textViewName.text = spot.name
        holder.textViewSecurityLevel.text = "Security Level: " + spot.securityLevel
    }
    override fun getItemCount(): Int {
        return skateSpotList.size
    }
    fun setData(list: List<SkateSpot>) {
        skateSpotList = list
        notifyDataSetChanged()
    }
}