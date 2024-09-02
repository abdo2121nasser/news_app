package com.example.myapplication.adaptors

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.myapplication.FavouriteActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ArticleListItemBinding
import com.example.myapplication.databinding.FavouriteListItemBinding
import com.example.myapplication.news_model.FavouriteModel
import com.example.myapplication.news_model.NewsModel

class FavouriteAdaptor(val activity: FavouriteActivity, val model: FavouriteModel) :
    RecyclerView.Adapter<FavouriteAdaptor.ItemHolder>() {
    class ItemHolder(var binding: FavouriteListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = FavouriteListItemBinding.inflate(activity.layoutInflater, parent, false)
        return ItemHolder(binding)
    }

    override fun getItemCount() = model.articles.size


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.title.text = model.articles[position].title
        Glide.with(holder.binding.image.context).load(model.articles[position].imageUrl)
            .error(R.drawable.baseline_broken_image_24)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.binding.image)
        holder.binding.favouriteItem.setOnClickListener {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(model.articles[position].url)
                )
            )
        }
        holder.binding.share.setOnClickListener {
            ShareCompat.IntentBuilder(activity).setType("text/plain")
                .setChooserTitle("share link with:")
                .setText(model.articles[position].url).startChooser()
        }
    }

}