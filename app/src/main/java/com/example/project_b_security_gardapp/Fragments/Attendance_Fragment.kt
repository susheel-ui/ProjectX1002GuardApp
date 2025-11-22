package com.example.project_b_security_gardapp.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_b_security_gardapp.Adapters.StaffItemAdapter
import com.example.project_b_security_gardapp.AttendanceActivity
import com.example.project_b_security_gardapp.Keywords
import com.example.project_b_security_gardapp.databinding.FragmentAttendanceBinding
import com.example.project_b_security_gardapp.viewModels.StaffAttendanceViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Attendance_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Attendance_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentAttendanceBinding
    lateinit var viewModel: StaffAttendanceViewModel
    lateinit var sharedPreferences: SharedPreferences
    lateinit var adapter: StaffItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity?.let {
            binding = FragmentAttendanceBinding.inflate(layoutInflater)
            sharedPreferences = it.getSharedPreferences(Keywords.GUARD_MY_PREFS.toString(),Context.MODE_PRIVATE)
            adapter  = StaffItemAdapter(requireContext(),emptyList())
            binding.rvStaffList.layoutManager  = LinearLayoutManager(requireContext())

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewModel = StaffAttendanceViewModel()
        binding.btnTodayAttendance.setOnClickListener {
            startActivity(Intent(requireContext(), AttendanceActivity::class.java))
        }


        val token = sharedPreferences.getString(Keywords.GUARD_USER_TOKEN.toString(),Keywords.GUARD_NOT_FOUND.toString())
        viewModel.getStaffs(token.toString())
        viewModel.staffAttendanceList.observe(viewLifecycleOwner){
            adapter = StaffItemAdapter(requireContext(),it)
            binding.rvStaffList.adapter = adapter
        }
        //loading alert box
        viewModel.loading.observe(viewLifecycleOwner){

        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Attendance_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Attendance_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}