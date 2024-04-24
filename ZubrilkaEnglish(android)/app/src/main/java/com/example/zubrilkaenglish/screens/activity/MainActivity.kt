package com.example.zubrilkaenglish.screens.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.ActivityMainBinding
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.repositories.MemoRepository
import com.example.zubrilkaenglish.repositories.VoiceRepository
import com.example.zubrilkaenglish.screens.ApiNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    //инициализируем синглетоны в первый раз они регестрируются в EventBus
    private val cardsRepository = CardsRepository.instance
    private val voiceRepository = VoiceRepository.instance
    private val memoRepository = MemoRepository.instance

    private lateinit var binding:ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var apiNotification: ApiNotification
    private lateinit var navController: NavController
//    lateinit var navController: NavController//TODO ???

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)

        apiNotification = ApiNotification.instance

        // Получаем NavHostFragment и NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
//        navController=Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)//TODO ???



        // Создание ActionBarDrawerToggle для управления выдвижной шторкой
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Установка слушателя для элементов меню в NavigationView
        binding.navView.setNavigationItemSelectedListener {menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    println("Вызван пункт меню: R.id.nav_home")
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
                R.id.nav_server -> {
                    println("Вызван пункт меню: R.id.nav_server")
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
                R.id.nav_settings -> {
                    println("Вызван пункт меню: R.id.nav_settings")
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
                R.id.nav_memos -> {
                    println("Вызван пункт меню: R.id.nav_memos")
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    GlobalScope.launch {
                        delay(150)
                        withContext(Dispatchers.Main){
                            try {
                                navController.navigate(R.id.action_menuFragment_to_memoFragment)
                            }catch (ignore: Exception){}
                        }
                    }
                    false
                }
                else -> false
            }
        }
    }
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
