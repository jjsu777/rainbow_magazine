package com.jju.rainbow_magazine

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerView_shop_Adapter(
    private val context: Context,
    private val shopList: MutableList<RecyclerView_shop_item>
) : RecyclerView.Adapter<RecyclerView_shop_Adapter.ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_shop_item, parent, false)
        return ShopViewHolder(view)
    }


    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shopList[position]
        holder.shop_titleTextView.text = shop.shop_title
        holder.shop_contentTextView.text = shop.shop_price
        holder.shop_dateTextView.text = shop.shop_content

        val bitmap = BitmapFactory.decodeByteArray(shop.shop_image, 0, shop.shop_image.size)
        holder.shop_image.setImageBitmap(bitmap)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Activity_Shop_detail::class.java)
            intent.putExtra("shop_id", shop.shop_id)
            context.startActivity(intent)
        }

        val username = SharedPreferencesUtil.getUsername(context)
        val dbHelper = DBHelper(context)
        if (username != null && dbHelper.checkAdmin(username)) {
            holder.shop_deleteImageView.visibility = View.VISIBLE // 관리자일 경우 보이게 설정
        } else {
            holder.shop_deleteImageView.visibility = View.GONE // 관리자가 아닐 경우 숨김
        }

        // 삭제 아이콘에 클릭 리스너 설정
        holder.shop_deleteImageView.setOnClickListener {
            DBHelper(context).deleteShop(shop.shop_id)
            shopList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, shopList.size)
        }
    }

    override fun getItemCount() = shopList.size

    class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shop_titleTextView: TextView = view.findViewById(R.id.tv_rv_shop_title)
        val shop_contentTextView: TextView = view.findViewById(R.id.tv_rv_shop_content)
        val shop_dateTextView: TextView = view.findViewById(R.id.tv_rv_shop_price)
        val shop_image: ImageView = view.findViewById(R.id.img_rv_shop_photo)
        val shop_deleteImageView: ImageView = view.findViewById(R.id.img_rv_shop_delete) // 삭제 아이콘 뷰
    }
}
