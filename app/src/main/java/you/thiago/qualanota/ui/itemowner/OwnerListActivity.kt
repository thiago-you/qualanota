package you.thiago.qualanota.ui.itemowner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.qualanota.R
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemOwner

class OwnerListActivity : AppCompatActivity(), ItemOwnerAdapter.AdapterItemOwnerClickListener {

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerview) }
    private val fabNewItemAction: FloatingActionButton by lazy { findViewById(R.id.fabNewItemAction) }

    private val newItemOwnerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setupList()
        }
    }

    private val editItemOwnerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setupList()
            setResult(Activity.RESULT_OK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_owner_list)

        setupToolbar()
        setupInterface()
        setupList()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupInterface() {
        fabNewItemAction.setOnClickListener {
            newItemOwnerResult.launch(Intent(this, NewItemOwnerActivity::class.java))
        }
    }

    private fun setupList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = Database.get().itemOwnerDao().getAll()

            lifecycleScope.launch(Dispatchers.Main) {
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@OwnerListActivity)
                    isNestedScrollingEnabled = false
                    setHasFixedSize(false)
                    adapter = ItemOwnerAdapter(list, this@OwnerListActivity)
                }
            }
        }
    }

    override fun onAdapterClick(itemOwner: ItemOwner) {
        val intent = Intent(this, EditItemOwnerActivity::class.java).apply {
            putExtra("id", itemOwner.id)
            putExtra("name", itemOwner.name)
            putExtra("location", itemOwner.location)
            putExtra("description", itemOwner.description)
        }

        editItemOwnerResult.launch(intent)
    }
}
