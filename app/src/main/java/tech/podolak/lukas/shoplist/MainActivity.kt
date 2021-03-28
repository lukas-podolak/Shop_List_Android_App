 package tech.podolak.lukas.shoplist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), ShopListAdapter.OnItemClickListener {
    private val shopList = ArrayList<shoplist_item>()
    private val adapter = ShopListAdapter(shopList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        loadData()
    }

    fun insertItem(view: View) {
        val index:Int = shopList.size

        if (item_text_box.text.toString() != "") {
            val newItem = shoplist_item(R.drawable.ic_baseline_check_box_outline_blank_24, item_text_box.text.toString())
            item_text_box.setText("")

            shopList.add(index, newItem)
            adapter.notifyItemInserted(index)
            saveData(index)
        } else {
            Toast.makeText(this, "No text entered.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemClick(position: Int) {
        val clickedItem: shoplist_item = shopList[position]

        if (clickedItem.imageResource != R.drawable.ic_baseline_check_box_24) {
            clickedItem.imageResource = R.drawable.ic_baseline_check_box_24
            adapter.notifyItemChanged(position)
            changeSavedData(position)
        }
        else if (clickedItem.imageResource == R.drawable.ic_baseline_check_box_24) {
            shopList.removeAt(position)
            adapter.notifyItemRemoved(position)
            removeSavedData(position)
        }
    }

    private fun saveData(position: Int) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ShopListSharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.apply {
            remove("shopList_size")
            putInt("shopList_size", shopList.size)

            remove("shopList_status_test_$position")
            remove("shopList_status_ir_$position")

            val item: shoplist_item = shopList[position]
            putString("shopList_status_test_$position", item.test)
            putInt("shopList_status_ir_$position", item.imageResource)
        }.apply()

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
    }

    private fun changeSavedData(position: Int) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ShopListSharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.apply {
            remove("shopList_status_test_$position")
            remove("shopList_status_ir_$position")

            val item: shoplist_item = shopList[position]
            putString("shopList_status_test_$position", item.test)
            putInt("shopList_status_ir_$position", item.imageResource)
        }.apply()

        Toast.makeText(this, "Data changed", Toast.LENGTH_SHORT).show()
    }

    private fun removeSavedData(position: Int) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ShopListSharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.apply {
            remove("shopList_size")
            putInt("shopList_size", shopList.size)

            remove("shopList_status_test_$position")
            remove("shopList_status_ir_$position")
        }.apply()

        Toast.makeText(this, "Data removed", Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ShopListSharedPref", Context.MODE_PRIVATE)
        val savedShopListSize: Int = sharedPreferences.getInt("shopList_size", 0)

        if (savedShopListSize != 0) {
            for (i in 0 until savedShopListSize) {
                val index: Int = i
                shopList.add(index, shoplist_item(sharedPreferences.getInt("shopList_status_ir_$index", 0), sharedPreferences.getString("shopList_status_test_$index", null).toString()))
                adapter.notifyItemInserted(index)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}