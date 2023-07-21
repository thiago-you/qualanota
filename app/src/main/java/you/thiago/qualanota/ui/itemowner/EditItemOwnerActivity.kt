package you.thiago.qualanota.ui.itemowner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.koalaloadinglibrary.KoalaLoadingDialog
import you.thiago.qualanota.R
import you.thiago.qualanota.components.DeleteAlert
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemOwner
import you.thiago.qualanota.validator.ItemOwnerValidator

class EditItemOwnerActivity : AppCompatActivity() {

    private var itemOwner = ItemOwner()

    private val edtName: EditText by lazy { findViewById(R.id.edtName) }
    private val edtLocation: EditText by lazy { findViewById(R.id.edtLocation) }
    private val edtDescription: EditText by lazy { findViewById(R.id.edtDescription) }

    private val btnSaveAction: FloatingActionButton by lazy { findViewById(R.id.fabSaveAction) }
    private val btnActionDelete: TextView by lazy { findViewById(R.id.actionDelete) }

    private var loadingDialog: KoalaLoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_item_owner)

        intent.extras?.also { data ->
            setupItem(data)
        }

        setupToolbar()
        setupInterface()

        if (itemOwner.id == 0) {
            finish()
        }
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

    private fun setupItem(data: Bundle) {
        itemOwner = ItemOwner(
            id = data.getInt("id"),
            name = data.getString("name"),
            location = data.getString("location"),
            description = data.getString("description"),
        )
    }

    private fun setupInterface() {
        btnActionDelete.visibility = View.VISIBLE

        edtName.setText(itemOwner.name)
        edtLocation.setText(itemOwner.location)
        edtDescription.setText(itemOwner.description)

        btnSaveAction.setOnClickListener {
            runCatching {
                itemOwner.name = edtName.text.toString()
                itemOwner.location = edtLocation.text.toString()
                itemOwner.description = edtDescription.text.toString()

                ItemOwnerValidator.validate(itemOwner)

                loadingDialog = KoalaLoadingDialog().also {
                    it.setText(getString(R.string.saving))
                    it.show(supportFragmentManager, "loading")
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    Database.get().itemOwnerDao().update(itemOwner)

                    Thread.sleep(2000)
                    updateItemReviews(itemOwner)

                    lifecycleScope.launch(Dispatchers.Main) {
                        val data = Intent().apply {
                            putExtra("id", itemOwner.id)
                            putExtra("updated", true)
                        }

                        loadingDialog?.dismissAllowingStateLoss()

                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }
                }

                return@runCatching
            }.onFailure {
                loadingDialog?.dismissAllowingStateLoss()
                Toast.makeText(this@EditItemOwnerActivity, it.message, Toast.LENGTH_LONG).show()
            }
        }

        btnActionDelete.setOnClickListener {
            DeleteAlert.show(this) {
                runCatching {
                    loadingDialog = KoalaLoadingDialog().also {
                        it.setText(getString(R.string.deleting))
                        it.show(supportFragmentManager, "loading")
                    }

                    lifecycleScope.launch(Dispatchers.IO) {
                        Database.get().itemOwnerDao().delete(itemOwner)

                        Thread.sleep(2000)

                        lifecycleScope.launch(Dispatchers.Main) {
                            val data = Intent().apply {
                                putExtra("id", itemOwner.id)
                                putExtra("deleted", true)
                            }

                            loadingDialog?.dismissAllowingStateLoss()

                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }
                }.onFailure {
                    loadingDialog?.dismissAllowingStateLoss()
                    Toast.makeText(this@EditItemOwnerActivity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun updateItemReviews(itemOwner: ItemOwner) {
        val ownerName: String? = intent.extras?.getString("name")

        if (ownerName.isNullOrBlank()) {
            return
        }

        val list = Database.get().itemReviewDao().loadAllByOwners(ownerName).map { itemReview ->
            itemReview.apply {
                owner = itemOwner.name
            }
        }

        list.forEach { itemReview ->
            Database.get().itemReviewDao().update(itemReview)
        }
    }
}
