package com.example.weatherapp1221

import android.content.ClipData
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp1221.databinding.DayItemBinding
import com.squareup.picasso.Picasso

class itemAdapter(private val dataList: List<WeatherClass>):RecyclerView.Adapter<itemAdapter.myVH>() {
    class myVH(private var binding: DayItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:WeatherClass){
            binding.tvTemp.text = item.temperature.toString()
            binding.textCondition.text = item.text
            binding.tvDate.text = item.currentDate
            binding.tvHumid.text = item.humidity.toString()
            binding.tvWindSpeed.text = item.windSpeed.toString()
            Picasso.get().load("https:${item.image}").into(binding.weatherIcon);
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myVH {
        val binding = DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return myVH(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: myVH, position: Int) {
        holder.bind(dataList[position])
    }
}