 package tech.podolak.lukas.shoplist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
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
    }

    fun insertItem(view: View) {
        val index:Int = 0

        if (item_text_box.text.toString() != "") {
            val newItem = shoplist_item(R.drawable.ic_baseline_check_box_outline_blank_24, item_text_box.text.toString())
            item_text_box.setText("")

            shopList.add(index, newItem)
            adapter.notifyItemInserted(index)
        } else {
            Toast.makeText(this, "No text entered.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemClick(position: Int) {
        val clickedItem:shoplist_item = shopList[position]

        if (clickedItem.imageResource != R.drawable.ic_baseline_check_box_24) {
            clickedItem.imageResource = R.drawable.ic_baseline_check_box_24
            adapter.notifyItemChanged(position)
        }
        else if (clickedItem.imageResource == R.drawable.ic_baseline_check_box_24) {
            shopList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    private fun saveData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ShopListSharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.apply() {
            putInt("shopList_size", shopList.size)

            for (i in 0 .. shopList.size) {
                remove("shopList_status_test_$i")
                remove("shopList_status_ir_$i")

                putString("shopList_status_test_$i", shopList[i].test)
                putInt("shopList_status_ir_$i", shopList[i].imageResource)
            }
        }

        editor.commit()
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("ShopListSharedPref", Context.MODE_PRIVATE)
        val savedShopListSize: Int = sharedPreferences.getInt("shopList_size", 0)

        for (i in 0 .. savedShopListSize) {
            shopList[i].test = sharedPreferences.getString("shopList_status_test_$i", null).toString()
            shopList[i].imageResource = sharedPreferences.getInt("shopList_status_ir_$i", 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
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