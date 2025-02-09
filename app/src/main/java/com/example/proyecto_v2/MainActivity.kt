package com.example.proyecto_v2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.wait
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    //La apiKey
    private val apiKey = "a38992d9fddc404a6b45f7eeaa3bd94a"

    //Variables que usamos luego
    lateinit var destiny : String
    lateinit var latch: CountDownLatch
    private var nombre = ""
    private var temperatura = 0.0
    private var icon_id = ""
    private var clima = ""
    private var viento = ""
    private var humedad = ""
    //Booleano para saber si el destino es valido
    private var valido : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //le ponemos nombre a los elementos de la vista con el id
        val calendario = findViewById<CalendarView>(R.id.calendarView)
        val destino = findViewById<TextView>(R.id.editTextDestino)
        val botonSiguiente = findViewById<Button>(R.id.buttonSiguiente)

        //Metodo para conseguir el dia, mes y año que seleccionas en el calendario
        calendario.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, day ->
            //Dia seleccionado
            val diaInput = day
            //Mes seleccionado. +1 porque enero es 0
            val mesInput = month + 1
            //Anno seleccionado
            val annoInput = year
        })

        botonSiguiente.setOnClickListener(){
            if(destino.text.isEmpty() || destino.text.isBlank()){
                Toast.makeText(this,"El destino no puede estar vacio",Toast.LENGTH_SHORT).show()
            }else{
                destiny = destino.text.toString()

                //Iniciamos un CountDownLatch
                latch = CountDownLatch(1)
                FetchWeatherData(destiny,apiKey)
                //Esperamos a que el CountDownLatch llegue a 0
                latch.await()
                //Comprobamos si el destino es valido
                if(valido == true){
                    //Hacemos un intent que nos manda a la siguiente pantalla que muestra al usuario los datos que ha pedido
                    val intent1 = Intent(this, Clima::class.java)
                    //Pasamos las valores que necesitamos a la siguiente pantalla
                    intent1.putExtra("Nombre",nombre)
                    intent1.putExtra("Temperatura",temperatura)
                    intent1.putExtra("Icon_id",icon_id)
                    intent1.putExtra("Clima",clima)
                    intent1.putExtra("Viento",viento)
                    intent1.putExtra("Humedad",humedad)
                    //Ejecutamos el intent
                    startActivity(intent1)
                }else{
                    //Hacemos un Toast que muestre un mensaje al usuario
                    Toast.makeText(this,"Introduce un destino valido",Toast.LENGTH_SHORT).show()
                }
            }


        }

    }

    override fun onResume() {
        super.onResume()
        valido = false
    }

    override fun onRestart() {
        super.onRestart()
        valido = false
    }

    private fun FetchWeatherData(cityName: String, apiKEY : String) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKEY
        Log.d("Output",url.toString())
        val ex = Executors.newSingleThreadExecutor();
        ex.execute {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            try {
                val response: Response = client.newCall(request).execute()
                val result = response.body?.string()
                nombre = JSONObject(result.toString()).get("name").toString()
                temperatura = JSONObject(result.toString()).getJSONObject("main").get("temp").toString().toDouble()
                icon_id = JSONObject(result.toString()).getJSONArray("weather").getJSONObject(0).get("icon").toString()
                clima = JSONObject(result.toString()).getJSONArray("weather").getJSONObject(0).get("description").toString()
                viento = JSONObject(result.toString()).getJSONObject("wind").get("speed").toString()
                humedad = JSONObject(result.toString()).getJSONObject("main").get("humidity").toString()
                if(JSONObject(result.toString()).get("cod").toString() == "200"){
                    valido = true
                }
                //Restamos 1 al CountDownLatch
                latch.countDown()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}