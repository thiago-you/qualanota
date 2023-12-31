package you.thiago.qualanota.ui.itemowner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import you.thiago.koalaloadinglibrary.KoalaLoadingDialog
import you.thiago.qualanota.R
import you.thiago.qualanota.data.Database
import you.thiago.qualanota.data.model.ItemOwner
import you.thiago.qualanota.validator.ItemOwnerValidator

class NewItemOwnerActivity : AppCompatActivity() {

    private val name: EditText by lazy { findViewById(R.id.edtName) }
    private val location: EditText by lazy { findViewById(R.id.edtLocation) }
    private val description: EditText by lazy { findViewById(R.id.edtDescription) }

    private val fabSaveAction: FloatingActionButton by lazy { findViewById(R.id.fabSaveAction) }

    private var loadingDialog: KoalaLoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_item_owner)

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
        fabSaveAction.setOnClickListener {
            runCatching {
                val itemOwner = ItemOwner(
                    name = name.text.toString(),
                    location = location.text.toString(),
                    description = description.text.toString(),
                )

                ItemOwnerValidator.validate(itemOwner)

                loadingDialog = KoalaLoadingDialog().also {
                    it.setText(getString(R.string.saving))
                    it.show(supportFragmentManager, "loading")
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    Database.get().itemOwnerDao().insert(itemOwner)

                    Thread.sleep(2000)

                    val data = Intent().apply {
                        putExtra("selected", itemOwner.name)
                        putExtra("inserted", true)
                    }

                    loadingDialog?.dismissAllowingStateLoss()

                    setResult(Activity.RESULT_OK, data)
                    finish()
                }

                return@runCatching
            }.onFailure {
                loadingDialog?.dismissAllowingStateLoss()
                Toast.makeText(this@NewItemOwnerActivity, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
