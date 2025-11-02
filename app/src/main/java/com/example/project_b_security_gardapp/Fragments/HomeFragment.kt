package com.example.project_b_security_gardapp.Fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ocx_1002_uapp.Adapters.VisitorListViewAdapter
import com.example.project_b_security_gardapp.Keywords
import com.example.project_b_security_gardapp.R
import com.example.project_b_security_gardapp.api.Entities.User
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.FragmentHomeBinding
import com.example.project_b_security_gardapp.viewModels.Fragments.HomeFragment.HomeViewModelFactory
import com.example.project_b_security_gardapp.viewModels.Fragments.HomeFragment.ViewModelHomeFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: VisitorListViewAdapter
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: ViewModelHomeFragment
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val retrofit = RetrofitInstance.getInstance
        val userServices = retrofit.create(UserServices::class.java)
        val userRepository = UserRepository(userServices)
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(userRepository)
        ).get(ViewModelHomeFragment::class.java)
        activity?.let {
            sharedPreferences =
                it.getSharedPreferences(Keywords.MYPREFS.toString(), Context.MODE_PRIVATE)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val token = sharedPreferences.getString(Keywords.USERTOKEN.toString(), "").toString()
        Log.d(TAG, "onCreateView: HomeFramgnet : $token")
        viewModel.getUserInfo(token)


        viewModel.data.observe(viewLifecycleOwner){
//            binding.societyNameTV.text = it
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // TODO:: Here Recycler View will update
        try {
            adapter = VisitorListViewAdapter(requireContext(), listOf("Kapil", "Vipin", "Aman"))
            binding.RecentGuest.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
            binding.RecentGuest.adapter = adapter


            viewModel.getUserInfo("")

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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}