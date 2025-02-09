package com.example.proyecto_v2

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.Executors


class Clima : AppCompatActivity() {

    //La apiKey
    private val apiKey = "a38992d9fddc404a6b45f7eeaa3bd94a"

    lateinit var icono : ImageView
    lateinit var icono1 : ImageView
    lateinit var icono2 : ImageView
    lateinit var icono3 : ImageView
    lateinit var icono4 : ImageView
    lateinit var icono5 : ImageView

    lateinit var clima : TextView

    lateinit var temperatura : TextView
    lateinit var temp1 : TextView
    lateinit var temp2 : TextView
    lateinit var temp3 : TextView
    lateinit var temp4 : TextView
    lateinit var temp5 : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clima)

        val nombreDestino = findViewById<TextView>(R.id.textViewPruebaDestino)
        temperatura = findViewById(R.id.textViewTemp)
        clima = findViewById(R.id.textViewClima)
        val viento = findViewById<TextView>(R.id.textViewViento)
        val humedad = findViewById<TextView>(R.id.textViewHumedad)

        icono = findViewById(R.id.imageViewIcon)
        icono1 = findViewById(R.id.imageViewDia1)
        icono2 = findViewById(R.id.imageViewDia2)
        icono3 = findViewById(R.id.imageViewDia3)
        icono4 = findViewById(R.id.imageViewDia4)
        icono5 = findViewById(R.id.imageViewDia5)

        temp1 = findViewById(R.id.textViewTemp1)
        temp2 = findViewById(R.id.textViewTemp2)
        temp3 = findViewById(R.id.textViewTemp3)
        temp4 = findViewById(R.id.textViewTemp4)
        temp5 = findViewById(R.id.textViewTemp5)
        cambiarIcono(intent.getStringExtra("Icon_id"), icono)

        nombreDestino.text = intent.getStringExtra("Nombre")
        clima.text = intent.getStringExtra("Clima")
        val vientoText = "Viento: " + intent.getStringExtra("Viento") + " km/h"
        viento.text = vientoText
        val humedadText = "Humedad: " + intent.getStringExtra("Humedad") + "%"
        humedad.text = humedadText
        val temperaturaCelsius = BigDecimal(intent.getDoubleExtra("Temperatura",0.0) - 273.15).setScale(2,RoundingMode.HALF_EVEN)
        val temperaturaText = "Temperatura: " + temperaturaCelsius + " ºC"
        temperatura.text = temperaturaText

        val url = "https://api.openweathermap.org/data/2.5/forecast?q=" + intent.getStringExtra("Nombre") + "&appid=" + apiKey
        Log.d("Output1",url.toString())
        val ex = Executors.newSingleThreadExecutor();
        ex.execute {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            try {
                val response: Response = client.newCall(request).execute()
                val result = response.body?.string()
                cambiarIcono(JSONObject(result.toString()).getJSONArray("list").getJSONObject(7).getJSONArray("weather").getJSONObject(0).get("icon").toString(), icono1)
                temp1.text = BigDecimal(JSONObject(result.toString()).getJSONArray("list").getJSONObject(7).getJSONObject("main").get("temp").toString().toDouble() - 273.15).setScale(2,RoundingMode.HALF_EVEN).toString() + " ºC"

                cambiarIcono(JSONObject(result.toString()).getJSONArray("list").getJSONObject(15).getJSONArray("weather").getJSONObject(0).get("icon").toString(),icono2)
                temp2.text = BigDecimal(JSONObject(result.toString()).getJSONArray("list").getJSONObject(15).getJSONObject("main").get("temp").toString().toDouble() - 273.15).setScale(2,RoundingMode.HALF_EVEN).toString() + " ºC"

                cambiarIcono(JSONObject(result.toString()).getJSONArray("list").getJSONObject(23).getJSONArray("weather").getJSONObject(0).get("icon").toString(),icono3)
                temp3.text = BigDecimal(JSONObject(result.toString()).getJSONArray("list").getJSONObject(23).getJSONObject("main").get("temp").toString().toDouble() - 273.15).setScale(2,RoundingMode.HALF_EVEN).toString() + " ºC"

                cambiarIcono(JSONObject(result.toString()).getJSONArray("list").getJSONObject(31).getJSONArray("weather").getJSONObject(0).get("icon").toString(),icono4)
                temp4.text = BigDecimal(JSONObject(result.toString()).getJSONArray("list").getJSONObject(31).getJSONObject("main").get("temp").toString().toDouble() - 273.15).setScale(2,RoundingMode.HALF_EVEN).toString() + " ºC"

                cambiarIcono(JSONObject(result.toString()).getJSONArray("list").getJSONObject(39).getJSONArray("weather").getJSONObject(0).get("icon").toString(),icono5)
                temp5.text = BigDecimal(JSONObject(result.toString()).getJSONArray("list").getJSONObject(39).getJSONObject("main").get("temp").toString().toDouble() - 273.15).setScale(2,RoundingMode.HALF_EVEN).toString() + " ºC"



            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }

    private fun cambiarIcono(icon_id: String?, icono1: ImageView) {
        if(icon_id == "01d" || icon_id == "01n"){
            icono1.setImageResource(R.drawable.clear_sky)
        }
        if (icon_id == "02d" || icon_id == "02n"){
            icono1.setImageResource(R.drawable.few_clouds)
        }
        if (icon_id == "03d" || icon_id == "03n"){
            icono1.setImageResource(R.drawable.scattered_clouds)
        }
        if (icon_id == "04d" || icon_id == "04n"){
            icono1.setImageResource(R.drawable.broken_clouds)
        }
        if (icon_id == "09d" || icon_id == "09n"){
            icono1.setImageResource(R.drawable.shower_rain)
        }
        if (icon_id == "10d" || icon_id == "10n"){
            icono1.setImageResource(R.drawable.rain)
        }
        if (icon_id == "11d" || icon_id == "11n"){
            icono1.setImageResource(R.drawable.thunderstorm)
        }
        if (icon_id == "13d" || icon_id == "13n"){
            icono1.setImageResource(R.drawable.snow)
        }
        if (icon_id == "50d" || icon_id == "50n"){
            icono1.setImageResource(R.drawable.mist)
        }
    }


}
