package com.example.imagesearchsample.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchsample.R
import com.example.imagesearchsample.common.ImageLoader
import com.example.imagesearchsample.databinding.CustomSearchImageBinding
import kotlin.math.roundToInt

class ImageSearchAdapter : RecyclerView.Adapter<ImageSearchAdapter.ItemViewHolder>() {

    interface OnClickListener {
        fun onClick(item: ImageSearchAdapterViewModel)
    }

    class ItemViewHolder(private val binding: CustomSearchImageBinding, private val listener: OnClickListener?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ImageSearchAdapterViewModel, screenWidth: Int) {

            if (data.url.value == null) {
                //ImageLoader.imageUrlToView(R.drawable.avatar_default_new, binding.memberImage.image)
            } else {
                ImageLoader.imageUrlToViewAsCircle(
                    data.url.value!!,
                    null,//R.drawable.avatar_default_new,
                    binding.itemImage.image
                )
            }

            binding.viewModel = data

            binding.itemImage.image.layoutParams = binding.itemImage.image.layoutParams.apply {
                this.width = ((screenWidth / 3) * 0.85).roundToInt()
                this.height = ((screenWidth / 3) * 0.85).roundToInt()
            }

            binding.card.setOnClickListener { listener?.onClick(data) }
        }
    }

    lateinit var lifecycleOwner: LifecycleOwner

    var listener: OnClickListener? = null

    var screenWidth: Int? = null

    val dataList = object : ArrayList<ImageSearchAdapterViewModel>() {
        override fun addAll(elements: Collection<ImageSearchAdapterViewModel>): Boolean {
            val result = super.addAll(elements)
            notifyDataSetChanged()
            return result
        }

        override fun clear() {
            super.clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate<CustomSearchImageBinding>(
                LayoutInflater.from(parent.context),
                R.layout.custom_search_image,
                parent,
                false
            ).apply {
                this.lifecycleOwner = this@ImageSearchAdapter.lifecycleOwner
            },
            listener
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], screenWidth!!)
    }
}
