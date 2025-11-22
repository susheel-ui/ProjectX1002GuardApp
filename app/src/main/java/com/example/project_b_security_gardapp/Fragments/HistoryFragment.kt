package com.example.project_b_security_gardapp.Fragments

import android.content.ContentValues.TAG
import android.content.Context
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
import com.example.project_b_security_gardapp.Keywords
import com.example.project_b_security_gardapp.Adapters.VisitorListViewAdapter
import com.example.project_b_security_gardapp.databinding.FragmentHistoryBinding
import com.example.project_b_security_gardapp.viewModels.RequestsViewModel

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: VisitorListViewAdapter
    private lateinit var requestsViewModel: RequestsViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        activity?.let {
            sharedPreferences = it.getSharedPreferences(Keywords.GUARD_MY_PREFS.toString(), Context.MODE_PRIVATE)
        }

        // Initialize ViewModel
        requestsViewModel = ViewModelProvider(this)[RequestsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // ✅ Setup RecyclerView
        adapter = VisitorListViewAdapter(requireContext(), emptyList())
        binding.RecentGuest.layoutManager = LinearLayoutManager(requireContext())
        binding.RecentGuest.adapter = adapter

        // ✅ Get token from SharedPreferences
        val token = sharedPreferences.getString(Keywords.GUARD_USER_TOKEN.toString(), "").toString()
        Log.d(TAG, "HistoryFragment: Token = $token")

        // ✅ Fetch history data
        requestsViewModel.getGuestRequests(token)

        // ✅ Observe LiveData
        requestsViewModel.requestsLiveData.observe(viewLifecycleOwner) { visitors ->
            if (visitors.isNotEmpty()) {
                adapter.updateData(visitors)
//                binding.noDataText.visibility = View.GONE
                binding.RecentGuest.visibility = View.VISIBLE
            } else {
//                binding.noDataText.visibility = View.VISIBLE
                binding.RecentGuest.visibility = View.GONE
            }
        }

        // ✅ Observe loading state
        requestsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // ✅ Observe error messages
        requestsViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}
