package com.example.weatherapp1221

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp1221.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.json.JSONObject


private lateinit var analytics: FirebaseAnalytics


const val API_KEY = "a8089af300b845ccbe3142629231008"
var city = "Gagarin"


class MainActivity : AppCompatActivity() {



    private lateinit var activityBinding: ActivityMainBinding

    var item0 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
    var item1 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
    var item2 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
//    var item3 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")
//    var item4 = WeatherClass("date",0.0,0.0, 0.0,"Condition"," ")

    var itemList = listOf<WeatherClass>(item0, item1, item2)
    val adapter = itemAdapter(itemList)

    override fun onCreate(savedInstanceState: Bundle?) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        }

        analytics = Firebase.analytics

        super.onCreate(savedInstanceState)
         activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

            getResult(city)

        activityBinding.recView.adapter = adapter
        activityBinding.renewButton.setOnClickListener {
            if(city=="Gagarin") {
                getResult("Zelenograd")
                city = "Zelenograd"
            } else if(city == "Zelenograd"){
                getResult("Gagarin")
                city = "Gagarin"
            }
        }

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

                val current = obj.getJSONObject("current")
                val tempC = current.getString("temp_c")
                val lastUpdate = current.getString("last_updated")
                val conditionToday = current.getJSONObject("condition")
                val conditionIconToday = conditionToday.getString("icon")
                val currentConditionTextPrimary = conditionToday.getString("text")
                var currentConditionText:String = translate(currentConditionTextPrimary)

                activityBinding.currantTxtCondition.text = "Сейчас в ${translate(city)} "+ currentConditionText.lowercase()
                activityBinding.tvCurTemp.text = tempC+"°C"
                activityBinding.requestTime.text = lastUpdate
                Picasso.get().load("https:${conditionIconToday}").into(activityBinding.imgConditionCurrent)

                val forecast = obj.getJSONObject("forecast")
                val forecastDayArray = forecast.getJSONArray("forecastday")

                for (i in 0 until forecastDayArray.length()) {
                    val forecastDay = forecastDayArray.getJSONObject(i)
                    val date = forecastDay.getString("date")
                    val day = forecastDay.getJSONObject("day")
                    val avgHum = day.getDouble("avghumidity")
                    val avgTempC = day.getDouble("avgtemp_c")
                    val maxWindKph = day.getDouble("maxwind_kph")
                    val condition = day.getJSONObject("condition")
                    val conditionTextPrimary = condition.getString("text")
                    var conditionText:String = translate(conditionTextPrimary)
                    val conditionIcon = condition.getString("icon")
                    itemList[i].currentDate = date
                    itemList[i].temperature = avgTempC
                    itemList[i].humidity = avgHum
                    itemList[i].windSpeed = maxWindKph
                    itemList[i].text = conditionText
                    itemList[i].image = conditionIcon


                    Log.d("Log","Response : ${obj}")
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



    fun translate(txtConditionInput:String):String{
        var txtConditionOutput:String = " "
        when(txtConditionInput){
            "Gagarin" -> txtConditionOutput = "Гагарине"
            "Zelenograd" -> txtConditionOutput = "Зеленограде"
            "Clear" -> txtConditionOutput = "Ясно"
            "Sunny" -> txtConditionOutput = "Cолнечно"
            "Partly cloudy" -> txtConditionOutput = "Переменная облачность"
            "Cloudy" -> txtConditionOutput = "Облачно"
            "Overcast" -> txtConditionOutput = "Пасмурно"
            "Mist" -> txtConditionOutput = "Дымка"
            "Patchy rain possible" -> txtConditionOutput = "Возможен кратковременный дождь"
            "Patchy snow possible" -> txtConditionOutput = "Возможен кратковременный снег"
            "Patchy sleet possible" -> txtConditionOutput = "Возможен небольшой мокрый снег"
            "Patchy freezing drizzle possible"-> txtConditionOutput = "Возможен кратковременный ледяной дождь"
            "Thundery outbreaks possible" -> txtConditionOutput = "Возможна гроза"
            "Blowing snow" -> txtConditionOutput = "Метель"
            "Blizzard" -> txtConditionOutput = "Метель"
            "Fog" -> txtConditionOutput = "Туман"
            "Freezing fog" -> txtConditionOutput = "Изморозь"
            "Patchy light drizzle"  -> txtConditionOutput = "Возможна изморось"
            "Light drizzle" -> txtConditionOutput = "Легкая изморось"
            "Freezing drizzle" -> txtConditionOutput = "Ледяной дождь"
            "Heavy freezing drizzle" -> txtConditionOutput = "Сильный ледяной дождь"
            "Patchy light rain" -> txtConditionOutput = "Возможен небольшой дождь"
            "Light rain"  -> txtConditionOutput = "Небольшой дождь"
            "Moderate rain at times"  -> txtConditionOutput = "Временами умеренный дождь"
            "Moderate rain" -> txtConditionOutput = "Умеренный дождь"
            "Heavy rain at times" -> txtConditionOutput = "Временами сильный дождь"
            "Heavy rain" -> txtConditionOutput = "Сильный дождь"
            "Light freezing rain" -> txtConditionOutput = "Небольшой ледяной дождь"
            "Moderate or heavy freezing rain" -> txtConditionOutput = "Сильный ледяной дождь"
            "Light sleet" -> txtConditionOutput = "Мокрый снег"
            "Moderate or heavy sleet" -> txtConditionOutput = "Сильный мокрый снег"
            "Patchy light snow" -> txtConditionOutput = "Возможен небольшой снег"
            "Light snow" -> txtConditionOutput = "Небольшой снег"
            "Patchy moderate snow" -> txtConditionOutput = "Возможен умеренный снег"
            "Moderate snow" -> txtConditionOutput = "Умеренный снег"
            "Patchy heavy snow" -> txtConditionOutput = "Возможен сильный снег"
            "Heavy snow" -> txtConditionOutput = "Сильный снег"
            "Ice pellets" -> txtConditionOutput = "Град"
            "Light rain shower" -> txtConditionOutput = "Легкий ливень"
            "Moderate or heavy rain shower" -> txtConditionOutput = "Сильный ливень"
            "Torrential rain shower" -> txtConditionOutput = "Тропический ливень"
            "Light sleet showers" -> txtConditionOutput = "Light sleet showers"
            "Moderate or heavy sleet showers"-> txtConditionOutput = "Moderate or heavy sleet showers"
            "Light snow showers" -> txtConditionOutput = "Небольшой снегопад"
            "Moderate or heavy snow showers" -> txtConditionOutput = "Сильный снегопад"
            "Light showers of ice pellets" -> txtConditionOutput = "Небольшой град"
            "Moderate or heavy showers of ice pellets" -> txtConditionOutput = "Сильный град"
            "Patchy light rain with thunder" -> txtConditionOutput = "Небольшой дождь с грозой"
            "Moderate or heavy rain with thunder" -> txtConditionOutput = "Сильный дождь с грозой"
            "Patchy light snow with thunder" -> txtConditionOutput = "Небольшой снег с грозой"
            "Moderate or heavy snow with thunder" -> txtConditionOutput = "Сильный снег с грозой"
        }
        return txtConditionOutput
    }
}
