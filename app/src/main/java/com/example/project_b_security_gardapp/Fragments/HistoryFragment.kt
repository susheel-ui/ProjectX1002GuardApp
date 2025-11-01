package com.example.project_b_security_gardapp.Fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ocx_1002_uapp.Adapters.VisitorListViewAdapter
import com.example.project_b_security_gardapp.R
import com.example.project_b_security_gardapp.databinding.FragmentHistoryBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var fragmentBinding: FragmentHistoryBinding
    private lateinit var adapter: VisitorListViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentBinding = FragmentHistoryBinding.inflate(layoutInflater)
        return fragmentBinding.root
    }

    override fun onStart() {
            super.onStart()
        // TODO:: Here Recycler View will update
        try {
            adapter = VisitorListViewAdapter(requireContext(), listOf("Kapil","Vipin","Aman","Kapil","Vipin","Aman"))
            fragmentBinding.RecentGuest.layoutManager  = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            fragmentBinding.RecentGuest.adapter = adapter

        } catch (e: Exception) {
            Log.d(TAG, "onCreate: ${e.message}")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}