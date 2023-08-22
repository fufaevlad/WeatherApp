package com.example.weatherapp1221

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp1221.databinding.ActivityMainBinding
import org.json.JSONObject

const val API_KEY = "a8089af300b845ccbe3142629231008"

class MainActivity : AppCompatActivity() {



    var item0 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
    var item1 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
    var item2 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
    var item3 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
    var item4 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
    var itemList = listOf<WeatherClass>(item0, item1, item2, item3, item4)


    val adapter = itemAdapter(itemList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            getResult("Kurchatov")


        binding.recView.adapter = adapter

//        binding.weatherButton.setOnClickListener {
//            getResult("Kurchatov")
//        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getResult(name:String){
        val url = "https://api.weatherapi.com/v1/forecast.json?q=$name&days=5&key=$API_KEY"

        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                    response->
                val obj = JSONObject(response)
                val forecast = obj.getJSONObject("forecast")
                val forecastDayArray = forecast.getJSONArray("forecastday")
//                val current = obj.getJSONObject("current")
//                val currentTemp = current.getString("temp_c")
//                item0.temperature = currentTemp.toDouble()

                for (i in 0 until forecastDayArray.length()) {
                    val forecastDay = forecastDayArray.getJSONObject(i)
                    val date = forecastDay.getString("date")
                    val day = forecastDay.getJSONObject("day")
                    val avgHum = day.getDouble("avghumidity")
                    val avgTempC = day.getDouble("avgtemp_c")
                    val maxWindKph = day.getDouble("maxwind_kph")
                    val condition = day.getJSONObject("condition")
                    val conditionText = condition.getString("text")
                    val conditionIcon = condition.getString("icon")
                    itemList[i].currentDate = date
                    itemList[i].temperature = avgTempC
                    itemList[i].humidity = avgHum
                    itemList[i].windSpeed = maxWindKph
                    itemList[i].text = conditionText
                    itemList[i].image = conditionIcon


                    Log.d("Log","Response : $conditionIcon")
                }

                adapter.notifyDataSetChanged()
                Log.d("Log","Response : $")
            },
            {
                Log.d("Log","Volley error: $it")
            }
        )
        queue.add(stringRequest)
    }

}
