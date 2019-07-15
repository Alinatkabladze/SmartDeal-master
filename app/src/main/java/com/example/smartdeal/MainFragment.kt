package com.example.smartdeal

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.smartdeal.database.AppDatabase
import com.example.smartdeal.database.ProductFromDatabase
import org.jetbrains.anko.doAsync
import com.example.smartdeal.model.Product
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.jetbrains.anko.support.v4.uiThread
import org.jetbrains.anko.uiThread
import java.net.URL

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        doAsync {
            val json = URL("https://api.myjson.com/bins/rpwsb").readText()

            uiThread {
                d("alina", "json: $json")
                val products = Gson().fromJson(json, Array<Product>::class.java).toList()

                root.recycler_view.apply {
                    layoutManager = GridLayoutManager(activity, 2)
                    adapter = ProductsAdapter(products)
                    root.progressBar.visibility = View.GONE
                }
            }

        }

       /* doAsync {

            val db = Room.databaseBuilder(
                activity!!.applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()

            val productsFromDatabase=db.productDao().getAll()

            val products=productsFromDatabase.map {
            Product(
                it.title,
                "@mipmap/ic_launcher.png",
                it.discount,
                false
            )
            }

            uiThread {
                root.recycler_view.apply {
                    layoutManager = GridLayoutManager(activity, 2)
                    adapter = ProductsAdapter(products)
                    root.progressBar.visibility = View.GONE
                }
            }
        }*/


        val categories =
            listOf("Woman", "Man", "Kids", "Footwear", "Dresses", "Accessories", "TRF", "Pants", "Jackets", "Cosmetics")

        root.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = CategoriesAdapter(categories)
        }
        return root
    }
}