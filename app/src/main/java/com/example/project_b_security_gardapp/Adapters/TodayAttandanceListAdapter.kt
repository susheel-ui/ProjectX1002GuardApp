package com.example.project_b_security_gardapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_b_security_gardapp.api.Responses.TodayStaffEntity
import com.example.project_b_security_gardapp.databinding.AttandanceCheckinCheckoutLayoutBinding
import com.example.project_b_security_gardapp.viewModels.StaffAttendanceViewModel


class TodayAttandanceListAdapter(private val context: Context, private val list: List<TodayStaffEntity>,private val viewModel: StaffAttendanceViewModel,private val token:String) :RecyclerView.Adapter<TodayAttandanceListAdapter.ViewHolder>(){
    class ViewHolder(private val binding: AttandanceCheckinCheckoutLayoutBinding,private val viewModel: StaffAttendanceViewModel,private val token: String) : RecyclerView.ViewHolder(binding.root) {
            fun bind(entity: TodayStaffEntity){
                binding.tvStaffName.text = entity.staffName
                binding.tvStaffId.text = entity.staffCode.toString()

                if(entity.checkInTime != null){
                    binding.chipStatus.text = "Checked In"
                    binding.btnToggleAttendance.text  = "Check Out"
                }else{
                    binding.chipStatus.text = "Not Checked In"
                    binding.btnToggleAttendance.text  = "Check In"
                }
                if(entity.checkOutTime != null){
                    binding.chipStatus.text = "Completed"
                   binding.btnToggleAttendance.visibility = View.GONE
                }

                binding.btnToggleAttendance.setOnClickListener {
                    val operationDecider = binding.btnToggleAttendance.text
                    if(operationDecider.equals("Check Out")){
                        //check out
                        viewModel.checkOut(entity.id, token)
                    }else{
                        //check in
                        viewModel.checkIn(entity.id, token)
                    }
                }


            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AttandanceCheckinCheckoutLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding,viewModel,token)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bind(list.get(position))
    }

}