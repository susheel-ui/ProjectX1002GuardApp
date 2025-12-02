package com.example.project_b_security_gardapp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_b_security_gardapp.VeiwRequestActivity
import com.example.project_b_security_gardapp.api.Entities.Entity_HomePageRequests
import com.example.project_b_security_gardapp.api.Entities.RequestsResultEntity
import com.example.project_b_security_gardapp.databinding.ActivityServicesContactBinding
import com.example.project_b_security_gardapp.databinding.VisitorListLayoutBinding

class RecentGurestAdapter(private val context:Context,private var list:List<Entity_HomePageRequests>) : RecyclerView.Adapter<RecentGurestAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: VisitorListLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Entity_HomePageRequests){
                binding.tvGuestName.text = item.guestName
                binding.tvType.text = item.status
                binding.tvFlatNumber.text = item.flatNumber
            binding.root.setOnClickListener{
                val intent = Intent(context, VeiwRequestActivity::class.java)
                intent.putExtra("id",item.id.toString())
                context.startActivity(intent)
            }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val binding =  VisitorListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    // ✅ Call this to update the list from ViewModel
    fun updateData(newList: List<Entity_HomePageRequests>) {
        list = newList
        notifyDataSetChanged()
    }
}