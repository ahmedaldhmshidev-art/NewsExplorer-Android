package com.example.globalinfocountries.presentation.countries

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.globalinfocountries.R
import com.example.globalinfocountries.domain.model.Country

class AdapterCountry( private val onClick :(String) -> Unit): RecyclerView.Adapter<AdapterCountry.HolderCountry>()
{

    private val _country = mutableListOf<Country>()

    fun submit(list: List<Country>){
        _country.clear()
        _country.addAll(list)
        notifyDataSetChanged()
    }

   inner class HolderCountry(itemView:View):RecyclerView.ViewHolder(itemView) {

//       private val binding = ItemCardCountryBinding.bind(itemView)

       val img = itemView.findViewById<ImageView>(R.id.img_flagId_card)
       val tv_name = itemView.findViewById<TextView>(R.id.tv_nameId_card)
       val tv_capital = itemView.findViewById<TextView>(R.id.tv_capital_id_card)
       val tv_population = itemView.findViewById<TextView>(R.id.tv_populationId_card)

       fun build(country: Country){
           tv_name.text = country.name
           tv_capital.text = country.capital
           tv_population.text = country.population.toString()

           Glide.with(itemView.context).load(country.flagUrl)    .override(600, 400)
               .placeholder(R.drawable.placeholder)
               .error(R.drawable.error_image2).into(img)
           
           itemView.setOnClickListener {
               Log.d("ADAPTER_CLICK", "Clicked code: ${country.code}")

               onClick(country.code )
           }

       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCountry {
        val viewInflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_country , parent , false)
        return HolderCountry(viewInflate)
    }

    override fun getItemCount(): Int = _country.size

    override fun onBindViewHolder(holder: HolderCountry, position: Int) {
        val countryList = _country[position]
        holder.build(countryList)
    }

}