package you.thiago.qualanota.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import you.thiago.qualanota.data.model.ItemReview
import you.thiago.qualanota.ui.itemreview.EditItemReviewActivity
import you.thiago.qualanota.ui.itemreview.NewItemReviewActivity

class HomeActivity : AppCompatActivity(), ItemReviewAdapter.AdapterItemReviewClickListener {

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerview) }
    private val fabNewItemAction: FloatingActionButton by lazy { findViewById(R.id.fabNewItemAction) }

    private val newItemResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setupList()
        }
    }

    private val editItemResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setupList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupToolbar()
        setupInterface()
        setupList()
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    private fun setupInterface() {
        fabNewItemAction.setOnClickListener {
            newItemResult.launch(Intent(this, NewItemReviewActivity::class.java))
        }
    }

    private fun setupList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = Database.get().itemReviewDao().getAll()

            lifecycleScope.launch(Dispatchers.Main) {
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@HomeActivity)
                    adapter = ItemReviewAdapter(list, this@HomeActivity)
                }
            }
        }
    }

    override fun onAdapterClick(itemReview: ItemReview) {
        val intent = Intent(this, EditItemReviewActivity::class.java).apply {
            putExtra("id", itemReview.id)
            putExtra("title", itemReview.title)
            putExtra("owner", itemReview.owner)
            putExtra("rating", itemReview.rating)
            putExtra("review", itemReview.review)
        }

        editItemResult.launch(intent)
    }
}
