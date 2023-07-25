package com.jju.rainbow_magazine

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Fragment_Shop : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

                val shopList: ArrayList<RecyclerView_shop_item> = fetchShopFromDB()

                viewManager = GridLayoutManager(requireContext(), 2)
                viewAdapter = RecyclerView_shop_Adapter(requireContext(), shopList)

                recyclerView = view.findViewById<RecyclerView>(R.id.rv_shop_profile).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }

                val fab: FloatingActionButton = view.findViewById(R.id.add_shop)
                val username = SharedPreferencesUtil.getUsername(requireContext())

                val dbHelper = DBHelper(requireContext())
                if (username != null && dbHelper.checkAdmin(username)) {
                    fab.visibility = View.VISIBLE
                } else {
                    fab.visibility = View.GONE
                }
                val startForResult =
                    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            val updatedShopList: ArrayList<RecyclerView_shop_item> =
                                fetchShopFromDB()
                            viewAdapter =
                                RecyclerView_shop_Adapter(requireContext(), updatedShopList)
                            recyclerView.adapter = viewAdapter
                        }
                    }

                fab.setOnClickListener {
                    val intent = Intent(requireContext(), Activity_Shop_write::class.java)
                    startForResult.launch(intent)
                }
            }

            @SuppressLint("Range")
            private fun fetchShopFromDB(): ArrayList<RecyclerView_shop_item> {
                val dbHelper = DBHelper(requireContext())
                val shopCursor = dbHelper.getAllShop()
                val shopList = ArrayList<RecyclerView_shop_item>()

                while (shopCursor.moveToNext()) {
                    val shopwrite_id = shopCursor.getInt(shopCursor.getColumnIndex("shopwrite_id"))
                    val shop_title = shopCursor.getString(shopCursor.getColumnIndex("shop_title"))
                    val fullContent = shopCursor.getString(shopCursor.getColumnIndex("shop_content"))
                    val shop_content = if (fullContent.codePointCount(0, fullContent.length) > 30) {
                        fullContent.substring(0, fullContent.offsetByCodePoints(0, 30)) + "...more"
                    } else {
                        fullContent
                    }
                    val shop_image = shopCursor.getBlob(shopCursor.getColumnIndex("shop_image"))
                    val shop_price = shopCursor.getString(shopCursor.getColumnIndex("shop_price")) + "원"

                    shopList.add(
                        RecyclerView_shop_item(
                            shopwrite_id,
                            shop_title,
                            shop_content,
                            shop_price,
                            shop_image
                        )
                    )
                }

                shopCursor.close()
                return shopList
            }
        }

