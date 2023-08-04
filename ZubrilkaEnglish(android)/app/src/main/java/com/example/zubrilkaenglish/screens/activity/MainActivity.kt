package com.example.zubrilkaenglish.screens.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.zubrilkaenglish.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
//    lateinit var navController: NavController//TODO ???

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)



//        navController=Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)//TODO ???

    }
}
