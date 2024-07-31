package com.ascendeum.stwgamsampledemo

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var adContainerView: FrameLayout? = null
    private var uiCustomKeysMap = mapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adContainerView = findViewById(R.id.ad_view_container);
        // Sample code for GAM doc page
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) { initializationStatus ->
                val statusMap =
                    initializationStatus.adapterStatusMap
                for (adapterClass in statusMap.keys) {
                    val status = statusMap[adapterClass]
                    Log.d(
                        "MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status!!.description, status.latency
                        )
                    )
                }
                // Start loading ads here...
                // PASS KV at as a map of values E.g
                val map = mapOf("key1" to "value1", "key2" to "y", "key3" to "zz")
                loadBannerAds(map)
            }
        }
    }

    private fun loadBannerAds(map: Map<String, String>) {
        // Create a new ad view.
        val adView = AdManagerAdView(this)
        adView.adUnitId = "/6499/example/banner"
        adView.setAdSize(AdSize.BANNER)
        adContainerView?.addView(adView)
        // Start loading the ad in the background.
        val adRequest = AdManagerAdRequest.Builder()
        // E.g setting key value demo
        map.forEach { (key, value) ->
            adRequest.addCustomTargeting(key, value)
        }
        // if we have more we can set
        adRequest.addCustomTargeting("category", "stocks")
        // pass multiple values for a key as a list of strings
        adRequest.addCustomTargeting("coins", listOf("SHIB", "ETC", "BTC"))
        // Want to logs KV MAP Details
        Log.d("MyApp", adRequest.build().customTargeting.toString())
        adView.loadAd(adRequest.build())
    }
}