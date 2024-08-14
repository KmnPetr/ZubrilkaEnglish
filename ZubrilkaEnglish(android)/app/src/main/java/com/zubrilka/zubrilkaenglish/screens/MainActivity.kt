package com.zubrilka.zubrilkaenglish.screens

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zubrilka.zubrilkaenglish.R
import com.zubrilka.zubrilkaenglish.databinding.ActivityMainBinding
import com.zubrilka.zubrilkaenglish.events.NfEvEnum
import com.zubrilka.zubrilkaenglish.events.NotificationEvent
import com.zubrilka.zubrilkaenglish.events.iEvent
import com.zubrilka.zubrilkaenglish.repositories.CardsRepository
import com.zubrilka.zubrilkaenglish.repositories.MemoRepository
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.repositories.StatisticsRepository
import com.zubrilka.zubrilkaenglish.repositories.VoiceRepository
import com.zubrilka.zubrilkaenglish.services.ads.YandexAds
import com.zubrilka.zubrilkaenglish.utils.APP_EMAIL
import com.zubrilka.zubrilkaenglish.utils.APP_NAME
import com.zubrilka.zubrilkaenglish.services.apiNotification.ApiNotification
import com.zubrilka.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    //инициализируем синглетоны в первый раз они регестрируются в EventBus
    private lateinit var apiNotification:ApiNotification
    private lateinit var cardsRepository:CardsRepository
    private lateinit var voiceRepository:VoiceRepository
    private lateinit var memoRepository:MemoRepository
    private lateinit var profileRepository:ProfileRepository
    private lateinit var statisticsRepository:StatisticsRepository
    private lateinit var yandexAds:YandexAds

    private lateinit var binding:ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController


    //lateinit var navController: NavController//TODO ???

    override fun onCreate(savedInstanceState: Bundle?) {

        // Устанавливаем принудительно дневную тему
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)

        //первоначальная инициализация и подгрузка рекламы
//            yandexAds.initYandexAds(this) TODO временное отключение яндекс рекламы

        // Получаем NavHostFragment и NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController


        setupToggle()
        listenNavigationView()
        initRepositories()
        setListeners()
    }

    /**
     * различные слушатели
     */
    private fun setListeners() {
        binding.btnExit.setOnClickListener { finish() }
    }

    /**
     * the function will set up toggle
     */
    private fun setupToggle() {
        val toolbar: Toolbar = binding.toolbar

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val titleTextView:TextView = toolbar.getChildAt(0) as TextView
        if (titleTextView != null) {
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
//            titleTextView.setTypeface(titleTextView.typeface, Typeface.BOLD)
            titleTextView.gravity = Gravity.CENTER
        }

//        val toggle = ActionBarDrawerToggle(
//            this,
//            binding.drawerLayout,
//            binding.toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
        // Включаем кнопку "Назад"
//        supportActionBar?.setHomeAsUpIndicator(com.google.android.material.R.drawable)
//        binding.drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_info -> {
                EventBus.getDefault().post(NotificationEvent("",NfEvEnum.POPUP_INFO))
                true
            }
            R.id.action_hamburger -> {
                binding.drawerLayout.open()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /**
     * инициализирует репозитории
     */
    private fun initRepositories() {
        GlobalScope.launch{
            //инициализируем синглетоны в первый раз они регестрируются в EventBus
            apiNotification = ApiNotification.instance
            cardsRepository = CardsRepository.instance
            voiceRepository = VoiceRepository.instance
            memoRepository = MemoRepository.instance
            profileRepository = ProfileRepository.instance
            statisticsRepository = StatisticsRepository.instance
            yandexAds = YandexAds.instanse
        }
    }

    /**
     * прослушивает кнопки с навигейшен вью
     */
    private fun listenNavigationView() {
        binding.navView.setNavigationItemSelectedListener {menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    println("Вызван пункт меню: R.id.profile")
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    EventBus.getDefault().post(NotificationEvent("",NfEvEnum.GO_TO_UPSTACK))
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
                R.id.nav_memos -> {
                    println("Вызван пункт меню: R.id.nav_memos")
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    EventBus.getDefault().post(NotificationEvent("",NfEvEnum.GO_TO_UPSTACK))
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
//                R.id.nav_logout -> {
//                     finish()
//                    false
//                }
                else -> false
            }
        }
    }

    /**
     * будет изменять фон в соответствии с изменениями фрагментов
     * в функцию onStart фрагмента нужно поместить код пример: "EventBus.getDefault().post(NotificationEvent(R.drawable.bac25.toString(),NfEvEnum.CHANGE_BACKGROUND))"
     * также на первоначальную загрузку вонового изображения влияет изображение в activity_main.xml в элементе background
     */
    fun changeBackground(event: NotificationEvent) {
        binding.background.setImageResource(event.message.toInt())
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
        ApiNotification.instance.handleEvent(event,this) //передадим евент здесь потомучто для его показа нужен контекст
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
    /**
     * переключит в фрагмент таблицы рейтинга
     */
    fun goToRating() {
        navController.popBackStack(navController.graph.startDestinationId, false)
        navController.navigate(R.id.action_menuFragment_to_ratingFragment)
    }
    /**
     * переключит наверх фрагментов по стеку
     */
    fun popBackStack() {
        navController.popBackStack(navController.graph.startDestinationId, false)
    }

    /**
     * сменит титл на тулбаре по просьбе фрагментов
     */
    fun changeTitle(event: NotificationEvent) {
        binding.toolbar.setTitle(event.message)
    }
}
