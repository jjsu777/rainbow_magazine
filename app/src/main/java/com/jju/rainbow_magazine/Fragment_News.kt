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

class Fragment_News : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsList: ArrayList<RecyclerView_news_item> = fetchNewsFromDB()

        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = RecyclerView_news_Adapter(requireContext(), newsList)

        recyclerView = view.findViewById<RecyclerView>(R.id.rv_news_profile).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val fab: FloatingActionButton = view.findViewById(R.id.add_news)

        val username = SharedPreferencesUtil.getUsername(requireContext())

        val dbHelper = DBHelper(requireContext())
        if (username != null && dbHelper.checkAdmin(username)) {
            fab.visibility = View.VISIBLE
        } else {
            fab.visibility = View.GONE
        }

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val updatedNewsList: ArrayList<RecyclerView_news_item> = fetchNewsFromDB()
                viewAdapter = RecyclerView_news_Adapter(requireContext(), updatedNewsList)
                recyclerView.adapter = viewAdapter
            }
        }

        fab.setOnClickListener {
            val intent = Intent(requireContext(), Activity_News_write::class.java)
            startForResult.launch(intent)
        }
    }


    @SuppressLint("Range")
    private fun fetchNewsFromDB(): ArrayList<RecyclerView_news_item> {
        val dbHelper = DBHelper(requireContext())
        val newsCursor = dbHelper.getAllNews()
        val newsList = ArrayList<RecyclerView_news_item>()

        while (newsCursor.moveToNext()) {
            val newswrite_id = newsCursor.getInt(newsCursor.getColumnIndex("newswrite_id"))
            val title = newsCursor.getString(newsCursor.getColumnIndex("title"))
            val fullContent = newsCursor.getString(newsCursor.getColumnIndex("content"))
            val content = if (fullContent.codePointCount(0, fullContent.length) > 50) {
                fullContent.substring(0, fullContent.offsetByCodePoints(0, 50)) + "...more"
            } else {
                fullContent
            }

            val image = newsCursor.getBlob(newsCursor.getColumnIndex("image"))
            val date = newsCursor.getString(newsCursor.getColumnIndex("date"))

            newsList.add(RecyclerView_news_item(newswrite_id, title, content, date, image))
        }

        newsCursor.close()
        return newsList
    }
}