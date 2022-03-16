package com.pack.kotapp

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.util.concurrent.Executors

//import org.json.JSONObject

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()
    }

    private fun getData()
    {
        val queue = Volley.newRequestQueue(this)
        val url = "https://assessment.api.vweb.app/rides"
        val stringReq = StringRequest(Request.Method.GET, url,
            { response ->
            Log.e("response",response.toString())
            val data = response.toString()
            var jArray = JSONArray(data)
            Log.e("Array:",jArray.toString())

            for(i in 0..jArray.length()-1)
            {
                var jobject = jArray.getJSONObject(i)
                Log.e("jobject",jobject.toString())
                var rid = jobject.getInt("id")
                //Log.d("id",rid.toString())
                var oscid = jobject.getInt("origin_station_code")
                var spath = jobject.getString("station_path")
                //Log.d("id",spath.toString())
                var dscid = jobject.getInt("destination_station_code")
                var did = jobject.getString("date")
                var murl = jobject.getString("map_url")

                val imageView = findViewById<ImageView>(R.id.img2) //id of image inside the card

                val executor = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())
                var image: Bitmap? = null

                executor.execute {

                    try
                    {
                        val `in` = java.net.URL(murl).openStream()
                        image = BitmapFactory.decodeStream(`in`)

                        handler.post {
                            imageView.setImageBitmap(image)
                        }
                    }

                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                }

                var sid = jobject.getString("state")
                var cid = jobject.getString("city")
            }

            },
            {  })
        queue.add(stringReq)
    }
}