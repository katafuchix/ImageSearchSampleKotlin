package com.example.imagesearchsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imagesearchsample.ui.MainFragment
import com.squareup.okhttp.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.addToBackStack(null)
            val fragment = MainFragment.newInstance()

            // idがcontainerの領域にフラグメントを表示
            fragmentTransaction.replace(R.id.container, fragment)
            fragmentTransaction.commit()
        }
        /*
        val request = Request.Builder()
            .url("https://api.cryptowat.ch/markets/coinbase-pro/btcusd/ohlc")
            .build()
        //enqueueを使用
        client.newCall(request).enqueue(ImageDownloadCallback())
        */
    }

    /*
    inner class ImageDownloadCallback : Callback {
        override fun onFailure(request: Request?, e: IOException?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onResponse(response: Response?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    */
}
