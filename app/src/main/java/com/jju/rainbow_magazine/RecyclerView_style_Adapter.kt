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

class RecyclerView_style_Adapter(
    private val context: Context,
    private val styleList: MutableList<RecyclerView_style_item>
) : RecyclerView.Adapter<RecyclerView_style_Adapter.StyleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StyleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_style, parent, false)
        return StyleViewHolder(view)
    }


    override fun onBindViewHolder(holder: StyleViewHolder, position: Int) {
        val style = styleList[position]
        holder.style_titleTextView.text = style.style_name
        holder.style_contentTextView.text = style.style_age
        holder.style_dateTextView.text = style.style_content

        val bitmap = BitmapFactory.decodeByteArray(style.style_image, 0, style.style_image.size)
        holder.style_image.setImageBitmap(bitmap)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Activity_Style_detail::class.java)
            intent.putExtra("style_id", style.style_id)
            context.startActivity(intent)
        }

        val username = SharedPreferencesUtil.getUsername(context)
        val dbHelper = DBHelper(context)
        if (username != null && dbHelper.checkAdmin(username)) {
            holder.style_deleteImageView.visibility = View.VISIBLE // 관리자일 경우 보이게 설정
        } else {
            holder.style_deleteImageView.visibility = View.GONE // 관리자가 아닐 경우 숨김
        }

        // 삭제 아이콘에 클릭 리스너 설정
        holder.style_deleteImageView.setOnClickListener {
            DBHelper(context).deleteStyle(style.style_id)
            styleList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, styleList.size)
        }
    }

    override fun getItemCount() = styleList.size

    class StyleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val style_titleTextView: TextView = view.findViewById(R.id.tv_rv_style_title)
        val style_contentTextView: TextView = view.findViewById(R.id.tv_rv_style_subtitle)
        val style_dateTextView: TextView = view.findViewById(R.id.tv_rv_style_date)
        val style_image: ImageView = view.findViewById(R.id.img_rv_style_photo)
        val style_deleteImageView: ImageView = view.findViewById(R.id.img_rv_style_delete) // 삭제 아이콘 뷰
    }
}
