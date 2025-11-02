package com.example.project_b_security_gardapp.Fragments

import android.annotation.SuppressLint
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
import com.example.project_b_security_gardapp.Keywords
import com.example.project_b_security_gardapp.R
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.FragmentUserBinding
import com.example.project_b_security_gardapp.viewModels.Fragments.HomeFragment.ViewModelHomeFragment
import com.example.project_b_security_gardapp.viewModels.Fragments.UserFragment.UserFragmentViewModelFactory
import com.example.project_b_security_gardapp.viewModels.Fragments.UserFragment.ViewModelUserFragment

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    lateinit var bindingUserFragment: FragmentUserBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModelUserFragment: ViewModelUserFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        activity?.let {
            sharedPreferences =
                it.getSharedPreferences(Keywords.MYPREFS.toString(), Context.MODE_PRIVATE)
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        bindingUserFragment = FragmentUserBinding.inflate(layoutInflater)
        val retrofit = RetrofitInstance.getInstance
        val userServices = retrofit.create(UserServices::class.java)
        val repo = UserRepository(userServices)
        val token =
            sharedPreferences.getString(Keywords.USERTOKEN.toString(), Keywords.NOTFOUND.toString())
        viewModelUserFragment = ViewModelProvider(
            this,
            UserFragmentViewModelFactory(repo)
        ).get(ViewModelUserFragment::class.java)

        if (!token.equals(Keywords.NOTFOUND.toString())) {
            viewModelUserFragment.getData(token.toString())
        }else{
            Log.d(TAG, "onCreateView: Something wrong with token Token = $token")
        }

        viewModelUserFragment.data.observe(viewLifecycleOwner){
//            bindingUserFragment.GardIdTV.text = "GardId : ".plus(it.id.toString())
            bindingUserFragment.UserNameTV.text = it.fullName
            bindingUserFragment.mobileNumberTV.text = it.phoneNumber
            bindingUserFragment.societyNameTV.text = it.societyName
            bindingUserFragment.GardIdTV.text = "GardId :- ${it.id}"
        }
        bindingUserFragment.btnLogOut.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString(Keywords.USERTOKEN.toString(), Keywords.NOTFOUND.toString())
            editor.apply()
            activity?.finish()
        }
        return bindingUserFragment.root
    }


}