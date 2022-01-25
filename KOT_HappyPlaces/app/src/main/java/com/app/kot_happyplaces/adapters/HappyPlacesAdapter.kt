package com.app.kot_happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.kot_happyplaces.R
import com.app.kot_happyplaces.activities.AddHappyPlaceActivity
import com.app.kot_happyplaces.activities.MainActivity
import com.app.kot_happyplaces.database.DatabaseHandler
import com.app.kot_happyplaces.models.HappyPlaceModel
import kotlin.*


open class HappyPlacesAdapter(private val context: Context,
                              private var list: ArrayList<HappyPlaceModel>):
                              RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
            R.layout.item_happy_place, parent, false))
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]


        if (holder is MyViewHolder){
           holder.itemView//.findViewById<ImageView>(R.id.civPlaceImage)
               //.setImageURI(Uri.parse(model.image))
               .findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.civPlaceImage)
               .setImageURI(Uri.parse(model.image))
           holder.itemView.findViewById<TextView>(R.id.tvTitle).text = model.title
           holder.itemView.findViewById<TextView>(R.id.tvDescription).text = model.description

           holder.itemView.setOnClickListener {
               if (onClickListener != null){
                   onClickListener!!.onClick(position, model)
               }
           }
        }
    }

    fun removeAt(position: Int){
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])
        if (isDeleted > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)//notificar al adaptador o recyclerview los cambios siempre
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener{
        fun onClick(position: Int, model: HappyPlaceModel)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}