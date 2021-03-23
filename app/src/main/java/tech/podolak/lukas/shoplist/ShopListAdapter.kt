package tech.podolak.lukas.shoplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.shoplist_item.view.*

class ShopListAdapter(private val shopListList: List<shoplist_item>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shoplist_item, parent, false)

        return ShopListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val currentItem = shopListList[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView.text = currentItem.test
    }

    override fun getItemCount() = shopListList.size

    inner class ShopListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.image_view
        val textView: TextView = itemView.item_text

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position:Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}