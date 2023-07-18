package you.thiago.qualanota.ui.itemreview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.qualanota.R
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemReview
import you.thiago.qualanota.ui.itemowner.NewItemOwnerActivity

class NewItemReviewActivity : AppCompatActivity() {

    private val title: EditText by lazy { findViewById(R.id.edtTitle) }
    private val owner: Spinner by lazy { findViewById(R.id.spnOwner) }
    private val rating: EditText by lazy { findViewById(R.id.edtRating) }
    private val review: EditText by lazy { findViewById(R.id.edtReview) }

    private val fabSaveAction: FloatingActionButton by lazy { findViewById(R.id.fabSaveAction) }
    private val actionNewOwner: TextView by lazy { findViewById(R.id.actionAddOwner) }

    private val newOwnerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selected = result.data?.getStringExtra("selected")

            if (!selected.isNullOrBlank()) {
                loadSpinner(selected)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_review_item)
        setupToolbar()
        setupInterface()
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
        loadSpinner("Selecione")

        fabSaveAction.setOnClickListener {
            val itemReview = ItemReview(
                title = title.text.toString(),
                owner = owner.selectedItem.toString(),
                rating = rating.text.toString().toInt(),
                review = review.text.toString(),
            )

            lifecycleScope.launch(Dispatchers.IO) {
                Database.get().itemReviewDao().insert(itemReview)

                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        actionNewOwner.setOnClickListener {
            newOwnerResult.launch(Intent(this, NewItemOwnerActivity::class.java))
        }
    }

    private fun loadSpinner(selected: String = "") {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = Database.get().itemOwnerDao().getAll().map { it.name }.toMutableList()

            list.add(0, "Selecione")

            lifecycleScope.launch(Dispatchers.Main) {
                val ownerAdapter = ArrayAdapter(
                    this@NewItemReviewActivity,
                    android.R.layout.simple_spinner_item,
                    list.toTypedArray(),
                )

                ownerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                with(owner) {
                    adapter = ownerAdapter
                    prompt = "Selecione"
                    gravity = Gravity.CENTER

                    if (selected.isNotBlank()) {
                        setSelection(list.indexOf(selected), false)
                    }
                }
            }
        }
    }
}
