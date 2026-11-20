package com.example.globalinfocountries.presentation.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.globalinfocountries.R
import com.example.globalinfocountries.databinding.ItemNewsBinding
import com.example.globalinfocountries.domain.model.News
import com.example.globalinfocountries.utils.isValidUri
import com.example.globalinfocountries.utils.showDate

class AdapterNews(
    private val onLinkClicked:(String)->Unit ): RecyclerView.Adapter<AdapterNews.HolderNews>()
{
    private val _news = mutableListOf<News>()

    fun submitList(list : List<News>){
        _news.clear()
        _news.addAll(list)
        notifyDataSetChanged()
    }

   inner class HolderNews(itemView: View):RecyclerView.ViewHolder(itemView){

      private val binding = ItemNewsBinding.bind(itemView)

       fun build(news :News){
           binding.tvTitleNewsId.text = news.title
           binding.tvPublishedAtNewsId.text = showDate(news.publishedAt)
           binding.tvDescriptionNewsId.text = news.description
           binding.tvAuthorAtNewsId.text = news.author
           binding.tvUrlIdItemNews.text = "more:  ${news.url.take(30)}..."
//           view photo
// تحميل الصورة مع التحقق
           loadImage(news.imageUrl)
//           onClick Link
           binding.tvUrlIdItemNews.setOnClickListener {
               onLinkClicked(news.url)
           }
       }

    private fun loadImage(imageUrl: String) {
        // دالة مستقلة لتحميل الصورة
            if (!imageUrl.isNullOrEmpty() && isValidUri(imageUrl)) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image2)
                    .centerCrop() // أو fitCenter حسب الشكل المطلوب
                    .into(binding.imgNewsId)
            } else {
                binding.imgNewsId.setImageResource(R.drawable.error_image2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderNews {
        val action = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent ,false)
        return HolderNews(action)
    }
    override fun getItemCount(): Int = _news.size

    override fun onBindViewHolder(holder: HolderNews, position: Int) {
        holder.build(_news[position])


    }
}
