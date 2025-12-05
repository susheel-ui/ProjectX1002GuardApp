package com.example.project_b_security_gardapp.Fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_b_security_gardapp.Adapters.RecentGurestAdapter
import com.example.project_b_security_gardapp.Keywords
import com.example.project_b_security_gardapp.Adapters.VisitorListViewAdapter
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.FragmentHomeBinding
import com.example.project_b_security_gardapp.viewModels.Fragments.HomeFragment.HomeViewModelFactory
import com.example.project_b_security_gardapp.viewModels.Fragments.HomeFragment.ViewModelHomeFragment
import com.example.project_b_security_gardapp.viewModels.RequestsViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RecentGurestAdapter
    private lateinit var viewModel: ViewModelHomeFragment
    private lateinit var requestsViewModel: RequestsViewModel
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var token :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewModels
        val retrofit = RetrofitInstance.getInstance
        val userServices = retrofit.create(UserServices::class.java)
        val userRepository = UserRepository(userServices)

        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(userRepository)
        )[ViewModelHomeFragment::class.java]

        requestsViewModel = ViewModelProvider(this)[RequestsViewModel::class.java]

        // Get SharedPreferences for token
        activity?.let {
            sharedPreferences = it.getSharedPreferences(Keywords.GUARD_MY_PREFS.toString(), Context.MODE_PRIVATE)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // ✅ Initialize RecyclerView
        try {
            adapter = RecentGurestAdapter(requireContext(), emptyList())
            binding.RecentGuest.layoutManager = LinearLayoutManager(requireContext())
            binding.RecentGuest.adapter = adapter
        } catch (e: Exception) {
            Log.d(TAG, "onCreateView: Some Issues in adapter initialization")
        }

        // ✅ Get token
        token = sharedPreferences.getString(Keywords.GUARD_USER_TOKEN.toString(), "").toString()
        Log.d(TAG, "Token from SharedPrefs: $token")

        // ✅ Fetch user profile info
        viewModel.getUserInfo(token)
        viewModel.userData.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.societyNameTV.text = it.societyName.uppercase()
                Log.d(TAG, "onCreateView: $it")
            }
        }

        // ✅ Observe visitor list
        viewModel.recentRequests.observe(viewLifecycleOwner) { visitors ->
            if (visitors.isNotEmpty()) {
                adapter.updateData(visitors) // ✅ Fixed here
            } else {
                Toast.makeText(requireContext(), "No visitors found", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Observe loading and errors
        requestsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        requestsViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requestsViewModel.getGuestRequests(token)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopLoop()
    }
}
