package com.example.zubrilkaenglish.services.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.zubrilkaenglish.utils.LOG
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader

/**
 * класс содержит методы для работы с яндекс рекламой
 */
class YandexAds private constructor(){
    companion object{
        val instanse: YandexAds by lazy { YandexAds() }
    }

    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null

    /**
     * инициализирует яндекс рекламу
     */
    fun initYandexAds(context: Context) {

        //чтото непонятное типа нельзя собирать данные и показывать их детям
//        MobileAds.setUserConsent(true)
//        MobileAds.setAgeRestrictedUser(true)

        MobileAds.initialize(context) {
            Log.d(LOG,"YandexAds: initialised")

            // Interstitial ads loading should occur after initialization of the SDK.
            // Initialize SDK as early as possible, for example in Application.onCreate or Activity.onCreate
            interstitialAdLoader = InterstitialAdLoader(context).apply {
                setAdLoadListener(object : InterstitialAdLoadListener {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitialAd = ad
                        // The ad was loaded successfully. Now you can show loaded ad.
                        Log.d(LOG,"YandexAds: onAdLoaded")
                    }

                    override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                        // Ad failed to load with AdRequestError.
                        // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                        Log.d(LOG,"YandexAds: onAdFailedToLoad")
                    }
                })
            }
            loadInterstitialAd()
        }
    }

    /**
     * подгрузка яндекс рекламы
     */
    private fun loadInterstitialAd() {
        val adRequestConfiguration = AdRequestConfiguration.Builder("R-M-7862350-1").build()
        interstitialAdLoader?.loadAd(adRequestConfiguration)
    }

    /**
     * покажет межстраничную рекламу
     */
    fun showAd(activity: Activity) {
        interstitialAd?.apply {
            setAdEventListener(object : InterstitialAdEventListener {
                override fun onAdShown() {
                    // Called when ad is shown.
                    Log.d(LOG,"YandexAds: onAdShown")
                }
                override fun onAdFailedToShow(adError: AdError) {
                    // Called when an InterstitialAd failed to show.
                    // Clean resources after Ad dismissed
                    interstitialAd?.setAdEventListener(null)
                    interstitialAd = null

                    // Now you can preload the next interstitial ad.
                    loadInterstitialAd()
                }
                override fun onAdDismissed() {
                    // Called when ad is dismissed.
                    // Clean resources after Ad dismissed
                    interstitialAd?.setAdEventListener(null)
                    interstitialAd = null

                    // Now you can preload the next interstitial ad.
                    loadInterstitialAd()
                }
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(LOG,"YandexAds: onAdClicked")
                }
                override fun onAdImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                    Log.d(LOG,"YandexAds: onAdImpression")
                }
            })
            show(activity)
        }
    }

    /**
     * необходимо вызывать при закрытии активити
     */
    fun destroyYandexAds(){
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null

        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }
}