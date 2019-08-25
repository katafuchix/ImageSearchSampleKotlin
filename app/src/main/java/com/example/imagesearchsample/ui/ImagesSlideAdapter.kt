package com.example.imagesearchsample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchsample.R
import com.example.imagesearchsample.common.ImageLoader
import com.example.imagesearchsample.databinding.CustomImageSlideItemBinding

class ImagesSlideAdapter : RecyclerView.Adapter<ImagesSlideAdapter.ViewHolder>() {

    // 画面サイズ
    var screenWidth: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<CustomImageSlideItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.custom_image_slide_item,
            null,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.getSize()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this, position, screenWidth!!)
    }

    class ViewHolder(val binding: CustomImageSlideItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(adapter: ImagesSlideAdapter, position: Int, screenWidth: Int) {
            // TODO 高速でスクロールすると、Picassoのロード完了時に再利用したViewに画像が割り当てられ、意図しない画像が表示される可能性あり
            val image = adapter.itemList.getItem(position).url
            if (image != null) {
                ImageLoader.imageUrlToView(image, null, binding.memberImage.image)
            }

            binding.memberImage.image.layoutParams = binding.memberImage.image.layoutParams.apply {
                this.width = screenWidth
                this.height = screenWidth
            }
        }
    }

    val itemList = Item(this)

    class Item(private val adapter: ImagesSlideAdapter) {
        data class Data(val url: String?)

        private val item = ArrayList<Data>()
        private val selected = ArrayList<Boolean>()

        fun addItem(url: Data) {
            item.add(url)
            selected.add(false)
            adapter.notifyDataSetChanged()
        }

        fun addAllItems(urlList: List<Data>) {
            item.addAll(urlList)
            selected.addAll(urlList.map { false })
            adapter.notifyDataSetChanged()
        }

        fun getItem(position: Int): Data {
            return item[position]
        }

        fun getAllItems(): List<Data> = item

        fun getSize(): Int = item.size

        fun isSelected(position: Int): Boolean = selected[position]
    }
}
