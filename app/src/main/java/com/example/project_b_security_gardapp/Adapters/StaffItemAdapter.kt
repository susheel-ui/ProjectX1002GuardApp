package com.example.project_b_security_gardapp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project_b_security_gardapp.AttendanceActivity
import com.example.project_b_security_gardapp.api.Responses.ResponseStaffArrayItem
import com.example.project_b_security_gardapp.databinding.StaffListLayoutBinding
import kotlin.contracts.contract

class StaffItemAdapter(private val context: Context, val list: List<ResponseStaffArrayItem>) : RecyclerView.Adapter<StaffItemAdapter.ViewHolder>() {
    class ViewHolder(private val binding: StaffListLayoutBinding,val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entity:ResponseStaffArrayItem){
            binding.tvStaffName.text = entity.name.toString()
            binding.tvStaffId.text = entity.staffCode.toString()
            binding.btnMarkAttendance.setOnClickListener {
                Toast.makeText(binding.root.context, "Attendance Marked", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, AttendanceActivity::class.java)
                intent.putExtra("staff_code",entity.staffCode.toString())
                 context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StaffListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return ViewHolder(binding,context)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }
}