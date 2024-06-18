package com.example.zubrilkaenglish.screens

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.ActivityMainBinding
import com.example.zubrilkaenglish.events.iEvent
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.repositories.MemoRepository
import com.example.zubrilkaenglish.repositories.ProfileRepository
import com.example.zubrilkaenglish.repositories.VoiceRepository
import com.example.zubrilkaenglish.services.ads.YandexAds
import com.example.zubrilkaenglish.utils.APP_EMAIL
import com.example.zubrilkaenglish.utils.APP_NAME
import com.example.zubrilkaenglish.services.apiNotification.ApiNotification
import com.example.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    //инициализируем синглетоны в первый раз они регестрируются в EventBus
    private val cardsRepository = CardsRepository.instance
    private val voiceRepository = VoiceRepository.instance
    private val memoRepository = MemoRepository.instance
    private val profileRepository = ProfileRepository.instance

    private val apiNotification = ApiNotification.instance

    private val yandexAds = YandexAds.instanse

    private lateinit var binding:ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController


    //lateinit var navController: NavController//TODO ???

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)

        //первоначальная инициализация и подгрузка рекламы
        yandexAds.initYandexAds(this)

        // Получаем NavHostFragment и NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        //navController=Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)//TODO ???

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
                R.id.profile -> {
                    println("Вызван пункт меню: R.id.profile")
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    GlobalScope.launch {
                        delay(150)
                        withContext(Dispatchers.Main){
                            try {
                                navController.navigate(R.id.action_menuFragment_to_profileInfoFragment)
                            }catch (ignore: Exception){}
                        }
                    }
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

        setEmailAndName()
    }

    /**
     * установит имя и емэил в выдвижной шторке
     */
    private fun setEmailAndName() {
        val headerView = binding.navView.getHeaderView(0)

        val username = headerView.findViewById<TextView>(R.id.username)
        val email = headerView.findViewById<TextView>(R.id.email)

        mainViewModel.profile.observe(this, Observer { profile ->

            if (profile != null) {
                username.text = profile.name
                email.text = profile.email
            } else {
                username.text = APP_NAME
                email.text = APP_EMAIL
            }
        })
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        yandexAds.destroyYandexAds()
    }

    override fun onStart() {
        super.onStart()
        //TODO там есть некоторые сетьевые запросы которые запускаются до старта активити не оч хорошо
        Log.d(LOG,"onStart FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")
        Log.d(LOG,"onStart FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")
        Log.d(LOG,"onStart FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")
        Log.d(LOG,"onStart FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        Log.d(LOG,"onStop FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")

        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun <T : Enum<T>, E : iEvent<T>> notificationApi(event: E){
        apiNotification.handleEvent(event,this) //передадим евент здесь потомучто для его показа нужен контекст
    }
    /**
     * переключит в фрагмент каталога из любого другого фрагмента
     */
    fun goToCatalog() {
        navController.popBackStack(navController.graph.startDestinationId, false)
        navController.navigate(R.id.action_menuFragment_to_catalogCardsFragment)
    }
    /**
     * переключит в фрагмент напоминаний из любого другого фрагмента
     */
    fun goToMemos() {
        navController.popBackStack(navController.graph.startDestinationId, false)
        navController.navigate(R.id.action_menuFragment_to_memoFragment)
    }
}
