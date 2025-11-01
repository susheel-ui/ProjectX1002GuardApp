package com.example.ocx_1002_uapp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.project_b_security_gardapp.databinding.VisitorListLayoutBinding

class VisitorListViewAdapter(val context: Context, val list: List<String>) : Adapter<VisitorListViewAdapter.viewHolder>() {
   class viewHolder(val binding: VisitorListLayoutBinding) : ViewHolder(binding.root){
       fun bind(name: String){
           binding.tvGuestName.text = name
       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VisitorListLayoutBinding.inflate(inflater,parent,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size // here we will return the size of the arraylist
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
            val currentGuest = list[position]
            holder.itemView.setOnClickListener {
//                context.startActivity(Intent(context, RequestActivity::class.java))
                Toast.makeText(context, "${currentGuest}", Toast.LENGTH_SHORT).show()
            }
            holder.bind(currentGuest)
    }
}