package com.jju.rainbow_magazine

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Fragment_Style : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_style, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val styleList: ArrayList<RecyclerView_style_item> = fetchStyleFromDB()

        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = RecyclerView_style_Adapter(requireContext(), styleList)

        recyclerView = view.findViewById<RecyclerView>(R.id.rv_profile).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val fab: FloatingActionButton = view.findViewById(R.id.add_style)

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val updatedStyleList: ArrayList<RecyclerView_style_item> = fetchStyleFromDB()
                viewAdapter = RecyclerView_style_Adapter(requireContext(), updatedStyleList)
                recyclerView.adapter = viewAdapter
            }
        }

        fab.setOnClickListener {
            val intent = Intent(requireContext(), Activity_Style_write::class.java)
            startForResult.launch(intent)
        }
    }

    @SuppressLint("Range")
    private fun fetchStyleFromDB(): ArrayList<RecyclerView_style_item> {
        val dbHelper = DBHelper(requireContext())
        val styleCursor = dbHelper.getAllStyle()
        val styleList = ArrayList<RecyclerView_style_item>()

        while (styleCursor.moveToNext()) {
            val stylewrite_id = styleCursor.getInt(styleCursor.getColumnIndex("stylewrite_id"))
            val style_title = styleCursor.getString(styleCursor.getColumnIndex("style_title"))
            val style_content = styleCursor.getString(styleCursor.getColumnIndex("style_content"))
            val style_image = styleCursor.getBlob(styleCursor.getColumnIndex("style_image"))
            val style_date = styleCursor.getString(styleCursor.getColumnIndex("style_date"))

            styleList.add(RecyclerView_style_item(stylewrite_id, style_title, style_content, style_date, style_image))
        }

        styleCursor.close()
        return styleList
    }
}
