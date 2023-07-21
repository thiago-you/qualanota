package you.thiago.qualanota.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.koalaloadinglibrary.KoalaLoadingView
import you.thiago.qualanota.R
import you.thiago.qualanota.components.ConfirmPopulateDataAlert
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemOwner
import you.thiago.qualanota.data.model.ItemReview
import you.thiago.qualanota.ui.itemowner.OwnerListActivity
import you.thiago.qualanota.ui.itemreview.EditItemReviewActivity
import you.thiago.qualanota.ui.itemreview.NewItemReviewActivity

class HomeActivity : AppCompatActivity(), ItemReviewAdapter.AdapterItemReviewClickListener {

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerview) }
    private val fabNewItemAction: FloatingActionButton by lazy { findViewById(R.id.fabNewItemAction) }
    private val fabListOwnersAction: View by lazy { findViewById(R.id.listOwnersAction) }
    private val populateDataAction: View by lazy { findViewById(R.id.populateDataAction) }
    private val loadingView: KoalaLoadingView by lazy { findViewById(R.id.loading_view) }

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

    private val listItemOwnerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setupList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

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

        fabListOwnersAction.setOnClickListener {
            listItemOwnerResult.launch(Intent(this, OwnerListActivity::class.java))
        }

        populateDataAction.setOnClickListener {
            ConfirmPopulateDataAlert.show(this) {
                populateData()
            }
        }
    }

    private fun setupList() {
        recyclerView.visibility = View.INVISIBLE
        loadingView.start()

        lifecycleScope.launch(Dispatchers.IO) {
            val list = Database.get().itemReviewDao().getAll()

            Thread.sleep(2000)

            lifecycleScope.launch(Dispatchers.Main) {
                loadingView.stop()

                recyclerView.apply {
                    visibility = View.VISIBLE
                    layoutManager = LinearLayoutManager(this@HomeActivity)
                    isNestedScrollingEnabled = false
                    setHasFixedSize(false)
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

    private fun populateData() {
        recyclerView.visibility = View.INVISIBLE
        loadingView.start()

        lifecycleScope.launch(Dispatchers.IO) {
            Database.get().clearAllTables()

            populateItemReviews()
            populateItemOwners()
            setupList()

            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(this@HomeActivity, getString(R.string.data_populate_success), Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun populateItemReviews() {
        val items = listOf(
            ItemReview(
                title = "Sanduíche de Presunto",
                owner = "Dona Florinda",
                rating = 5,
                review = "QUERIA MAIS! Isso isso isso.",
            ),
            ItemReview(
                title = "Pastel sabor Pizza",
                owner = "9 Pasteis",
                rating = 3,
                review = "Bom sabor, mas não veio borda recheada!",
            ),
            ItemReview(
                title = "Huurh Awwgggghhh!",
                owner = "Chewie Burger",
                rating = 5,
                review = "Melhor hamburger do mundo e do brasil!",
            ),
            ItemReview(
                title = "Pizza de 4 queijos",
                owner = "DiPizzas",
                rating = 1,
                review = "Muito bom, perfeito!",
            ),
            ItemReview(
                title = "Pizza Tradicional + Refri",
                owner = "DiPizzas",
                rating = 5,
                review = "Ótima pizza, recomendo muito!",
            ),
        )

        items.forEach { item ->
            Database.get().itemReviewDao().insert(item)
        }
    }

    private suspend fun populateItemOwners() {
        val items = listOf(
            ItemOwner(
                name = "9 Pasteis",
                location = "Pátio Batel",
                description = "Pastelaria",
            ),
            ItemOwner(
                name = "DiPizzas",
                location = "Jockey Plaza",
                description = "Pizzaria do Di",
            ),
            ItemOwner(
                name = "Dona Florinda",
                location = "Acapulco",
                description = "Prefiro evitar a fadiga",
            ),
            ItemOwner(
                name = "Burger Queen",
                location = "Shopping Estação",
                description = "A rainha dos hamburgeres!",
            ),
            ItemOwner(
                name = "Mc Donaldinho",
                location = "Shopping Estação",
                description = "O diblador dos hamburgeres!",
            ),
            ItemOwner(
                name = "Não lembro",
                location = "Shopping Estação",
                description = "Não lembro, só sei que era bom!",
            ),
            ItemOwner(
                name = "Chewie Burger",
                location = "Curitiba",
                description = "Huurh Awwgggghhh!",
            ),
        )

        items.forEach { item ->
            Database.get().itemOwnerDao().insert(item)
        }
    }
}
