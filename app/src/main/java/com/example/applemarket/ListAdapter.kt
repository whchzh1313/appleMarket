package com.example.applemarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ListItemBinding
import java.text.DecimalFormat

class ListAdapter (private val mItems: MutableList<ListData>) : RecyclerView.Adapter<ListAdapter.Holder>() {
    interface  ItemClick {
        fun onClick(view: View, position: Int)
    }
    interface  ItemLongClick {
        fun onLongClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null
    var itemLongClick : ItemLongClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.Holder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }


    override fun onBindViewHolder(holder: ListAdapter.Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClick?.onLongClick(it, position)
            true
        }
        // 데이터 포멧 변경
        val decimal = DecimalFormat("#,###")
        val testNum = mItems[position].price
        val priceFormat = decimal.format(testNum)
        holder.price.text = "${priceFormat}원"

        holder.place.text = mItems[position].place
        holder.image.setImageResource(mItems[position].thumb)
        holder.title.text = mItems[position].title
        holder.likeNum.text = mItems[position].like.toString()
        holder.chatNum.text = mItems[position].chat.toString()

        when (mItems[position].heart) {
            true -> holder.heart.setImageResource(R.drawable.heart_on)
            false -> holder.heart.setImageResource(R.drawable.heart_off)
        }
    }

    inner class Holder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val price = binding.listPrice
        val place = binding.listPlace
        val image = binding.listImage
        val title = binding.listTitle
        val likeNum = binding.listLikeNum
        val chatNum = binding.listChatNum
        val heart = binding.listLike
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

}